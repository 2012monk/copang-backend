package com.alconn.copang.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@SpringBootTest
@Transactional
public class ItemDetailServiceTest {

    @Autowired
    ItemDetailService itemDetailService;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemDetailRepository itemDetailRepository;

    @Autowired
    EntityManager em;
    public Item itemTest(){
        Item item=Item.builder()
                .itemName("테스트상품")
                .build();
        return item;
    }

    public ItemDetail itemDetailTest(){
        ItemDetail itemDetail=ItemDetail.builder()
                .stockQuantity(10)
                .price(10000)
                .mainImg("메인이미지")
                .optionName("색상")
                .optionValue("빨강")
                .build();
        return itemDetail;
    }

    @Test
    public void saveTest(){
        Item item=itemTest();
        itemRepository.save(item);

        ItemDetail itemDetail=itemDetailTest();
        itemDetail.itemConnect(item);
        itemDetailRepository.save(itemDetail);

        em.flush();
        em.close();
    }

    //상품 ID를 조회해서 없으면 첫번째 옵션을 대표로 적용
    public Item itemFind(Long id){
        Item item=itemRepository.findById(id).get();


        return item;
    }

}



    //
//    @Autowired
//    ItemDetailService itemDetailService;
//
//    @Autowired
//    ItemDetailRepository itemDetailRepository;
//
//    @Autowired
//    ItemService itemService;
//
//    @Autowired
//    EntityManager em;
//
//    public Item itemCreate() {
//        Item item = Item.builder()
//                .itemName("test")
//                .build();
//        return item;
//    }
//
//    public ItemDetail itemDetailCreate2(Item itemInput){
//        ItemDetail itemDetail=ItemDetail.builder()
//                .item(itemInput)
//                .price(10000)
//                .stockQuantity(111)
//                .option("옷")
//                .detailImg("사진")
//                .build();
//        return itemDetail;
//    }
//    public ItemDetail itemDetailCreate(){
//        ItemDetail itemDetail=ItemDetail.builder()
////                .item(itemCreate())
//                .price(10000)
//                .stockQuantity(111)
//                .option("가구")
//                .detailImg("가구사진")
//                .build();
//        return itemDetail;
//    }
//
//    @Test
//    public void delete(Long id){
//        boolean result=itemService.deleteItem(124124412L);
//        System.out.println("result = " + result);
//    }
//
//    @Test
//    public void save(){
//        Item item=itemCreate();
//        Item item2=itemCreate();
//        itemService.saveItem(item);
//        itemService.saveItem(item2);
//
//    }
//
//    @Test
//    public void findItemDetailByItem(){
//        Item item = itemCreate();
//        itemService.saveItem(item);
//        em.flush();
//        for(int i=0;i<3;i++){
//            try {
//                itemDetailService.itemDetailSave(itemDetailCreate2(item)) ;
//                em.flush();
//
//            } catch (Exception e) {
//                e.getStackTrace();
//            }
//        }
//
//        List<ItemDetail> itemDetails= itemDetailService.listItemDetailFind(item.getItemId());
//
//        for(ItemDetail itemDetail: itemDetails){
//            System.out.println(itemDetail.toString());
//        }
//    }
//
//    @Test
//    public void findOne(){
//        Item item=itemCreate();
//        itemService.saveItem(item);
//
//
//
//        ItemDetail itemDetail=itemDetailCreate2(item);
//        itemDetailService.itemDetailSave(itemDetail);
//
//        ItemDetail itemDetai2=itemDetailCreate2(item);
//        itemDetailService.itemDetailSave(itemDetai2);
//
//        em.flush();
//        em.close();
//
//        List<ItemDetail> itemDetail1s=itemDetailRepository.findItemDetailsByItem_ItemId(itemDetail.getItem().getItemId());
//
//        System.out.println(itemDetail1s);
//    }
//
//    @Test
//    public void updateItemDetaile(){
//        Item item=itemCreate();
//        itemService.saveItem(item);
//
//        ItemDetail itemDetail=itemDetailCreate2(item);
//        ItemDetail itemDetail2=itemDetailCreate2(item);
//        itemDetailService.itemDetailSave(itemDetail);
//        itemDetailService.itemDetailSave(itemDetail2);
//
//        itemDetail2.updateItemDetail(3000,200,"옵션","사진2");
////        em.flush();
//        itemDetailService.itemDetailUpdate(itemDetail.getItemDetailId(), itemDetail2);
//
//    }
//
//
//}
