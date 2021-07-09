package com.alconn.copang.item;

import com.alconn.copang.category.Category;
import com.alconn.copang.client.Client;
import com.alconn.copang.item.rating.ItemRank;
import com.alconn.copang.seller.Seller;
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

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;

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

    // ==============추가부분 =================== */
    private Double averageRating;

    private String brand;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn
    private ItemRank itemRank;

    // ==============추가부분 =================== */

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
