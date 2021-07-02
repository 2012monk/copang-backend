package com.alconn.copang.cart;

import com.alconn.copang.item.ItemDetailForm;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

public class CartForm {

    @Builder
    @NoArgsConstructor @AllArgsConstructor
    @Getter
    public static class Add {

        private Long itemId;

        private Long itemDetailId;

        private int amount;
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
