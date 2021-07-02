package com.alconn.copang.categoryitem;

import com.alconn.copang.category.Category;
import com.alconn.copang.category.CategoryMapper;
import com.alconn.copang.category.CategoryRepository;
import com.alconn.copang.category.CategoryService;
import com.alconn.copang.category.dto.CategoryRequest;
import com.alconn.copang.category.dto.CategoryView;
import com.alconn.copang.exceptions.NoSuchEntityExceptions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

//카테고리 아이템 연동테스트
@SpringBootTest
@Transactional
public class CategoryServiceTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CategoryService categoryService;

    @Autowired
    EntityManager em;



    @Test
    public void updateCategoryService() throws Exception {
        //루트 카테고리는 null, 최상위 카테고리를 뜻하는 의미는 0
        CategoryRequest.CategoryUpdate categoryUpdate= CategoryRequest.CategoryUpdate.builder()
                .categoryId(testSaveData().getCategoryId())
                .categoryName("신발")
                .build();
       CategoryView categoryView= categoryService.categoryupdate(categoryUpdate);
    }

    @Test
    public void saveCategoryService() throws Exception{
        CategoryRequest.CategorySave category= CategoryRequest.CategorySave.builder()
                .parentId(0l)
                .categoryName("의류")
                .build();
            categoryService.save(category);

        CategoryRequest.CategorySave category2= CategoryRequest.CategorySave.builder()
                .parentId(categoryRepository.findAll().get(0).getCategoryId())
                .categoryName("청바지")
                .build();
            categoryService.save(category2);


    }

//    ======목데이터
    private CategoryRequest.CategorySave testData() throws NoSuchEntityExceptions {
        CategoryRequest.CategorySave test= CategoryRequest.CategorySave.builder()
                .categoryName("의류")
                  .parentId(0l)
                .build();
          return test;
    }

    private Category testSaveData() throws NoSuchEntityExceptions {
        Category test= Category.builder()
                .categoryName("의류")
                .parentId(0l)
                .build();
        categoryRepository.save(test);
        return test;
    }
//===
}
