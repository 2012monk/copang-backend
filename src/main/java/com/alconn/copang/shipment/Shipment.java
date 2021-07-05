package com.alconn.copang.shipment;

import com.alconn.copang.address.Address;
import com.alconn.copang.order.OrderItem;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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

    @OneToMany
    @JoinColumn(name = "order_item_id")
    private List<OrderItem> orderItems;

    private String trackingNumber;

    private Integer shippingCharge;

}
