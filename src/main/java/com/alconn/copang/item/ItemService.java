package com.alconn.copang.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.EntityManager;
import java.util.NoSuchElementException;
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

    public Item itemfindById(Long id){
//        return itemRepository.findById(id).get();
        return itemRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    public void itemUpdate(Long id,Item upItem){
        Item item=itemfindById(id);
        item.returnItem(upItem.getItemName(),upItem.getMainImg(),upItem.getItemComment());
    }

}
