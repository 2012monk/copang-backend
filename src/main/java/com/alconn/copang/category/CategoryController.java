package com.alconn.copang.category;

import com.alconn.copang.category.dto.CategoryRequest;
import com.alconn.copang.category.dto.CategoryView;
import com.alconn.copang.common.ResponseMessage;
import com.alconn.copang.exceptions.NoSuchEntityExceptions;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/category")
public class CategoryController {

    final CategoryService categoryService;


//    페이지 조회용
//    @Cacheable("main-category")
    @GetMapping("/main")
    public ResponseMessage<CategoryView.CategoryListDto> layerList(){
        return ResponseMessage.<CategoryView.CategoryListDto>builder()
                .message("대중소분류카테고리")
                .data(categoryService.layerCategory())
                .build();
    }

//    @Cacheable("list-category")
    //상품 등록용 카테고리 전체조회
    @GetMapping("/list")
    public ResponseMessage<CategoryView.CategoryListDto> list(){

        return ResponseMessage.<CategoryView.CategoryListDto>builder()
                .message("전체카테고리리스트")
                .data(categoryService.rootCategory(0l))
                .build();
    }

    //카테고리 제거
//    @CacheEvict(value = {"list-category", "main-category"})
    @DeleteMapping("/delete/{categoryId}")
    public ResponseMessage<CategoryView> del(@PathVariable(name = "categoryId") Long id) throws NoSuchEntityExceptions {
        //id값이 없을경우 에러확인

        CategoryView categoryView=categoryService.categorydelete(id);
        if (categoryView==null){
            return ResponseMessage.<CategoryView>builder()
                            .message("하위 카테고리를 먼저 제거해주세요")
                            .build();
        }
        else
            return ResponseMessage.<CategoryView>builder()
                    .message("카테고리삭제")
                    .data(categoryView)
                    .build();
    }


    //수정
//    @CacheEvict(value = {"list-category", "main-category"})
    @PutMapping("/update")
    public ResponseMessage<CategoryView> update(@RequestBody CategoryRequest.CategoryUpdate categoryUpdate) throws  NoSuchEntityExceptions{

        return ResponseMessage.<CategoryView>builder()
                .message("카테고리 수정")
                .data(categoryService.categoryupdate(categoryUpdate))
                .build();
    }




    //카테고리 등록
//    @CacheEvict(value = {"list-category", "main-category"})
    @PostMapping("/add")
    public ResponseMessage<CategoryView> save(@RequestBody CategoryRequest.CategorySave categorySave) throws NoSuchEntityExceptions {
            return ResponseMessage.<CategoryView>builder()
                .message("메인카테고리저장")
                .data(categoryService.save(categorySave))
                .build();
    }


}
