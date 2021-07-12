package com.alconn.copang.order;

import com.alconn.copang.address.Address;
import com.alconn.copang.address.AddressRepository;
import com.alconn.copang.exceptions.NoSuchEntityExceptions;
import com.alconn.copang.exceptions.UnauthorizedException;
import com.alconn.copang.exceptions.ValidationException;
import com.alconn.copang.order.dto.OrderForm;
import com.alconn.copang.order.dto.OrderForm.Response;
import com.alconn.copang.order.dto.OrderItemForm;
import com.alconn.copang.order.dto.ReturnOrderForm;
import com.alconn.copang.order.dto.ReturnOrderForm.AcceptRequest;
import com.alconn.copang.order.dto.SellerOrderForm;
import com.alconn.copang.order.mapper.OrderMapper;
import com.alconn.copang.order.mapper.ReturnOrderMapper;
import com.alconn.copang.order.mapper.SellerOrderMapper;
import com.alconn.copang.payment.ImpCancelInfo;
import com.alconn.copang.payment.ImpPaymentInfo;
import com.alconn.copang.payment.PaymentService;
import com.alconn.copang.shipment.Shipment;
import com.alconn.copang.shipment.ShipmentForm;
import com.alconn.copang.shipment.ShipmentForm.Request;
import java.nio.file.AccessDeniedException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository repo;

    private final OrderItemRepository orderItemRepository;

    private final PaymentService paymentService;

    private final OrderMapper orderMapper;

    private final SellerOrderRepository sellerOrderRepository;

    private final SellerOrderMapper sellerOrderMapper;

    private final SellerOrderService sellerOrderService;

    private final OrderQueryRepository orderQueryRepository;

    private final AddressRepository addressRepository;
    private final ReturnOrderMapper returnOrderMapper;

    @Transactional
    public OrderForm.Response readyOrder(OrderForm.Create form, Long clientId) {
        Orders orders = orderMapper.placeOrder(form, clientId);
        orders.connectOrderItems();
        repo.save(orders);
        return OrderForm.Response.builder()
            .orderId(orders.getOrderId())
            .build();
    }

    @Transactional
    public OrderForm.Response orderPayment(String uid, Long clientId, Long orderId)
        throws NoSuchEntityExceptions, ValidationException, UnauthorizedException, AccessDeniedException {

//        String uid = form.getUid();
        ImpPaymentInfo impPaymentInfo = paymentService.validatePayment(uid, orderId);

        Orders orders = repo.findById(orderId)
            .orElseThrow(() -> new NoSuchEntityExceptions("주문번호가 존재하지 않습니다"));

        orders.calculateTotal();
        if (orders.getTotalPrice() != (int) impPaymentInfo.getAmount()) {
            throw new ValidationException("요청하신 주문정보의 가격과 일치하지 않습니다");
        }

        if (clientId != null &&
            !orders.getClient().getClientId().equals(clientId)) {
            throw new AccessDeniedException("주문에대한 권한이 없습니다");
        }
        orders.setPayment(impPaymentInfo);
        orders.paymentComplete();

        sellerOrderService.placeSellerOrder(orders);
        repo.saveAndFlush(orders);

        return orderMapper.toResponse(orders);
    }

    @Transactional
    public void setSellerOrder(Long orderId) throws NoSuchEntityExceptions {
        Orders orders = repo.findById(orderId).orElseThrow(NoSuchEntityExceptions::new);
        sellerOrderService.placeSellerOrder(orders);
    }


    @Transactional
    public OrderForm.Response placeOrder(OrderForm.Create form, Long clientId) {
        // item id, item detail id 키값만으로 빌드해서 저장한다
        Orders orders = orderMapper.placeOrder(form, clientId);
        orders.connectOrderItems();
        repo.save(orders);

        return orderMapper.toResponse(orders);
    }

    private Orders getOneOrders(Long id) {
        return repo.getById(id);
    }

    private List<OrderItem> getOrderItems(Long orderId) {
        return orderItemRepository.findOrderItemsByOrders_OrderId(orderId);
    }


    @Transactional
    public OrderForm.Response cancelOrder(Long orderId) throws NoSuchEntityExceptions {
        Orders orders = repo.findById(orderId).orElseThrow(NoSuchEntityExceptions::new);
        orders.cancelOrder();
        return OrderForm.Response.builder()
            .orderId(orderId)
            .orderStatus(orders.getOrderStatus())
            .build();
    }

    public List<OrderForm.Response> listOrderClient(Long clientId) {
        List<Orders> ordersList = repo.findOrdersByClient_ClientId(clientId);
        ordersList.forEach(Orders::calculateTotal);

        return ordersList.stream().map(
            orderMapper::toResponse
        ).collect(Collectors.toList());

    }

    public OrderForm.Response getOneOrder(Long orderId) {
        Orders orders = repo.getById(orderId);
//        orders.getOrderItemList().forEach(OrderItem::calculateTotal);
        orders.calculateTotal();
        return orderMapper.toResponse(orders);

    }

    public List<SellerOrderForm.Response> getOrdersBySeller(Long sellerId) {
        List<SellerOrder> sellerOrders = sellerOrderRepository
            .findSellerOrdersBySeller_ClientId(sellerId);
        sellerOrders.forEach(SellerOrder::calculateTotal);

        return sellerOrders.stream()
            .map(s -> sellerOrderMapper.mtoForm(s, s.getOrderItems().get(0).getOrders()))
            .collect(Collectors.toList());
    }

    public List<SellerOrder> getSellers(Long sellerId) {
        return sellerOrderRepository.findSellerOrdersBySeller_ClientId(sellerId);
    }

    @Transactional
    public List<ShipmentForm.Response> placeShipment(List<ShipmentForm.Request> requests, Long sellerId)
        throws AccessDeniedException {
        List<OrderItem> list = orderQueryRepository.findByIds(requests.stream().map(
            Request::getOrderItemId).collect(
            Collectors.toList()));
        if (list.stream()
            .filter(o -> !o.getSellerOrder().getSeller().getClientId().equals(sellerId)).count() > 1) {
            throw new AccessDeniedException("본인인증 실패");
        }


        Map<Long, String> numberMap =
            requests.stream().collect(Collectors.toMap(Request::getOrderItemId,
                Request::getTrackingNumber));

        list.forEach(
            o -> o.setShipment(
                Shipment
                    .builder()
                    .trackingNumber(numberMap.get(o.getOrderItemId()))
                    .build())
        );

        return list.stream().map(i ->
                ShipmentForm.Response.builder()
                .orderItemId(i.getOrderItemId())
                .trackingNumber(i.getShipment().getTrackingNumber())
                .build()
            )
            .collect(Collectors.toList());


    }

    @Transactional
    public ReturnOrderForm.Response receiptReturnOrder(ReturnOrderForm.Request request,
        Long orderItemId, Long clientId)
        throws NoSuchEntityExceptions, AccessDeniedException {
        OrderItem i = orderItemRepository.findById(orderItemId)
            .orElseThrow(NoSuchEntityExceptions::new);
        Orders o = repo.getById(i.getOrders().getOrderId());
        System.out.println("i.getOrders().getOrderStatus() = " + i.getOrders().getOrderStatus());
        if (!i.getOrders().getClient().getClientId().equals(clientId)) {
            throw new AccessDeniedException("본인인증 실패");
        }
        Address address = addressRepository.findById(request.getAddressId())
            .orElseThrow(NoSuchEntityExceptions::new);
        ReturnOrder returnOrder =
            ReturnOrder.builder()
                .address(address)
                .pickupRequest(request.getPickupRequest())
                .returnReason(request.getReturnReason())
                .returnAmount(request.getAmount())
                .returnPrice(i.getItemDetail().getPrice() * request.getAmount())
                .build();

        i.setReturnOrder(returnOrder);

        System.out
            .println("i.getOrders().getImpPaymentInfo() = " + i.getOrders().getImpPaymentInfo());
        if (i.getOrders().getImpPaymentInfo() == null){
            throw new NoSuchEntityExceptions("결제 내역이 없습니다");
        }
        ImpPaymentInfo impPaymentInfo = paymentService.cancelAmount(
            i.getOrders().getImpPaymentInfo().getImp_uid(),
            i.getItemDetail().getPrice() * request.getAmount(),
            request.getReturnReason(),
            i.getOrders().getTotalPrice()
        );

        i.getOrders().setPayment(impPaymentInfo);

        ImpCancelInfo cancelInfo = impPaymentInfo.getCancel_history().stream().min(Comparator.comparing(
            ImpCancelInfo::getCanceled_at
        )).orElseThrow(NoSuchEntityExceptions::new);

        returnOrder.setCancelInfo(cancelInfo);

        return returnOrderMapper.toResponse(returnOrder);
    }

    public Object placeReturnOrder(List<ReturnOrderForm.AcceptRequest> requests, Long sellerId) {

        Map<Long, OrderItem> items = orderQueryRepository.findByIds(requests.stream().map(
            AcceptRequest::getOrderItemId).collect(
            Collectors.toList()))
            .stream().collect(Collectors.toMap(OrderItem::getOrderItemId, i -> i));


        for (AcceptRequest r: requests) {
            OrderItem o = items.get(r.getOrderItemId());
            o.getReturnOrder().updateTrackingNumber(r.getTrackingNumber());
        }

        Map<Orders, List<OrderItem>> returnList =
            items.values().stream().collect(Collectors.groupingBy(OrderItem::getOrders));

        for (Orders o: returnList.keySet()) {
            ImpPaymentInfo impPaymentInfo = paymentService.cancelAmount(
                o.getUid(),
                returnList.get(o).stream().mapToInt(OrderItem::getUnitTotal).sum(),
                returnList.get(o).get(0).getReturnOrder().getReturnReason(),
                o.getTotalPrice()
            );

            o.setPayment(impPaymentInfo);
        }

        return null;
    }


    public List<ReturnOrderForm.Response> getCanceledItems(Long clineId) {

        List<ReturnOrder> orders = orderQueryRepository.getCanceledOrders(clineId);

        return returnOrderMapper.toResponse(orders);
    }
}
