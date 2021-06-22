package com.alconn.copang.order;

import com.alconn.copang.client.Address;
import com.alconn.copang.client.Client;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor @AllArgsConstructor
@Getter
@Builder
@Entity
public class Orders {

    @Id @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "orders")
    private List<OrderItem> orderItemList;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    private LocalDateTime orderDate;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private OrderState orderState = OrderState.INIT;
}
