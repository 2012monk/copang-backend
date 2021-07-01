package com.alconn.copang.category;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryView {

    private Long categoryId;

    private String categoryName;

    private Long parentId;


    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CategoryListDto{

        private Long categoryId;

        private String categoryName;

        private Long parentId;

        private List<CategoryListDto> subCategory=new ArrayList<>();


        public void changeSubCategory(List<CategoryListDto> categoryListDtos){
            this.subCategory=categoryListDtos;
        }

    }

}
