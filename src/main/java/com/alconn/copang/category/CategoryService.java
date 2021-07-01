package com.alconn.copang.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

    final CategoryRepository categoryRepository;

    final CategoryMapper categoryMapper;

//    //test
//    public CategoryView.CategoryListDto rootCategory(){
//        Map<Long, List<CategoryView.CategoryListDto>> parentGroup = categoryRepository.findAll()
//                .stream()
//                .map(category -> CategoryView.CategoryListDto.builder()
//                        .categoryId(category.getCategoryId())
//                        .categoryName(category.getCategoryName())
//                        .parentId(category.getParentId())
//                        .build()).collect(Collectors.toCollection(c->c.get));
//    }

    //부모 카테고리

    public CategoryView saveTop(CategoryForm.CategorySaveTop categorySaveTop){
            Category category=categoryMapper.topToEntity(categorySaveTop);
            category.changeCategoryprentId(0L);
            categoryRepository.save(category);

            return categoryMapper.toDto(category);
    }

    //자식 카테고리
    public CategoryView categorySave(CategoryForm.CategorySaveForm  categorySaveForm){
        Category category=categoryMapper.childToEntity(categorySaveForm);
        categoryRepository.save(category);
        return categoryMapper.toDto(category);
    }
}
