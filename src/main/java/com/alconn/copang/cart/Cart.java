package com.alconn.copang.cart;

import com.alconn.copang.client.Client;
import com.alconn.copang.common.JoinItemBaseEntity;
import com.alconn.copang.order.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@NoArgsConstructor @AllArgsConstructor
@Getter
@Entity
public class Cart {

    @Id @GeneratedValue
    private Long cartId;

    @Column(unique = true)
    private Long clientId;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItem> cartItems = new HashSet<>();

    private int totalPrice;

    private int totalAmount;

    public void clearCart(){
        cartItems = null;
//        cartItems.forEach(CartItem::disconnectToCart);
//        this.cartItems = new HashSet<>(1);

    }

    public void addCartItem(CartItem item) {
        this.cartItems.add(item);
        item.connectToCart(this);
    }

    public void addCartItems(List<CartItem> items) {
        this.cartItems.addAll(items);
        items.forEach(i -> i.connectToCart(this));
    }

    public void deleteItem(CartItem item) {
        this.cartItems.remove(item);
    }

    public void setTotalPrice() {
        this.totalPrice = this.cartItems.stream().mapToInt(CartItem::calculateTotal).sum();
    }

    public void setTotalAmount() {
        this.totalAmount = this.cartItems.stream().mapToInt(CartItem::getAmount).sum();
    }
}
