package com.alconn.copang.store;

import com.alconn.copang.client.Client;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor @NoArgsConstructor
@Getter
@Entity
public class Store {

    @Id @GeneratedValue
    private Long id;

    private String storeName;

    private String description;

    @CreationTimestamp
    private LocalDateTime createdTime;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client user;


}
