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

    //특정번호 상품 조회 -> 상품명만 조회되는데
    public Item itemFindNum(Long id){
        //Exception은 상위계층에서 잡는다 음 핸들링할까..
        //일단 미루자 NosuchElementException
        return itemRepository.findById(id).get();
    }

    //삭제
    ///EmptyResultDataAccessException
    public boolean itemDelete(Long id){
        itemRepository.deleteById(id);
        return true;
    }


//    //삭제
//    @Transactional
//    public boolean deleteItem(Long id){
//        try {
//            itemRepository.deleteById(id);
//            return true;
//        } catch (EmptyResultDataAccessException e) {
//            return false;
//        }
//    }
//
//    //아이템 업데이트
//    @Transactional
//    public Item itemUpdate(Long id,Item upItem){
//       Item item=itemfindById(id);
//       item.returnItem(upItem.getItemName());
//
//       return item;
//    }
//
}
