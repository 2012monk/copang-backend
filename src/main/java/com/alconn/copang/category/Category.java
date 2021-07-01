package com.alconn.copang.category;

import com.alconn.copang.item.Item;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id @GeneratedValue
    private Long categoryId;

    private String categoryName;

    private Long parentId;
//
//    @OneToMany(mappedBy = "categoryItem")
//    private List<Item> item;

    public void changeCategoryprentId(Long parentId){

        this.parentId=parentId;
    }



}
