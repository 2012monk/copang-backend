package com.alconn.copang.order;

import com.alconn.copang.address.Address;
import com.alconn.copang.address.AddressForm;
import com.alconn.copang.client.Client;
import com.alconn.copang.client.ClientMapper;
import com.alconn.copang.exceptions.NoSuchEntityExceptions;
import com.alconn.copang.item.Item;
import com.alconn.copang.item.ItemDetail;
import com.alconn.copang.order.dto.OrderForm;
import com.alconn.copang.order.dto.OrderItemForm;
import com.alconn.copang.order.mapper.OrderItemMapper;
import lombok.RequiredArgsConstructor;
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

    private final OrderItemRepository orderItemRepository;

    private final OrderItemMapper orderItemMapper;

    private final ClientMapper clientMapper;

    @Transactional
    public OrderForm.Response createOrder(OrderForm.Create form) {



        // item id, item detail id 키값만으로 빌드해서 저장한다

        List<OrderItem> orderItems = form.getOrderItems()
                .stream()
                .map(i ->
                        OrderItem
                        .builder()
                        .amount(i.getAmount())
                        .itemDetail(
                                ItemDetail
                                .builder()
                                .itemDetailId(i.getItemDetailId())
                                .item(Item
                                        .builder()
//                                        .itemName(i.getItemName())
                                        .itemId(i.getItemId())
                                        .build())
                                .build())
                        .build()
                )
                .collect(Collectors.toList());


        Orders orders = Orders.builder()
                .client(Client.builder().clientId(form.getClientId()).build())
                .address(Address.builder().addressId(form.getAddressId()).build())
                .orderState(OrderStatus.READY)
                .orderDate(LocalDateTime.now())
                .totalAmount(form.getTotalAmount())
                .totalPrice(form.getTotalPrice())
                .build();

        orders.setOrderItemList(orderItems);
        repo.save(orders);

//        orderItemRepository.saveAllAndFlush(orderItems);
//        OrderForm.Response response = mapper.toResponse(orders);

//        Orders o = getOneOrders(orders.getOrderId());

        //        System.out.println("orderItems = " + orderItems.get(0).getItemDetail().getItem().getItemCreate());
        return OrderForm.Response.builder()
        .orderDate(orders.getOrderDate())
        .orderId(orders.getOrderId())
        .orderItems(orders.getOrderItemList()
                .stream().map(i ->
                OrderItemForm.builder()
                    .itemDetailId(i.getItemDetail().getItemDetailId())
                    .itemId(i.getId())
                    .itemName(i.getItemDetail().getItem().getItemName())
                    .optionName(i.getItemDetail().getOptionName())
                    .optionValue(i.getItemDetail().getOptionValue())
                    .amount(i.getAmount())
                    .price(i.getItemDetail().getPrice())
                    .build()
                ).collect(Collectors.toList()))
        .clientId(orders.getClient().getClientId())
        .totalAmount(orders.getTotalAmount())
        .totalPrice(orders.getTotalPrice())
        .build();
    }

    private Orders getOneOrders(Long id) {
        return repo.getById(id);
    }

    private List<OrderItem> getOrderItems(Long orderId) {
        return orderItemRepository.findOrderItemsByOrders_OrderId(orderId);
    }


    @Transactional
    public OrderForm.Response proceedOrder(Long orderId) throws NoSuchEntityExceptions {

        Orders orders = repo.findById(orderId).orElseThrow(NoSuchEntityExceptions::new);

        orders.proceedOrder();
        return OrderForm.Response.builder()
                .orderId(orderId)
                .orderStatus(orders.getOrderState())
                .build();
    }

    @Transactional
    public OrderForm.Response cancelOrder(Long orderId) throws NoSuchEntityExceptions {
        Orders orders = repo.findById(orderId).orElseThrow(NoSuchEntityExceptions::new);
        orders.cancelOrder();
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

    public OrderForm.Response getOneOrder(Long orderId) {
        Orders orders = repo.getById(orderId);

        AddressForm address = AddressForm.builder()
                .receiverName(orders.getAddress().getReceiverName())
                .city(orders.getAddress().getCity())
                .receiverPhone(orders.getAddress().getReceiverPhone())
                .preRequest(orders.getAddress().getPreRequest())
                .detail(orders.getAddress().getDetail())
                .addressId(orders.getAddress().getAddressId())
                .build();
        OrderForm.Response response =
                OrderForm.Response
                .builder()
                .orderId(orderId)
                .orderStatus(orders.getOrderState())
                .address(address)
                .orderDate(orders.getOrderDate())
                .totalAmount(orders.getTotalAmount())
                .totalPrice(orders.getTotalPrice())
                .orderItems(
                        orders.getOrderItemList()
                        .stream().map(i ->
                                OrderItemForm.builder()
                                .amount(i.getAmount())
                                .unitTotal(i.getTotal())
                                .price(i.getItemDetail().getPrice())
                                .itemId(i.getItemDetail().getItem().getItemId())
                                .itemDetailId(i.getItemDetail().getItemDetailId())
                                .itemName(i.getItemDetail().getItem().getItemName())
                                .optionName(i.getItemDetail().getOptionName())
                                .optionValue(i.getItemDetail().getOptionValue())
                                .build()
                        ).collect(Collectors.toList())
                )
                .client(clientMapper.toResponse(orders.getClient()))
                .build();

        return response;

    }
}
