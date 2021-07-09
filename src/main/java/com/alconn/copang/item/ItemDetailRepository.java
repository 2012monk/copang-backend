package com.alconn.copang.item;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface ItemDetailRepository extends JpaRepository<ItemDetail, Long> {

    @Query("select itd from ItemDetail itd join fetch itd.item")
    List<ItemDetail> listItemDetailsALLFind();

    int countItemDetailByItemMainApply(ItemMainApply itemMainApply);

    //전체 대표 상품옵션만 리턴
    @Query(value = "select itd from ItemDetail itd join fetch itd.item where itd.itemMainApply=?1",
            countQuery = "select count(itd) from ItemDetail itd join fetch itd.item where itd.itemMainApply=?1")
    List<ItemDetail> listItemDetailsMainFind(ItemMainApply itemMainApply,Pageable pageable);


    @Query("select itd from ItemDetail itd join fetch itd.item where itd.item.itemId in (:itemId) and itd.itemMainApply=:itemMainApply")
    List<ItemDetail> listItemDetailCategoryFind(@Param("itemId") List<Long> itemId,@Param("itemMainApply")ItemMainApply itemMainApply);



    //테스트
    @Query("select itd from ItemDetail itd join fetch itd.item  where itd.item.itemId=?1 ")
    List<ItemDetail> findItemDetailPage(Long id);

    //테스트
//    @Query(value = "select itd from ItemDetail itd join fetch itd.item where itd.itemMainApply=?1" , countQuery = "select itd from ItemDetail itd join fetch itd.item where itd.itemMainApply=?1 ")
//    List<ItemDetail> listItemDetailsMainFind2(ItemMainApply itemMainApply, Pageable pageable);


    //테스트
//    @EntityGraph(value = "ItemDetail", type = EntityGraph.EntityGraphType.LOAD)
//    List<ItemDetail> findItemDetailsByItem_ItemId(Long id);


}
