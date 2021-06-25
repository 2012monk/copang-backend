package com.alconn.copang.order.dto;

import com.alconn.copang.client.Address;
import com.alconn.copang.client.Client;
import com.alconn.copang.order.OrderItem;
import com.alconn.copang.order.OrderStatus;
import lombok.*;

import java.util.List;

@Getter
public class OrderForm {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    public static class Create{

        private Long clientId;

        private String username;

        private String buyersPhone;

        private String buyerRealName;

        private Long addressId;

        private String tid;

        private List<OrderItem> orderItemList;

    }

    @Builder
    @Getter
    public static class Response{

        private Long orderId;

        private OrderStatus orderStatus;

        private Address address;

        private Client client;

        private List<OrderItemForm> orderItemFormList;
    }
}
