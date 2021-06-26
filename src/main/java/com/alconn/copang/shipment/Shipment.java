package com.alconn.copang.shipment;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Entity
public class Shipment {

    @Id @GeneratedValue
    private Long shipmentId;

    private String shipmentDetail;
}
