package com.alconn.copang.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.EntityManager;
import java.util.NoSuchElementException;

@SpringBootTest
@Transactional
public class ItemServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    ItemService itemService;

    public Item itemCreate() {
        Item item = Item.builder()
                .itemName("test")
                .mainImg("test")
                .itemComment("test")
                .build();
        return item;
    }

    public Item returnSave(){
        Item item=itemCreate();
        itemService.saveItem(item);
        return item;
    }


    @Test
    public void save() {
        Item item = itemCreate();
        boolean test=true;
        try {
            itemService.saveItem(item);
        } catch (DataIntegrityViolationException e) {
            test=false;
    }
        Assertions.assertTrue(test);

    }

    @Test
    public void findItem() {
        //item의 Id를 조회하여 대입한 객체가 같은지 테스트
        Item item=returnSave();
        Item item2=itemService.itemfindById(item.getId());
        Assertions.assertEquals(item,item2);

        //조회 파일 이 없을 경우 테스트
        boolean test=false;
        try{
            itemService.itemfindById(1234809L);
        }catch (NoSuchElementException e){
            test=true;
            Assertions.assertTrue(test);
        }

    }

    @Test
    public void delete() {
        //item과 동일한 item2를 생성하여 제거할경우 같이 지워지나를 테스트
        Item item=returnSave();
        itemService.deleteItem(item.getId());
        //Assertions.assertNull(itemService.itemfindById(item.getId()));
    }

    @Test
    public void update() {
        Item item=returnSave();
        Item item2=returnSave();
        itemService.itemUpdate(item.getId(),item2);
        Assertions.assertEquals(item.getItemName(),item2.getItemName());
//        Assertions.assertEquals(item.getId(),item2.getId());
    }


}

