package com.alconn.copang.cart;

import com.alconn.copang.client.Client;
import com.alconn.copang.order.OrderItem;

import javax.persistence.*;
import java.util.List;

@Entity
public class Cart {

    @Id @GeneratedValue
    private Long cartId;

    @OneToOne
    private Client client;

    @OneToMany
    private List<OrderItem> cartItems;
}
