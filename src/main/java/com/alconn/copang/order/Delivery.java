package com.alconn.copang.order;

import com.alconn.copang.client.Client;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@NoArgsConstructor @AllArgsConstructor
@Builder
@Getter
@Entity
public class Delivery {

    @Id @GeneratedValue
    private Long id;


}