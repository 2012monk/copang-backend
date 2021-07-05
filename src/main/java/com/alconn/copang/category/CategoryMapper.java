package com.alconn.copang.category;


import com.alconn.copang.category.dto.CategoryRequest;
import com.alconn.copang.category.dto.CategoryView;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface CategoryMapper {

    CategoryView toDto(Category category);

    //부모카테고리
    Category topToEntity(CategoryRequest.CategorySave categorySave);



}
