package com.alconn.copang.item;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface ItemDetailRepository extends JpaRepository<ItemDetail, Long> {

    @Query("select itd from ItemDetail itd join fetch itd.item")
    List<ItemDetail> listItemDetailsALLFind();

    //전체 대표 상품옵션만 리턴
    @Query("select itd from ItemDetail itd join fetch itd.item where itd.itemMainApply=?1 ")
    List<ItemDetail> listItemDetailsMainFind(ItemMainApply itemMainApply);


    @Query("select itd from ItemDetail itd join fetch itd.item where itd.item.itemId in (:itemId) and itd.itemMainApply=:itemMainApply")
    List<ItemDetail> listItemDetailCategoryFind(@Param("itemId") List<Long> itemId,@Param("itemMainApply")ItemMainApply itemMainApply);

    List<ItemDetail> findItemDetailsByItem_Category_CategoryIdAndItemMainApply(@Param(value = "categoryId") Long categoryId,@Param(value = "main") ItemMainApply apply);

    List<ItemDetail> findItemDetailsByItem_Category_CategoryId(@Param(value = "categoryId") Long categoryId);


    //테스트
    @Query("select itd from ItemDetail itd join fetch itd.item where itd.item.itemId=?1 ")
    List<ItemDetail> findItemDetailPage(Long id);

//    @Query("select i from ItemDetail i join fetch i.item where i.item.category.categoryId in (select c.categoryId from Category where c.categoryId in category)")
//    List<ItemDetail> selectAll(@Param(value = "cList") List<Long> clist);

    List<ItemDetail> findItemDetailsByItem_ItemId(@Param(value = "itemId") Long itemId);
}
