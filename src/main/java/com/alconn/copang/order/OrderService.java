package com.alconn.copang.order;

import com.alconn.copang.client.Address;
import com.alconn.copang.client.Client;
import com.alconn.copang.client.UserForm;
import com.alconn.copang.exceptions.NoSuchEntityExceptions;
import com.alconn.copang.item.Item;
import com.alconn.copang.item.ItemDetail;
import com.alconn.copang.order.dto.OrderForm;
import com.alconn.copang.order.dto.OrderItemForm;
import com.alconn.copang.order.mapper.OrderItemMapper;
import com.alconn.copang.order.mapper.OrderMapper;
import com.alconn.copang.order.mapper.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository repo;

//    private final OrderMapper mapper;

    private final OrderItemRepository orderItemRepository;

    private final OrderItemMapper orderItemMapper;


    @Transactional
    public OrderForm.Response createOrder(OrderForm.Create form) {

//        Orders orders = mapper.convertCreate(form);

//        List<ItemDetail> itemDetails = form.getOrderItems()
//                .stream()
//                .map(i -> ItemDetail.builder()
//                .item(Item.builder().id(i.getItemId()).build())
//                .id(i.getItemDetailId())
//                .build())
//                .collect(Collectors.toList());
        // Mapping OrderItems
        List<OrderItem> orderItems = form.getOrderItems()
                .stream()
                .map(i -> OrderItem
                        .builder()
                        .itemDetail(
                                ItemDetail
                                .builder()
                                .item(Item.builder().itemName(i.getItemName()).id(i.getItemId()).build())
                                .id(i.getItemDetailId())
                                .build())
                        .amount(i.getAmount())
                        .build()
                )
                .collect(Collectors.toList());

//        repo.save(orders);
//        List<OrderItem> orderItemList = form.getOrderItemList();

        orderItemRepository.saveAll(orderItems);

        Orders orders = Orders.builder()
                .client(Client.builder().clientId(form.getClientId()).build())
                .address(Address.builder().addressId(form.getAddressId()).build())
                .orderState(OrderStatus.READY)
                .orderDate(LocalDateTime.now())
                .totalAmount(form.getTotalAmount())
                .totalPrice(form.getTotalPrice())
                .build();

        repo.save(orders);
        orders.setOrderItemList(orderItems);
//        OrderForm.Response response = mapper.toResponse(orders);

        Orders o = getOneOrders(orders.getOrderId());
        OrderForm.Response r =
                OrderForm.Response.builder()
                .orderDate(o.getOrderDate())
                .orderId(o.getOrderId())
                .orderItems(o.getOrderItemList()
                        .stream().map(i ->
                        OrderItemForm.builder()
                            .itemDetailId(i.getItemDetail().getId())
                            .itemId(i.getId())
                            .itemName(i.getItemDetail().getItem().getItemName())
                                .option(i.getItemDetail().getOption())
                            .amount(i.getAmount())
                            .price(i.getItemDetail().getPrice())
                            .build()
                        ).collect(Collectors.toList()))
                .clientId(orders.getClient().getClientId())
                .build();

        return r;
    }

    private Orders getOneOrders(Long id) {
        return repo.getById(id);
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
        List<Orders> ordersList = repo.findOrdersByClient_ClientId(clientId);

        List<OrderForm.Response> responses = ordersList.stream().map(o ->
                OrderForm.Response.builder()
                        .orderId(o.getOrderId())
                        .orderItems(
                                o.getOrderItemList().stream().map(orderItemMapper::toDto).collect(Collectors.toList())).build()
        ).collect(Collectors.toList());

        return responses;

    }
}
