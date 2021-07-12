package com.alconn.copang.order.dto;

import com.alconn.copang.item.ItemDetail;
import com.alconn.copang.order.OrderItem;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import javax.validation.constraints.NotNull;
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

//    @NotNull(message = "상품아이디는 필수입니다")
    private Long itemId;

    @NotNull(message = "옵션 아이디는 필수값입니다")
    private Long itemDetailId;

    @NotNull(message = "수량은 필수입니다")
    private Integer amount;

    private String mainImg;

    private String itemName;

    private String optionName;

    private String optionValue;

    private Integer price;

    private Integer unitTotal;

    private String trackingNumber;
}
