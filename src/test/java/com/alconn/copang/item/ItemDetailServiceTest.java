package com.alconn.copang.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@SpringBootTest
@Transactional
public class ItemDetailServiceTest {

    @Autowired
    ItemDetailService itemDetailService;

    @Autowired
    ItemService itemService;

    @Autowired
    EntityManager em;



    public Item teamsave(){
        Item item=new Item();
        item.setItemName("test");
        item.setMainImg("test");
        item.setItemComment("test");
        itemService.saveItem(item);
        return item;
    }

    @Test
    @Commit
    public void itdSave(){
        Item item=teamsave();
        ItemDetail itemDetail= ItemDetail.builder()
                .detailImg("detailtest")
                .option("detailtest")
                .stockQuantity(1)
                .price(10000)
                .item(item)
                .build();
        ItemDetail itemDetail2= ItemDetail.builder()
                .detailImg("detailtest")
                .option("detailtest")
                .stockQuantity(1)
                .price(10000)
                .item(item)
                .build();
        itemDetailService.itDsave(itemDetail);
        itemDetailService.itDsave(itemDetail2);
        em.flush();
        em.close();
    }

    @Test
    @Transactional
    @Commit
    public void itddelete(){
        Long id=18L;
        ItemDetail itemDetail=itemDetailService.itDfind(id);
        itemDetailService.itDdelete(itemDetail.getId());
        em.flush();
        em.close();
    }

}
