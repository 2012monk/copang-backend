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

    //배송판매자등록
    @Id @GeneratedValue
    private Long id;

    //출고지주소 나중에 작업한다하심
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "release_place_id")
//    private Address shippingPlace;

    //택배사
    @Enumerated(EnumType.STRING)
    private LogisticCode logisticCompany;

    //배송비 관련 상태컬럼
    @Enumerated(EnumType.STRING)
    private ShippingChargeType shippingChargeType;

    //얼마이상무료
    private int freeShipOverPrice;

    //출고일..?
    private Integer releaseDate;

    //배송비
    private Integer shippingPrice;

    @OneToOne(mappedBy = "shipmentInfo")
    private Item item;

    public void itemConnect(Item item){


    }

}
