package com.alconn.copang.search;

import com.alconn.copang.shipment.ShipmentType;
import com.alconn.copang.shipment.ShippingChargeType;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor @AllArgsConstructor
@Getter
public class ItemSearchCondition {

    private Integer page = 0;

    private Integer size = 30;

    private String brand;

    private Integer priceOver;

    private Integer priceUnder;

    private LocalDate startDate;

    private LocalDate endDate;

    private String keyword;

    private ShippingChargeType shippingChargeType;

    private Integer overRating;

    private Long categoryId;
}
