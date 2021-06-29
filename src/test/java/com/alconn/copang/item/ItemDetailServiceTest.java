package com.alconn.copang.item;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

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
    ItemMapper itemMapper;

    @Autowired
    EntityManager em;


    private Item itemTest(){
        Item item=Item.builder()
                .itemName("테스트상품")
                .build();
        return item;
    }

    private ItemDetail itemDetailTest(){
        ItemDetail itemDetail=ItemDetail.builder()
                .stockQuantity(10)
                .price(10000)
                .mainImg("메인이미지")
                .optionName("색상")
                .optionValue("빨강")
                .build();
        return itemDetail;
    }

    //enum이 적용된 예제데이터
    public List<ItemDetail> findMockData(){
        Item item=itemTest();
        itemRepository.save(item);

        ItemDetail itemDetail=itemDetailTest();
        ItemDetail itemDetail2=itemDetailTest();

        List<ItemDetail> itemDetailList=new ArrayList<>();
//        itemDetail.itemConnect(item);
//        itemDetail2.itemConnect(item);

        itemDetailList.add(itemDetail);
        itemDetailList.add(itemDetail2);

        for(ItemDetail itemDetail1:itemDetailList){
            itemDetail.setItemMainApply(ItemMainApply.NON);
            itemDetail.itemConnect(item);
            //enum 설정하기전에 0번을 적용하는것으로 진행할게요

        }
        itemDetailList.get(0).setItemMainApply(ItemMainApply.APPLY);
        itemDetailRepository.saveAll(itemDetailList);
        em.flush();
        em.clear();

        return itemDetailList;
    }


    @DisplayName("대표 이미지 출력용")
    @Test
    public void findMainTest(){
        findMockData();
        findMockData();
        em.flush();
        em.clear();
        List<ItemDetail> list=itemDetailRepository.listItemDetailsMainFind(ItemMainApply.APPLY);
        List<ItemDetailForm> list2= itemMapper.listDomainToDto(list);
        for(ItemDetailForm itemDetailForm:list2){
            System.out.println("itemDetail = " + itemDetailForm.toString());
        }
    }


    @DisplayName("예제 데이터")
    @Test
    public void saveTest(){
        Item item=itemTest();
        itemRepository.save(item);

        ItemDetail itemDetail=itemDetailTest();
        itemDetail.itemConnect(item);
        itemDetailRepository.save(itemDetail);

        em.flush();
        em.clear();
    }

    //mockup데이터 디비 확인용
    @Test
    @Commit
    public void mockDatas(){
        findMockData();
        findMockData();
        em.flush();
        em.clear();
    }

    @Test
    public void findItemTest(){
        List<ItemDetail> list=findMockData();
        List<ItemDetail> list2=findMockData();

        itemDetailRepository.findItemDetailPage(list.get(0).getItem().getItemId());
        itemDetailRepository.findItemDetailPage(list2.get(0).getItem().getItemId());

    }

    @Test
    public void delTest(){
        List<ItemDetail> list=findMockData();
        List<ItemDetailForm> list2=itemMapper.listDomainToDto(list);
        em.flush();
        Long id=list.get(0).getItem().getItemId();
        System.out.println("id = " + id);
        itemRepository.deleteById(id);
        System.out.println("list2 = " + list2);
    }

    @Test
    public void updateTest(){
        List<ItemDetail> list=findMockData();
        List<ItemDetail> testList=itemDetailRepository.findItemDetailPage(list.get(0).getItem().getItemId());
        List<ItemDetailForm.DetailUpdateClass> testUpdateList =new ArrayList<>();

        for(ItemDetail itemDetail:testList){
            testUpdateList.add(
                ItemDetailForm.DetailUpdateClass.builder()
                        .itemDetailId(itemDetail.getItemDetailId())
                        .price(20000)
                        .stockQuantity(30)
                        .optionName("수정")
                        .optionValue("수정테스트")
                        .mainImg("수정사진")
                        .subImg("수정이미지")
                        .build()
            );

        }

        ItemForm.ItemFormUpdate itemFormUpdate= ItemForm.ItemFormUpdate.builder()
                .itemId(testList.get(0).getItem().getItemId())
                .itemName(testList.get(0).getItem().getItemName())
                .itemDetailUpdateClassList(testUpdateList)
                .build();

        ItemForm itemForm=itemDetailService.updateItemDetail(itemFormUpdate);

        em.flush();
        em.clear();
    }

}
