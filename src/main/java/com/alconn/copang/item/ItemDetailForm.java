package com.alconn.copang.item;


import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

//엔티티 1:1 매핑시 사용
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

        @Override
        public String toString() {
            return "MainForm{" +
                    "itemId=" + itemId +
                    ", itemName='" + itemName + '\'' +
                    ", itemDetailId=" + itemDetailId +
                    ", categoryId=" + categoryId +
                    ", price=" + price +
                    ", mainImg='" + mainImg + '\'' +
                    '}';
        }
    }

    //상세페이지, 저장
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetailForm{

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

    }
    //업데이트
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DetailUpdateClass {

        @NotNull
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
