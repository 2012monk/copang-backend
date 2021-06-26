package com.alconn.copang.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
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

    @CreationTimestamp
    @Column(name = "item_time",updatable = false)
    private LocalDate itemCreate;

    @Builder.Default
    @OneToMany(mappedBy = "item", cascade = CascadeType.REMOVE,orphanRemoval = true)
    private List<ItemDetail> itemDetails = new ArrayList<>();

    public void returnItem(String itemName, String mainImg, String itemComment){
        this.itemName=itemName;
        this.mainImg=mainImg;
        this.itemComment=itemComment;
    }

    public void addItemDeails(ItemDetail itemDetail) {
        this.itemDetails.add(itemDetail);
    }
}
