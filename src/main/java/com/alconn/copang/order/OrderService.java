package com.alconn.copang.order;

import com.alconn.copang.exceptions.NoSuchEntityExceptions;
import com.alconn.copang.exceptions.UnauthorizedException;
import com.alconn.copang.exceptions.ValidationException;
import com.alconn.copang.order.dto.OrderForm;
import com.alconn.copang.order.dto.OrderForm.Response;
import com.alconn.copang.order.mapper.OrderMapper;
import com.alconn.copang.payment.ImpPaymentInfo;
import com.alconn.copang.payment.PaymentService;
import com.alconn.copang.seller.Seller;
import java.util.List;
import java.util.Map;
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
        throws NoSuchEntityExceptions, ValidationException, UnauthorizedException {

//        String uid = form.getUid();
        ImpPaymentInfo impPaymentInfo = paymentService.validatePayment(uid, orderId);

        Orders orders = repo.findById(orderId)
            .orElseThrow(() -> new NoSuchEntityExceptions("주문번호가 존재하지 않습니다"));

        orders.calculateTotal();
        if (orders.getTotalPrice() != (int) impPaymentInfo.getAmount()) {
            throw new ValidationException("요청하신 주문정보의 가격과 일치하지 않습니다");
        }

        if (!orders.getClient().getClientId().equals(clientId)) {
            throw new UnauthorizedException("주문에 대한 권한이 없습니다");
        }
        orders.setPayment(impPaymentInfo);

        orders.paymentComplete();

        List<OrderItem> orderItems = orders.getOrderItemList();

        Map<Seller, List<OrderItem>> selectBySeller =
            orderItems.stream().collect(Collectors.groupingBy(c -> c.getItemDetail().getItem().getSeller()));

        List<SellerOrder> sellerOrders =
            selectBySeller.entrySet().stream().map(c ->
            SellerOrder.builder()
            .seller(c.getKey())
            .orderItems(c.getValue())
            .build()
                ).collect(Collectors.toList());

        sellerOrders.forEach(SellerOrder::placeSellerOrder);

        sellerOrderRepository.saveAll(sellerOrders);
        repo.save(orders);

        return orderMapper.toResponse(orders);
    }

    @Transactional
    public void setSellerOrder(Long orderId) throws NoSuchEntityExceptions {
        Orders orders = repo.findById(orderId).orElseThrow(NoSuchEntityExceptions::new);
        List<OrderItem> orderItems = orders.getOrderItemList();

        Map<Seller, List<OrderItem>> selectBySeller =
            orderItems.stream().collect(Collectors.groupingBy(c -> c.getItemDetail().getItem().getSeller()));

        List<SellerOrder> sellerOrders =
            selectBySeller.entrySet().stream().map(c ->
                SellerOrder.builder()
                    .seller(c.getKey())
                    .orderItems(c.getValue())
                    .build()
            ).collect(Collectors.toList());

        sellerOrders.forEach(SellerOrder::placeSellerOrder);

        sellerOrderRepository.saveAll(sellerOrders);
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
    public OrderForm.Response orderPayment(Long orderId) throws NoSuchEntityExceptions {

        Orders orders = repo.findById(orderId).orElseThrow(NoSuchEntityExceptions::new);

        orders.proceedOrder();
        return OrderForm.Response.builder()
            .orderId(orderId)
            .orderStatus(orders.getOrderStatus())
            .build();
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

    public List<Response> getOrdersBySeller(Long sellerId) {
        List<SellerOrder> sellerOrders = sellerOrderRepository.findSellerOrdersBySeller_ClientId(sellerId);


        return null;
    }

    public List<SellerOrder> getSellers(Long sellerId) {
        return sellerOrderRepository.findSellerOrdersBySeller_ClientId(sellerId);
    }
}
