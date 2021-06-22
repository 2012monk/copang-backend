package com.alconn.copang.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@NoArgsConstructor @AllArgsConstructor
@Getter
@Entity
public class Address {

    @Id @GeneratedValue
    private Long id;

    private String city;

    private String detail;

    private String mobile;

    private String preRequest;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

}
