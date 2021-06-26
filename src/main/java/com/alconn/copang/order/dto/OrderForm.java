package com.alconn.copang.order.dto;

import com.alconn.copang.client.Address;
import com.alconn.copang.client.Client;
import com.alconn.copang.client.UserForm;
import com.alconn.copang.order.OrderItem;
import com.alconn.copang.order.OrderStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class OrderForm {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    public static class Create{

        @NotBlank @Positive
        private Long clientId;

        @NotEmpty
        private String username;

        @NotBlank @Positive
        private Long addressId;

        private String tid;

        private List<OrderItem> orderItemList;

        private List<OrderItemForm> orderItems;

        @Positive
        private int totalAmount;

        @Positive
        private int totalPrice;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Builder
    @Getter
    public static class Response{

        private Long orderId;

        private Long clientId;

        private LocalDateTime orderDate;

        private OrderStatus orderStatus;

        private Address address;

        private UserForm.Response client;

        private List<OrderItemForm> orderItems;
    }
}
