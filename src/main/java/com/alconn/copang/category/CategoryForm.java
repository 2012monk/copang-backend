package com.alconn.copang.category;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//조회용
public class CategoryForm {

    private String categoryName;

    private Long parentId;



    //대표카테고리
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CategorySaveTop{

        @NotBlank
        private String categoryName;


    }

    //일반 카테고리
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CategorySaveForm{

        @NotBlank
        private String categoryName;

        @NotNull
        private Long parentId;
    }




    //수정용


}
