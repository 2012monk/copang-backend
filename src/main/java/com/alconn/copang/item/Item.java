package com.alconn.copang.item;

import com.alconn.copang.category.Category;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item {

    @Id @GeneratedValue
    private Long itemId;

    @Column(nullable = false)
    private String itemName;

    private String itemComment;

    @CreationTimestamp
    @Column(name = "item_time",updatable = false)
    private LocalDate itemCreate;

    //상품 제거 시 상품옵션 삭제 ( 연관관계 필요 )
    @OneToMany(mappedBy = "item", cascade = CascadeType.REMOVE,orphanRemoval = true)
    @Builder.Default
    private List<ItemDetail> itemDetails=new ArrayList<>();

    //카테고리
    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;

    //=====
    public void changeCategory(Category category){
        this.category=category;
        category.getItemList().add(this);
    }
    //=====

    //=====
    //수정
    public void updateMethod(String itemName, String itemComment){
        this.itemName=(itemName==null? this.itemName:itemName);
        this.itemComment=(itemComment==null? this.itemComment:itemComment);
    }
    //=====


}
