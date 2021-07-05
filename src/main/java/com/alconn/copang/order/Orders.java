package com.alconn.copang.order;

import com.alconn.copang.address.Address;
import com.alconn.copang.client.Client;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Builder.Default
    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL)
    private final List<OrderItem> orderItemList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @JsonFormat(pattern = "yyyy/MM/dd-HH:mm", locale = "Seoul/Asia", shape = JsonFormat.Shape.STRING)
    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime orderDate;

    @UpdateTimestamp
    private LocalDateTime lastUpdatedDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "address_id")
    private Address address;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.READY;

    public void setClient(Client client) {
        this.client = client;
    }

    public void setOrderItemList(List<OrderItem> orderItemList){
        this.orderItemList.addAll(orderItemList);
        orderItemList.forEach(o -> o.setOrders(this));
    }

    public void addOrderItem(OrderItem item){
        this.orderItemList.add(item);
        item.setOrders(this);
    }

    public void proceedOrder() {
        this.orderStatus = OrderStatus.PROCEED;
    }

    public void cancelOrder() {
        this.orderStatus = OrderStatus.CANCELED;
    }

    public void doneOrder() {
        this.orderStatus = OrderStatus.DONE;
    }

    public void connectOrderItems() {
        this.orderItemList.forEach(o -> o.setOrders(this));
    }

    public void calculateTotal() {
        this.orderItemList.forEach(OrderItem::calculateTotal);
        this.totalAmount = this.orderItemList.stream().mapToInt(OrderItem::getAmount).sum();
        this.totalPrice = this.orderItemList.stream().mapToInt(OrderItem::getUnitTotal).sum();
    }
}
