package com.alconn.copang.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface ItemRepository extends JpaRepository <Item,Long> {

}
