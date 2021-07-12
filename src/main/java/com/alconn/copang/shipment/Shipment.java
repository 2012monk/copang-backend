package com.alconn.copang.shipment;

import com.alconn.copang.address.Address;
import com.alconn.copang.order.OrderItem;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor @AllArgsConstructor
@Getter
@Entity
public class Shipment {

    @Id @GeneratedValue
    private Long shipmentId;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address shippingPlace;

    @ManyToOne
    @JoinColumn(name = "shipment_info_id")
    private ShipmentInfo shipmentInfo;

    @Builder.Default
    @OneToMany(mappedBy = "shipment")
    private List<OrderItem> orderItems = new ArrayList<>();

    private String trackingNumber;

    private Integer shippingCharge;

    public Shipment(Long shipmentId) {
        this.shipmentId = shipmentId;
    }

    public Shipment(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }
}
