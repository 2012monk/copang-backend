package com.alconn.copang.order.dto;

import com.alconn.copang.item.ItemDetail;
import com.alconn.copang.order.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class OrderItemForm {

    private String itemName;

    private Long itemDetailId;

    private String option;

    private Long itemId;

    private int price;

    private int amount;
}
