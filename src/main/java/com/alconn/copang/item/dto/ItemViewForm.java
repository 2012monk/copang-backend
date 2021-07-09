package com.alconn.copang.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

//단일
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemViewForm {
    private Long itemId;

    private String itemName;

    private String itemComment;

    //=====
    private Long categoryId;
    //=====

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

    //횟수 출력
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MainViewForm{
        private int totalCount;

        private List<ItemDetailForm.MainForm> list;
    }


}


