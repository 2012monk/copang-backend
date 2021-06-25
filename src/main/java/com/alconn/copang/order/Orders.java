package com.alconn.copang.order;

import com.alconn.copang.client.Address;
import com.alconn.copang.client.Client;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor @AllArgsConstructor
@Getter
@Builder
@Entity

public class Orders {

    @Id @GeneratedValue
    private Long orderId;

    private int totalPrice;

    private int totalAmount;

    private String requirement;

    private String tid;

    @OneToMany(mappedBy = "orders")
    private List<OrderItem> orderItemList;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime orderDate;

    @UpdateTimestamp
    private LocalDateTime lastUpdatedDate;

    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private OrderStatus orderState = OrderStatus.READY;


    public void proceedOrder() {
        this.orderState = OrderStatus.PROCEED;
    }

    public void cancelOrder() {
        this.orderState = OrderStatus.CANCELED;
    }

    public void doneOrder() {
        this.orderState = OrderStatus.DONE;
    }
}
