package com.alconn.copang.item;

import com.alconn.copang.exceptions.NoSuchUserException;
import com.alconn.copang.item.Item;
import com.alconn.copang.item.ItemService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.NoSuchElementException;
import java.util.Optional;

@Disabled
@SpringBootTest
@Transactional
public class ItemServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    ItemService itemService;

    @Test
    public void save(){
        Item item=Item.builder()
                .itemName("test")
                .itemComment("test")
                .mainImg("test")
                .build();
        try {
            itemService.saveItem(item);
        }catch (DataIntegrityViolationException e) {
            System.out.println("saveitem에러" + e.getMessage());
            /* 뭔가 작동을 한다!/*/
        }
    }

//
//    @Test
//    public void findItem(){
//        save();
//        Long id=2L;
//        Item item=itemService.itemfindById(id).orElseThrow(()-> new NoSuchElementException("findItem에러"));
//        System.out.println(item.getItemName());
//    }
//
//    @Test
//    @Commit
//    public void delete(){
////        save();
//        Long id=20L;
//        Optional<Item> item=itemService.itemfindById(id);
////        em.flush();
////        em.clear();
//        itemService.deleteItem(item.get().getId());
////        em.flush();
////        em.clear();
//    }
//
//    @Test
//    public void update(){
//        save();
//        Long id=9L;
//        Optional<Item> item=itemService.itemfindById(id);
//        em.flush();
//        Item item2 =item.get();
//        item2.setItemName("test2");
//        Optional<Item> item3=Optional.of(item2);
//        itemService.itemUpdate(item3);
//        em.flush();
//        em.clear();
//    }
}
