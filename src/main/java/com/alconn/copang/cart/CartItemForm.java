package com.alconn.copang.cart;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class CartItemForm {

    private Long cartId;

    private Long itemDetailId;

    private Long itemId;

    @NotBlank
    private String itemName;

    @Positive
    private Integer price;

    @Positive
    private Integer amount;

    @NotBlank
    private String optionName;

    @NotBlank
    private String optionValue;

    @NotBlank
    private String mainImg;

    private String subImg;

    private Integer unitTotal;
}
