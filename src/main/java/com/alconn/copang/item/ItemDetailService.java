package com.alconn.copang.item;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemDetailService {

    private final ItemDetailRepository itemDetailRepository;

    //저장
    @Transactional
    public ItemDetail itemDetailSave(ItemDetail itemDetail) {
        return itemDetailRepository.save(itemDetail);
    }

    //다중저장
    @Transactional
    public List<ItemDetail> itemDetailSaveList(List<ItemDetail> itemDetails){
        return itemDetailRepository.saveAll(itemDetails);
    }

    //전체 옵션 출력
    public ItemDetail itemDetailFind(Long id) {
        return itemDetailRepository.findById(id).get();
    }

    //전체 리스트
    public List<ItemDetail> listItemDetailsALLFind() {

        return itemDetailRepository.listItemDetailsALLFind();
    }


    //특정 상품에 대한 리스트
    public List<ItemDetail> listItemDetailFind(Long itemId) {

        return itemDetailRepository.getItemDetailByItem(itemId);
    }

    //삭제
    @Transactional
    public boolean itemDetailDelete(Long id) {
        try {
            itemDetailRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    //아이템 디테일 업데이트
    @Transactional
    public ItemDetail itemDetailUpdate(Long id, ItemDetail itemDetail) {
        ItemDetail itemDetail1=itemDetailFind(id);
        itemDetail1.updateItemDetail(
                itemDetail1.getPrice(),
                itemDetail1.getStockQuantity(),
                itemDetail1.getOption(),
                itemDetail1.getDetailImg());

        itemDetail1= itemDetailSave(itemDetail1);

        return itemDetail1;
    }
}
