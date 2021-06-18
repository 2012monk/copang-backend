package com.alconn.copang.item;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.EntityManager;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item){
        itemRepository.save(item);
    }

    public void deleteItem(Long id){
        itemRepository.deleteById(id);
    }

    //ID를 Optional<Iteam>으로 반환
    public Optional<Item> itemfindById(Long id){
        return itemRepository.findById(id);
    }

    public void itemUpdate(Optional<Item> item){
        itemRepository.save(item.get());
    }

}
