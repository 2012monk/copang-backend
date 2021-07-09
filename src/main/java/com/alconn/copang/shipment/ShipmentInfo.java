package com.alconn.copang.shipment;

import com.alconn.copang.address.Address;

import javax.persistence.*;

import com.alconn.copang.item.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor @AllArgsConstructor
@Getter
@Entity
public class ShipmentInfo {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "release_place_id")
    private Address shippingPlace;

    @Enumerated(EnumType.STRING)
    private LogisticCode logisticCompany;

    @Enumerated(EnumType.STRING)
    private ShippingChargeType shippingChargeType;

    private int freeShipOverPrice;

    private Integer releaseDate;

    private Integer shippingPrice;

    @OneToOne(mappedBy = "shipmentInfo")
    private Item item;
    
    public void update(LogisticCode logisticCompany, ShippingChargeType shippingChargeType, int freeShipOverPrice, Integer releaseDate, Integer shippingPrice){
        this.logisticCompany= logisticCompany!=null? logisticCompany:this.logisticCompany;
        this.shippingChargeType= shippingChargeType!=null? shippingChargeType:this.shippingChargeType;
        this.freeShipOverPrice= freeShipOverPrice!=0? freeShipOverPrice:this.freeShipOverPrice;
        this.releaseDate= releaseDate!=null? releaseDate:this.releaseDate;
        this.shippingPrice= shippingPrice!=null? shippingPrice:this.shippingPrice;

    }
    

}
