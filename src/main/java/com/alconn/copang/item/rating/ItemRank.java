package com.alconn.copang.item.rating;

import com.alconn.copang.item.Item;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class ItemRank {

    @Id @GeneratedValue
    private Long itemRankId;

    @OneToOne
    private Item item;

}
