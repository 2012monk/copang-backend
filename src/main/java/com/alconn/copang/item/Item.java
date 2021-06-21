package com.alconn.copang.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    @Column(nullable = false)
    private String itemName;

    @Column(nullable = false)
    private String mainImg;

    private String itemComment;

    @OneToMany(mappedBy = "item", cascade = CascadeType.REMOVE,orphanRemoval = true)
    private List<ItemDetail> itemDetails;

    public void returnItem(String itemName, String mainImg, String itemComment){
        this.itemName=itemName;
        this.mainImg=mainImg;
        this.itemComment=itemComment;
    }

}
