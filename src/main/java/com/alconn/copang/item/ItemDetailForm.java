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


    //메인화면
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MainForm {

        private Long itemId;

        private String itemName;

        private Long itemDetailId;

        private int price;

        private String mainImg;

    }

    //상세페이지
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetailForm{
        private Long itemDetailId;

        private int price;

        private int stockQuantity;

        private String optionName;

        private String optionValue;

        private String mainImg;

        private String subImg;

        @Override
        public String toString() {
            return "DetailForm{" +
                    "itemDetailId=" + itemDetailId +
                    ", price=" + price +
                    ", stockQuantity=" + stockQuantity +
                    ", optionName='" + optionName + '\'' +
                    ", optionValue='" + optionValue + '\'' +
                    ", mainImg='" + mainImg + '\'' +
                    ", subImg='" + subImg + '\'' +
                    '}';
        }

    }
    public static class detailUpdate{

        @NotBlank
        private Long itemDetailId;

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
}
