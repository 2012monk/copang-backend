package com.alconn.copang.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ItemDetailRepository extends JpaRepository<ItemDetail, Long> {

    @Query("select itd from ItemDetail itd join fetch itd.item")
    public List<ItemDetail> listItemDetailsALLFind();


    @Query("select itd from ItemDetail itd where itd.item.id=?1 ")
    public List<ItemDetail> getItemDetailByItem(Long id);



}
