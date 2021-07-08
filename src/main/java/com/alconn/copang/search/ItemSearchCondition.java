package com.alconn.copang.search;

import com.alconn.copang.shipment.ShipmentType;
import com.alconn.copang.shipment.ShippingChargeType;
import java.time.LocalDate;

public class ItemSearchCondition {

    private Integer page;

    private Integer size;

    private String brand;

    private Integer priceOver;

    private Integer priceUnder;

    private LocalDate startDate;

    private LocalDate endDate;

    private String keywords;

    private ShippingChargeType shippingChargeType;

    private Integer overRating;

}
