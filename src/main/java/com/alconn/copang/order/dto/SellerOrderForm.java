package com.alconn.copang.order.dto;

import com.alconn.copang.address.AddressForm;
import com.alconn.copang.client.UserForm;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class SellerOrderForm {


    @Builder
    @NoArgsConstructor @AllArgsConstructor
    @Getter
    public static class Response{

        private Long sellerOrderId;

        private UserForm.Response client;

        private List<OrderItemForm> orderItems;

        private AddressForm address;

        @JsonFormat(pattern = "yyyy/MM/dd-HH:mm", locale = "Seoul/Asia", shape = JsonFormat.Shape.STRING)
        private LocalDateTime orderDate;

        private Integer totalPrice;

        private Integer totalAmount;

    }

}
