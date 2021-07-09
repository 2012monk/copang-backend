package com.alconn.copang.search;

import com.alconn.copang.shipment.ShippingChargeType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
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

    @JsonDeserialize(converter = EpochTimeToDateConvert.class)
    private LocalDate startDate;

    @JsonDeserialize(converter = EpochTimeToDateConvert.class)
    private LocalDate endDate;

    private String keyword;

    private ShippingChargeType shippingChargeType;

    private Integer overRating;

    private Long categoryId;
}
