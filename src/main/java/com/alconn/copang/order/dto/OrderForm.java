package com.alconn.copang.order.dto;

import com.alconn.copang.address.Address;
import com.alconn.copang.address.AddressForm;
import com.alconn.copang.client.UserForm;
import com.alconn.copang.order.OrderStatus;
import com.alconn.copang.payment.PaymentType;
import com.alconn.copang.shipment.LogisticCode;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.validation.annotation.Validated;

@Getter
public class OrderForm {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    public static class Create{

        @NotNull(message = "주소 아이디 는 필수 입력값입니다")
        private Long addressId;

//        @NotEmpty(message = "결제번호는 존재해야합니다")
        private String uid;

        private PaymentType paymentType;

        @Valid
        @Size(min = 1, message = "주문 상품은 최소 하나이상 존재해야 합니다")
        private List<OrderItemForm> orderItems;

        private Integer totalAmount;

        private Integer totalPrice;
    }

    @NoArgsConstructor @AllArgsConstructor

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Builder
    @Getter
    public static class Response{

        private Long orderId;

        @JsonFormat(timezone = "Seoul/Asia", pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime orderDate;

        private OrderStatus orderStatus;

        private AddressForm address;

        private UserForm.Response client;

        private List<OrderItemForm> orderItems;

        private int totalPrice;

        private int totalAmount;

        private String uid;

    }
}
