package com.alconn.copang.order;

import com.alconn.copang.exceptions.NoSuchEntityExceptions;
import com.alconn.copang.order.dto.OrderForm;
import com.alconn.copang.order.mapper.OrderItemMapper;
import com.alconn.copang.order.mapper.OrderMapper;
import com.alconn.copang.order.mapper.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository repo;

    private final OrderMapper mapper;

    private final OrderItemMapper orderItemMapper;


    @Transactional
    public OrderForm.Response createOrder(OrderForm.Create form) {

        Orders orders = mapper.createToEntity(form);

        repo.save(orders);

        OrderForm.Response response = mapper.toResponse(orders);

        return response;
    }


    @Transactional
    public OrderForm.Response updateOrderState(Long orderId, OrderStatus status) throws NoSuchEntityExceptions {

        Orders orders = repo.findById(orderId).orElseThrow(NoSuchEntityExceptions::new);

        orders.proceedOrder();
        return OrderForm.Response.builder()
                .orderId(orderId)
                .orderStatus(orders.getOrderState())
                .build();
    }

    public List<OrderForm.Response> listOrderClient(Long clientId) {
        List<Orders> ordersList = repo.findOrdersByClient_Id(clientId);

        List<OrderForm.Response> responses = ordersList.stream().map(o ->
                        OrderForm.Response.builder()
                        .orderId(o.getOrderId())
                        .orderItemFormList(
                        o.getOrderItemList().stream().map(orderItemMapper::toDto).collect(Collectors.toList())).build()
                        ).collect(Collectors.toList());

        return responses;

    }
}
