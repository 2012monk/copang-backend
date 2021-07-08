package com.alconn.copang.item;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@SpringBootTest
public class PageTets {

    @Autowired
    ItemRepository itemRepository;
    
    @Autowired
    ItemDetailRepository itemDetailRepository;
    
    @Autowired
    EntityManager em;
    
    @Transactional
    @Test
    public void test(){
        for(int i=0;i<5;i++) {
            Item item = Item.builder()
                    .itemComment("설명")
                    .itemName("상품")
                    .build();
            itemRepository.save(item);

            ItemDetail itemDetail = ItemDetail.builder()
                    .mainImg("메인사진")
                    .optionName("색상")
                    .optionValue("까만색")
                    .stockQuantity(i)
                    .price(1000)
                    .build();
            itemDetail.itemConnect(item);
            itemDetailRepository.save(itemDetail);
            em.flush();
            em.clear();
        }




        Pageable pageable=PageRequest.of(0,1);
//        List<ItemDetail> itemDetailList=itemDetailRepository.listItemDetailsALLFind2(pageable);
//        System.out.println("itemDetailList = " + itemDetailList.get(0).getStockQuantity());
//        Pageable pageable2=PageRequest.of(1,1);
//        List<ItemDetail> itemDetailList2=itemDetailRepository.listItemDetailsALLFind2(pageable2);
//        System.out.println("itemDetailList = " + itemDetailList2.get(0).getStockQuantity());


    }

}
