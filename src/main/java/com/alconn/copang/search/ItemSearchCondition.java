package com.alconn.copang.search;

import com.alconn.copang.shipment.ShippingChargeType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class ItemSearchCondition {

    @Builder.Default
    private Integer page = 0;

    @Builder.Default
    private Integer size = 30;

    private String brand;

    private Integer priceOver;

    private Integer priceUnder;

//    @JsonDeserialize(converter = EpochTimeToDateConvert.class)
    private LocalDate startDate;

//    @JsonDeserialize(converter = EpochTimeToDateConvert.class)
    private LocalDate endDate;

    private String keyword;

    private ShippingChargeType shippingChargeType;

    private Integer overRating;

    private Long categoryId;

    private OrderCondition sorted;

    @JsonCreator
    public OrderCondition fromString(@JsonProperty("sorted") String symbol) {
        log.warn(symbol);
        try{
            return OrderCondition.valueOf(symbol);
        }catch (Exception e) {
            log.debug("enum parse failed at order condition", e);
            return OrderCondition.ranking;
        }
    }
}
