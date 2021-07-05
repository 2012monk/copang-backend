package com.alconn.copang.category.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//조회용, 삭제
public class CategoryRequest {

    private Long categoryId;

    //저장
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CategorySave{

        @NotBlank
        private String categoryName;

        @NotNull
        private Long parentId;

    }

   //수정 -> 이름, 위치수정까지 가능
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static  class CategoryUpdate{

        @NotNull
        private Long categoryId;

        @NotBlank
        private String categoryName;

        private Long parentId;
   }
}
