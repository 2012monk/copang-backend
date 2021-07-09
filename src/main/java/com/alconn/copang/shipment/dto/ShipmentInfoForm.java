package com.alconn.copang.shipment.dto;

import com.alconn.copang.address.Address;
import com.alconn.copang.shipment.LogisticCode;
import com.alconn.copang.shipment.ShippingChargeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ShipmentInfoForm {

    private Long id;

    //출고지주소 나중에 작업한다하심
    private Address shippingPlace;

    //택배사
    private LogisticCode logisticCompany;

    private String companyName;

    //배송비 관련 상태컬럼
    private ShippingChargeType shippingChargeType;

    //얼마이상무료
    private int freeShipOverPrice;

    //출시일
    private Integer releaseDate;

    //배송비
    private Integer shippingPrice;

    @Override
    public String toString() {
        return "ShipmentInfoForm{" +
                "id=" + id +
                ", shippingPlace=" + shippingPlace +
                ", logisticCompany=" + logisticCompany +
                ", shippingChargeType=" + shippingChargeType +
                ", freeShipOverPrice=" + freeShipOverPrice +
                ", releaseDate=" + releaseDate +
                ", shippingPrice=" + shippingPrice +
                '}';
    }
}
