package com.alconn.copang.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.EntityManager;
import java.util.List;
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

    //전체상품 조회
    public List<Item> itemFindAll(){
        return itemRepository.findAll();
    }

    public Item itemFindNum(Long id) {
        // NosuchElementException
        return itemRepository.findById(id).orElseThrow(()->new NoSuchElementException("등록된 상품 아닙니다"));
    }

    //삭제
    ///EmptyResultDataAccessException
    public boolean itemDelete(Long id){
        itemRepository.deleteById(id);
        return true;
    }
}
