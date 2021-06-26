package com.alconn.copang.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ItemDetailRepository extends JpaRepository<ItemDetail, Long> {

    @Query("select itd from ItemDetail itd join fetch itd.item")
    List<ItemDetail> listItemDetailsALLFind();


    @Query("select itd from ItemDetail itd where itd.item.id=?1 ")
    List<ItemDetail> getItemDetailByItem(Long id);

    List<ItemDetail> getItemDetailsByItem_Id(@Param(value = "item_id") Long itemId);

}
