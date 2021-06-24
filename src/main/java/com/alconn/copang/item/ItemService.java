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

    //저장
    @Transactional
    public Item saveItem(Item item){
        return itemRepository.save(item);
    }

    //삭제
    @Transactional
    public boolean deleteItem(Long id){
        try {
            itemRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    //특정 번호찾기
    public Item itemfindById(Long id){
        return itemRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }


    //아이템 업데이트
    @Transactional
    public Item itemUpdate(Long id,Item upItem){
       Item item=itemfindById(id);
       item.returnItem(upItem.getItemName(),upItem.getMainImg(),upItem.getItemComment());

       return item;
    }

}
