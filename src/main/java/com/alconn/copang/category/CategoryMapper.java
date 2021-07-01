package com.alconn.copang.category;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface CategoryMapper {

    //자식 카테고리
    Category childToEntity(CategoryForm.CategorySaveForm categorySaveForm);

    CategoryView toDto(Category category);

    //부모카테고리
    Category topToEntity(CategoryForm.CategorySaveTop categorySaveTop);



}
