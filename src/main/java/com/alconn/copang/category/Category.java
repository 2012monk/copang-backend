package com.alconn.copang.category;

import com.alconn.copang.item.Item;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Cacheable
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

    //자식이 존재하는지 여부 Y,N
    @Builder.Default
    private String childCheck="N";

    //대중소용
    @Builder.Default
    private int layer=1;

    @OneToMany(mappedBy = "category")
    @Builder.Default
    List<Item> itemList=new ArrayList<>();

    public void changeCategoryprentId(Long parentId){
        this.parentId=parentId;
    }

    public void changeCategoryName(String categoryName){
        this.categoryName=categoryName;
    }

    public void changeChildCheck(String check){
        this.childCheck=check;
    }

    public void changeLayer(int layer){this.layer=layer;}

    @Override
    public String toString() {
        return "Category{" +
                "categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", parentId=" + parentId +
                ", childCheck='" + childCheck + '\'' +
                ", layer=" + layer +
                ", itemList=" + itemList +
                '}';
    }
}
