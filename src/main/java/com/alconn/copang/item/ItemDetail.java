package com.alconn.copang.item;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemDetail {

    @Id @GeneratedValue
    private Long itemDetailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int stockQuantity;

    @Column(nullable = false)
    private String optionName;

    @Column(nullable = false)
    private String optionValue;

    @Column(nullable = false)
    private String mainImg;

    private String subImg;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ItemMainApply itemMainApply=ItemMainApply.NON;


    //ItemDetail 빌드시 후 아이템 추가하여 순환참조 끊는다
    public void itemConnect(Item item){
            this.item=item;
            item.getItemDetails().add(this);
    }

    public void setItemMainApply(ItemMainApply itemMainApply) {
        this.itemMainApply = itemMainApply;
    }

    public void updateAllData(int price,int stockQuantity, String optionName, String optionValue, String mainImg, String subImg){
        this.price= (price==0) ? this.price :price;
        this.stockQuantity=(stockQuantity==0)? this.stockQuantity:stockQuantity;
        this.optionName= (optionName==null)? this.optionName:optionName;
        this.optionValue= (optionValue==null)? this.optionValue:optionValue;
        this.mainImg= (mainImg==null)? this.mainImg:mainImg;
        this.subImg= (subImg==null)? this.subImg:subImg;
    }


}
