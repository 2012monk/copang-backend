package com.alconn.copang.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

//단일
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemViewForm {
    private Long itemId;

    private String itemName;

    private String itemComment;

    private ItemDetailViewForm itemDetailViewForm;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemDetailViewForm{

        private Long itemDetailId;

        private int price;

        private int stockQuantity;

        private String optionName;

        private String optionValue;

        private String mainImg;

        private String subImg;
    }

}


