package com.alconn.copang.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface ItemRepository extends JpaRepository <Item,Long> {
    @Query("select i.itemId from Item i where  i.category.categoryId in (:ids)")
    List<Long> findCategoryItem(@Param("ids")List<Long> ids);

}
