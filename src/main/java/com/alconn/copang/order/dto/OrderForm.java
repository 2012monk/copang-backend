package com.alconn.copang.order.dto;

import com.alconn.copang.address.Address;
import com.alconn.copang.address.AddressForm;
import com.alconn.copang.client.UserForm;
import com.alconn.copang.order.OrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
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

//        private List<OrderItem> orderItemList;

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

        @JsonFormat(timezone = "Seoul/Asia", pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime orderDate;

        private OrderStatus orderStatus;

//        private Address address;

        private AddressForm address;

        private UserForm.Response client;

        private List<OrderItemForm> orderItems;

        private int totalPrice;

        private int totalAmount;
    }
}
