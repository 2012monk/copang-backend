package com.alconn.copang.category;

import com.alconn.copang.item.Item;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//조회대상 테이블
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryItem {

    @Id @GeneratedValue
    Long cmapiId;

    @OneToMany(mappedBy = "categoryItem")
    @Builder.Default
    List<Item> item=new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;

    //편의메서드
    public void changItemCnt(Item item){
        this.item.add(item);
        item.changeCategoryItem(this);
    }

    public void changeCategoryCnt(Category category){
        this.category=category;
        category.getCategoryItems().add(this);
    }



}
