package com.alconn.copang.item;


import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDetailForm {

    private Long itemDetailId;

    private Long itemId;

    @NotBlank
    private String itemName;

    @NotBlank
    private int price;

    @NotBlank
    private int stockQuantity;

    @NotNull
    private String optionName;

    @NotNull
    private String optionValue;

    @NotBlank
    private String mainImg;

    private String subImg;



}
