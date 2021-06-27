package com.alconn.copang.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.UnaryOperator;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemDetailService {

    private final ItemDetailRepository itemDetailRepository;

    private final ItemMapper itemMapper;

    private final ItemService itemService;

    //저장로직
    public List<ItemDetailForm> itemDetailListSave(List<ItemDetailForm> itemDetailFormList){
        //Domain
        List<ItemDetail> itemDetailList=itemMapper.listDtoToDomain(itemDetailFormList);
        itemDetailList=itemDetailSaveList(itemDetailList);

        //Dto로 재포장하여 전송
        itemDetailFormList=itemMapper.listDomainToDto(itemDetailList);
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

    //다중저장과 연관관계 설정
    @Transactional
    public List<ItemDetail> itemDetailSaveList(List<ItemDetail> itemDetailList){
        System.out.println("itemDetailList.get(0) = " + itemDetailList.get(0).getItem().getItemName());
        Item item=itemService.saveItem(itemDetailList.get(0).getItem());
        itemDetailList.replaceAll(new UnaryOperator<ItemDetail>() {
            @Override
            public ItemDetail apply(ItemDetail itemDetail) {
                itemDetail.itemConnect(item);
                return itemDetail;
            }
        });
        //item 저장은 한번만 이뤄지면 되므로
        return itemDetailRepository.saveAll(itemDetailList);
    }











//
//    //전체 옵션 출력
//    public ItemDetail itemDetailFind(Long id) {
//        return itemDetailRepository.findById(id).get();
//    }
//
//    //전체 리스트
//    public List<ItemDetail> listItemDetailsALLFind() {
//        return itemDetailRepository.listItemDetailsALLFind();
//    }
//
//    public List<ItemDetailForm> itemDetailFormList(){
//        List<ItemDetail> itemDetailList=itemDetailRepository.listItemDetailsALLFind();
//        List<ItemDetailForm> itemDetailForms=itemMapper.listDomainToDto(itemDetailList);
//
//
//        return itemDetailForms;
//
//    }
//
//
//    //특정 상품에 대한 리스트
//    public List<ItemDetail> listItemDetailFind(Long itemId) {
//
//        return itemDetailRepository.getItemDetailByItem(itemId);
//    }
//
//    //삭제
//    @Transactional
//    public boolean itemDetailDelete(Long id) {
//        try {
//            itemDetailRepository.deleteById(id);
//            return true;
//        } catch (EmptyResultDataAccessException e) {
//            return false;
//        }
//    }
//
//    //아이템 디테일 업데이트
////    @Transactional
////    public ItemDetail itemDetailUpdate(Long id, ItemDetail itemDetail) {
////        ItemDetail itemDetail1=itemDetailFind(id);
////        itemDetail1.updateItemDetail(
////                itemDetail1.getPrice(),
////                itemDetail1.getStockQuantity(),
////                itemDetail1.getOptionName(),
////                itemDetail1.getDetailImg());
////
////        itemDetail1= itemDetailSave(itemDetail1);
////
////        return itemDetail1;
////    }
}
