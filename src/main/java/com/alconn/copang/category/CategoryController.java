package com.alconn.copang.category;

import com.alconn.copang.common.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/category")
public class CategoryController {

    final CategoryService categoryService;






    //부모 카테고리
    @PostMapping("/top")
    public ResponseMessage<CategoryView> parentSave(@RequestBody CategoryForm.CategorySaveTop categorySaveTop){
            return ResponseMessage.<CategoryView>builder()
                .message("메인카테고리저장")
                .data(categoryService.saveTop(categorySaveTop))
                .build();
    }

    //자식카테고리
    @PostMapping("/bottom")
    public ResponseMessage<CategoryView> childSave(@RequestBody CategoryForm.CategorySaveForm categorySaveForm){
            return ResponseMessage.<CategoryView>builder()
                    .message("서브카테고리저장")
                    .data(categoryService.categorySave(categorySaveForm))
                    .build();
    }









}
