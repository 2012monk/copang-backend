package com.alconn.copang.order.dto;

import com.alconn.copang.item.ItemDetail;
import com.alconn.copang.order.OrderItem;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
public class OrderItemForm {

    private Long orderItemId;

    private String itemName;

    private Long itemDetailId;

    private String optionName;

    private String optionValue;

    private Long itemId;

    private Integer price;

    private Integer amount;

    private Integer unitTotal;
}
