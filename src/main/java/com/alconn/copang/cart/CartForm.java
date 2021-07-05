package com.alconn.copang.cart;

import com.fasterxml.jackson.annotation.JsonInclude;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

public class CartForm {

    @Builder
    @NoArgsConstructor @AllArgsConstructor
    @Getter
    public static class Add {

        @NotNull
        private Long itemId;

        @NotNull
        private Long itemDetailId;

        @NotNull
        private Integer amount;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Builder
    @Getter @NoArgsConstructor @AllArgsConstructor
    public static class Response {

        private Long cartId;

        private Long clientId;

        private Long cartItemId;

        private Long itemId;

        private Long itemDetailId;

        private int totalAmount;

        private int totalPrice;

        private CartItemForm cartItem;
//        private Set<CartItem> cartItems;

        private List<CartItemForm> cartItems;
    }
}
