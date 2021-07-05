package com.alconn.copang.shipment;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
    private ShipmentAddress shippingPlace;

    @Enumerated(EnumType.STRING)
    private LogisticCode logisticCompany;

    @Enumerated(EnumType.STRING)
    private ShippingChargeType shippingChargeType;

    private int freeShipOverPrice;

    private Integer releaseDate;

    private Integer shippingPrice;


}
