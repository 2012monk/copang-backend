package com.alconn.copang.categoryitem;

import com.alconn.copang.category.*;
import com.alconn.copang.category.dto.CategoryRequest;
import com.alconn.copang.category.dto.CategoryView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.*;

import static org.mockito.BDDMockito.given;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;


@SpringBootTest
 public class CategoryFindTest {

//    @InjectMocks
    @Autowired
    private CategoryService categoryService;

//    @Mock
    @Autowired
    private CategoryRepository categoryRepository;


    //rootCategory 메서드는 Id 특정 위치에서 찾을경우 사용 -> 전체 조회는 0L
    @Test
    public void  list() throws Exception{
        List<Category> categoryList=categortyadd();

        //when
        CategoryView.CategoryListDto categoryListDto=categoryService.rootCategory(categoryList.get(0).getParentId());
        System.out.println("categoryListDto.toString() = " + categoryListDto.toString());
    }

    private List<Category> categortyadd() {
        Category test1=Category.builder()
                        .categoryName("메인1")
                        .parentId(0l)
                        .build();
        Category test2=Category.builder()
                .categoryName("메인2")
                .parentId(0l)
                .build();

        Category test11=Category.builder()
                .categoryName("메인1-1")
                .parentId(1l)
                .build();

        Category test12=Category.builder()
                .categoryName("메인1-2")
                .parentId(1l)
                .build();

        Category test7=Category.builder()
                .categoryName("7-2")
                .parentId(3l)
                .build();


        Category test8=Category.builder()
                .categoryName("8-2")
                .parentId(7l)
                .build();


        Category test9=Category.builder()
                .categoryName("9-2")
                .parentId(8l)
                .build();


        Category test10=Category.builder()
                .categoryName("10-2")
                .parentId(9l)
                .build();


        Category test21=Category.builder()
                .categoryName("메인2-1")
                .parentId(2l)
                .build();


        Category test22=Category.builder()
                .categoryName("메인2-2")
                .parentId(2l)
                .build();

        List<Category> categoryList= new ArrayList<>();
        categoryList.add(test1);
        categoryList.add(test2);
        categoryList.add(test12);
        categoryList.add(test22);
        categoryList.add(test11);
        categoryList.add(test21);
        categoryList.add(test7);
        categoryList.add(test8);
        categoryList.add(test9);
        categoryList.add(test10);

        categoryRepository.saveAll(categoryList);


        return categoryList;
    }


}
