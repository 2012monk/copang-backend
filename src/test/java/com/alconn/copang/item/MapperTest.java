package com.alconn.copang.item;


import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class MapperTest {

   @Autowired
   private ItemDetailService itemDetailService;

   @Autowired
   private ItemService itemService;

   @Autowired
   private ItemMapper itemMapper;

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


    @Test
    @DisplayName("매퍼 아이템 서비스 메퍼실습")
    public void subTest(){
        Item item=itemCreate();
        itemService.saveItem(item);
        ItemDetail itemDetail=itemDetailCreate2(item);
        itemDetailService.itemDetailSave(itemDetail);


        ItemDetailForm itemDetailForm= itemMapper.domainToDto(itemDetail);
        System.out.println(itemDetailForm.toString());

    }

    @Test
    @DisplayName("매퍼 아이템 전체조회 테스트")
    public void listTest(){
       Item item=itemCreate();
       itemService.saveItem(item);
       ItemDetail itemDetail=itemDetailCreate2(item);
       itemDetailService.itemDetailSave(itemDetail);

        ItemDetail itemDetail2=itemDetailCreate2(item);

        itemDetailService.itemDetailSave(itemDetail2);


        List<ItemDetail> list=itemDetailService.listItemDetailsALLFind();
        List<ItemDetailForm> listFor=itemMapper.listDomainToDto(list);
        System.out.println("listFor = " + listFor);

    }

}
