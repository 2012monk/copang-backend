package com.alconn.copang.shipment;

import com.alconn.copang.seller.Seller;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor @AllArgsConstructor
@Builder
@Getter
@Entity
public class ShipmentAddress {

    @Id @GeneratedValue
    private Long shipmentAddress;

    private String addressName;

    private String address;

    private String detail;

    private String postalCode;

    private String contact;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Seller seller;

}
