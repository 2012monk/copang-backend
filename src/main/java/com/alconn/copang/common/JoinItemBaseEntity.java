package com.alconn.copang.common;

import com.alconn.copang.item.ItemDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;

@SuperBuilder
@AllArgsConstructor @NoArgsConstructor
@Getter
@MappedSuperclass
public abstract class JoinItemBaseEntity {

//    @Id @GeneratedValue
//    private Long id;

    // TODO connect 해야함
//    @ManyToOne(optional = false)
    @ManyToOne
    @JoinColumn(name = "item_id")
    protected ItemDetail item;

    protected int amount;

    protected int price;

}
