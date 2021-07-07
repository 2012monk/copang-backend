package com.alconn.copang.order;

import com.alconn.copang.exceptions.NoSuchEntityExceptions;
import com.alconn.copang.exceptions.UnauthorizedException;
import com.alconn.copang.exceptions.ValidationException;
import com.alconn.copang.order.dto.OrderForm;
import com.alconn.copang.order.mapper.OrderMapper;
import com.alconn.copang.payment.ImpPaymentInfo;
import com.alconn.copang.payment.PaymentService;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
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
    public OrderForm.Response orderPayment(String uid, @NotNull Long clientId, Long orderId)
        throws NoSuchEntityExceptions, ValidationException, AccessDeniedException {

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

        repo.save(orders);

        return orderMapper.toResponse(orders);
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

//        List<OrderForm.Response> responses = ordersList.stream().map(o ->
//            OrderForm.Response.builder()
//                .orderId(o.getOrderId())
//                .orderItems(
//                    o.getOrderItemList().stream().map(orderItemMapper::toDto)
//                        .collect(Collectors.toList())).build()
//        ).collect(Collectors.toList());

        return ordersList.stream().map(
            orderMapper::toResponse
        ).collect(Collectors.toList());

    }

    public OrderForm.Response getOneOrder(Long orderId) {
        Orders orders = repo.getById(orderId);
//        orders.getOrderItemList().forEach(OrderItem::calculateTotal);
        orders.calculateTotal();
        return orderMapper.toResponse(orders);
        //        AddressForm address = AddressForm.builder()
//                .receiverName(orders.getAddress().getReceiverName())
//                .address(orders.getAddress().getAddress())
//                .receiverPhone(orders.getAddress().getReceiverPhone())
//                .preRequest(orders.getAddress().getPreRequest())
//                .detail(orders.getAddress().getDetail())
//                .addressId(orders.getAddress().getAddressId())
//                .build();
//        OrderForm.Response response =
//                OrderForm.Response
//                .builder()
//                .orderId(orderId)
//                .orderStatus(orders.getOrderState())
//                .address(address)
//                .orderDate(orders.getOrderDate())
//                .totalAmount(orders.getTotalAmount())
//                .totalPrice(orders.getTotalPrice())
//                .orderItems(
//                        orders.getOrderItemList()
//                        .stream().map(i ->
//                                OrderItemForm.builder()
//                                .amount(i.getAmount())
//                                .unitTotal(i.getTotal())
//                                .price(i.getItemDetail().getPrice())
//                                .itemId(i.getItemDetail().getItem().getItemId())
//                                .itemDetailId(i.getItemDetail().getItemDetailId())
//                                .itemName(i.getItemDetail().getItem().getItemName())
//                                .optionName(i.getItemDetail().getOptionName())
//                                .optionValue(i.getItemDetail().getOptionValue())
//                                .build()
//                        ).collect(Collectors.toList())
//                )
//                .client(clientMapper.toResponse(orders.getClient()))
//                .build();

    }
}
