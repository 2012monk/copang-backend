package com.alconn.copang.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemDetailService {

    private final ItemDetailRepository itemDetailRepository;

    public void itemDetailSave(ItemDetail itemDetail) {
        itemDetailRepository.save(itemDetail);
    }

    public ItemDetail itemDetailFind(Long id) {
        return itemDetailRepository.findById(id).get();
    }

    public List<ItemDetail> listItemDetailFind(Long itemId) {
        return itemDetailRepository.getItemDetailByItem(itemId);
    }

    public void itemDetailDelete(Long id) {
        itemDetailRepository.deleteById(id);
    }

    public void itemDetailUpdate(Long id, ItemDetail itemDetail) {
        ItemDetail itemDetail1=itemDetailFind(id);
        itemDetail1.updateItemDetail(
                itemDetail1.getPrice(),
                itemDetail1.getStockQuantity(),
                itemDetail1.getOption(),
                itemDetail1.getDetailImg());

        itemDetailSave(itemDetail1);
    }

}
