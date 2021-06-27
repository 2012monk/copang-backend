package com.alconn.copang.order.dto;

import com.alconn.copang.item.ItemDetail;
import com.alconn.copang.order.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
public class OrderItemForm {

    private String itemName;

    private Long itemDetailId;

    private String optionName;

    private String optionValue;

    private Long itemId;

    private int price;

    private int amount;

    private int unitTotal;
}
