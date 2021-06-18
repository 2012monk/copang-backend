package com.alconn.copang.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemDetailService {
    private final ItemDetailRepository itemDetailRepository;

    public void itDsave(ItemDetail itemDetail){
        itemDetailRepository.save(itemDetail);
    }

    public ItemDetail itDfind(Long id){
        return itemDetailRepository.findById(id).get();
    }

    public void itDdelete(Long id){
        itemDetailRepository.deleteById(id);
    }

    public void itDupdate(ItemDetail itemDetail){
        itemDetailRepository.save(itemDetail);
    }
}
