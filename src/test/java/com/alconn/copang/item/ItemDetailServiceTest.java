package com.alconn.copang.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@SpringBootTest
@Transactional
public class ItemDetailServiceTest {

    @Autowired
    ItemDetailService itemDetailService;

    @MockBean
    ItemService itemService;

    public Item itemCreate() {
        Item item = Item.builder()
                .itemName("test")
                .mainImg("test2")
                .itemComment("test")
                .build();
        return item;
    }

    public ItemDetail itemDetailCreate2(Item itemInput){
        ItemDetail itemDetail=ItemDetail.builder()
                .item(itemInput)
                .price(10000)
                .stockQuantity(111)
                .option("옷")
                .detailImg("사진")
                .build();
        return itemDetail;
    }
    public ItemDetail itemDetailCreate(){
        ItemDetail itemDetail=ItemDetail.builder()
//                .item(itemCreate())
                .price(10000)
                .stockQuantity(111)
                .option("가구")
                .detailImg("가구사진")
                .build();
        return itemDetail;
    }

    @Test
    public void delete(){
        boolean result=itemService.deleteItem(124124412L);
        System.out.println("result = " + result);
    }

    @Test
    public void save(){
        Item item=itemCreate();
        itemService.saveItem(item);
        itemDetailService.itemDetailSave(itemDetailCreate2(item));
    }

    @Test
    public void findItemDetailByItem(){
        System.out.println("????????");
        Item item= null;
        try {
            item = itemCreate();
        itemService.saveItem(item);
        } catch (Exception e) {
            System.out.println("안됨");
        }
        for(int i=0;i<3;i++){

            try {
                itemDetailService.itemDetailSave(itemDetailCreate2(item));
            } catch (Exception e) {
                System.out.println("안됨2");
            }
        }

        List<ItemDetail> itemDetails= itemDetailService.listItemDetailFind(item.getId());

        for(ItemDetail itemDetail: itemDetails){
            System.out.println(itemDetail.toString());
        }
    }

    @Test
    public void updateItemDetaile(){
        Item item=itemCreate();
        itemService.saveItem(item);

        ItemDetail itemDetail=itemDetailCreate2(item);
        ItemDetail itemDetail2=itemDetailCreate2(item);
        itemDetailService.itemDetailSave(itemDetail);
        itemDetailService.itemDetailSave(itemDetail2);

        itemDetail2.updateItemDetail(3000,200,"옵션","사진2");
//        em.flush();
        itemDetailService.itemDetailUpdate(itemDetail.getId(), itemDetail2);

    }

}
