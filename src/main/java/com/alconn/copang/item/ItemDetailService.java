package com.alconn.copang.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemDetailService {

    private final ItemDetailRepository itemDetailRepository;

    private final ItemMapper itemMapper;

    private final ItemService itemService;


    @Transactional
    public ItemForm itemDetailListSave(ItemForm itemForm){
        //매퍼 풀기
        Item item=Item.builder()
                .itemName(itemForm.getItemName())
                .build();
        itemService.saveItem(item);

        List<ItemDetail> itemDetailList=itemMapper.listDtoToDomainN(itemForm.getItemDetailFormList());

        //연관관계 매핑
        itemDetailList=itemDetailSaveList(item,itemDetailList);


        //재포장
        ItemForm itemDetailFormList=itemMapper.itemDetailToDto(item,itemDetailList);

        return itemDetailFormList;
    }

    //단일저장 -- 지울지 상의 ( 모든 데이터를 리스트로 받을 지 개별로 받을지 상의 )
    @Transactional
    public ItemDetail itemDetailSave(ItemDetail itemDetail) {
        //완성된 itemDetail이냐 아니냐를 체크해야되는건가?
        itemService.saveItem(itemDetail.getItem());
        itemDetail.itemConnect(itemDetail.getItem());
        return itemDetailRepository.save(itemDetail);
    }

    //다중저장과 연관관계 설정, enum 임시저장
    private List<ItemDetail> itemDetailSaveList(Item item,List<ItemDetail> itemDetailList){
        for(ItemDetail itemDetail:itemDetailList){
            itemDetail.setItemMainApply(ItemMainApply.NON);
            itemDetail.itemConnect(item);
        }
        //enum 설정하기전에 0번을 적용하는것으로 진행할게요
        itemDetailList.get(0).setItemMainApply(ItemMainApply.APPLY);
        return itemDetailRepository.saveAll(itemDetailList);
    }

    //전체출력 ( 대표 출력 )
    public List<ItemDetailForm.MainForm> findMainList(){
        List<ItemDetail> list=itemDetailRepository.listItemDetailsMainFind(ItemMainApply.APPLY);
        List<ItemDetailForm.MainForm> listForm=itemMapper.mainPage(list);
        return listForm;
    }

    //상품 상세 페이지
    //상품 아이디를 받으면 전체 상품옵션을 돌려주는 방식
    public ItemForm findItemDetailPage(Long id){
        List<ItemDetail> list =itemDetailRepository.findItemDetailPage(id);
        ItemForm itemForm=itemMapper.itemDetailToDto(list.get(0).getItem(),list);
        return itemForm;
    }


    //삭제
    //지운 상품에 대해 정보를 돌려주는 역할을 해보도록 해보자
    @Transactional
    public ItemForm delItem(Long id){
        ItemForm itemForm=findItemDetailPage(id);
        itemService.itemDelete(id);
        return itemForm;
    }


    //옵션 개별 제거
    @Transactional
    public ItemForm delItemDetail(Long id){
        ItemDetail itemDetail=itemDetailRepository.findById(id).get();
        List<ItemDetail> itemDetailList=new ArrayList<>();
        itemDetailList.add(itemDetail);

        ItemForm itemForm=itemMapper.itemDetailToDto(itemDetail.getItem(),itemDetailList);

        itemDetailRepository.deleteById(id);
        return itemForm;
    }

    //DTO로 받아서 전체 업데이트
    @Transactional
    public ItemForm updateItemDetail(ItemForm.ItemFormUpdate itemFormUpdate){
        //풀기
        List<ItemDetailForm.DetailUpdateClass> iDetailUpdateClasses =itemFormUpdate.getItemDetailUpdateClassList();

       //item이 바뀌었는지 확인
        Item item=itemService.itemFindNum(itemFormUpdate.getItemId());

        boolean perceive=false;
        if(!item.getItemName().equals(itemFormUpdate.getItemName())){
            item.nameUpdate(itemFormUpdate.getItemName());
            perceive=true;
        }

        //상품 id에 해당하는 옵션조회
        List<ItemDetail> itemDetailList=itemDetailRepository.findItemDetailPage(itemFormUpdate.getItemId());
        System.out.println("값 바뀌는지 테스트 = " + itemDetailList.get(0).getMainImg());

        //옵션 변경
        if(itemFormUpdate.getItemDetailUpdateClassList().size()==itemDetailList.size()){
            for(int i=0;i<itemDetailList.size();i++){
                itemDetailList.get(i).updateAllData(iDetailUpdateClasses.get(i).getPrice(),
                        iDetailUpdateClasses.get(i).getStockQuantity(),
                        iDetailUpdateClasses.get(i).getOptionName(),
                        iDetailUpdateClasses.get(i).getOptionValue(),
                        iDetailUpdateClasses.get(i).getMainImg(),
                        iDetailUpdateClasses.get(i).getSubImg());

                //연관관계
                if(perceive)itemDetailList.get(i).itemConnect(item);
            }
        }
        itemDetailRepository.saveAll(itemDetailList);

        //재포장
        ItemForm itemForm=itemMapper.itemDetailToDto(item,itemDetailList);
        return itemForm;
    }


    //개별업데이트


}











