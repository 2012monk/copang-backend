package com.alconn.copang.categoryitem;

import com.alconn.copang.category.Category;
import com.alconn.copang.category.CategoryItem;
import com.alconn.copang.category.CategoryItemRepository;
import com.alconn.copang.category.CategoryRepository;
import com.alconn.copang.item.Item;
import com.alconn.copang.item.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

//카테고리 아이템 연동테스트
@SpringBootTest
@Transactional
public class CategoryServiceTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    CategoryItemRepository categoryItemRepository;

    @Autowired
    EntityManager em;

    //=============




//    ==============


    @Test
    public void saveTest(){
        Category category=Category.builder()
                .categoryName("의류")
                .parentId(0L)
                .build();
        //카테고리 생성
        categoryRepository.save(category);

        Item item=Item.builder()
                .itemName("양말")
                .build();
        itemRepository.save(item);

        CategoryItem categoryItem=CategoryItem.builder()
                .category(category)
                .build();
        categoryItem.changeCategoryCnt(category);
        categoryItem.changItemCnt(item);
        categoryItemRepository.save(categoryItem);

        em.flush();
        em.clear();
    }



}
