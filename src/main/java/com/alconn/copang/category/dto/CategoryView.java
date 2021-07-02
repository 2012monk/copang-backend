package com.alconn.copang.category.dto;

import lombok.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

//등록, 수정, 삭제 뷰
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryView {

    private Long categoryId;

    private String categoryName;

    private Long parentId;


    //조회용
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CategoryListDto{

        private Long categoryId;

        private String categoryName;

        private Long parentId;

        private List<CategoryListDto> cildCategory =new ArrayList<>();


        public void changeSubCategory(List<CategoryListDto> categoryListDtos){
            this.cildCategory =categoryListDtos;
        }


    }

}
