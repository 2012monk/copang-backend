package com.alconn.copang.item.dto;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.alconn.copang.shipment.dto.ShipmentInfoForm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDetailForm {

    private Long itemDetailId;

    private Long itemId;

    @NotBlank
    private String itemName;

    @NotEmpty
    private String itemComment;

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

    //메인화면
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MainForm {

        private Long itemId;

        private String itemName;

        private Long itemDetailId;

        private Long categoryId;

        private int price;

        private String mainImg;

        private Long sellerId;

        private Integer averageRating;

        private ShipmentInfoForm shipmentInfoForm;
    }

    //상세페이지, 저장
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetailForm {

        private Long itemDetailId;

        @NotBlank
        private int price;

        @NotBlank
        private int stockQuantity;

        @NotBlank
        private String optionName;

        @NotBlank
        private String optionValue;

        @NotBlank
        private String mainImg;


        private String subImg;

        private String sellerName;


    }

}
