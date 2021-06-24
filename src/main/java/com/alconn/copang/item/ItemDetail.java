package com.alconn.copang.item;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDetail {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
//    @Column(nullable = false)
    private Item item;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int stockQuantity;

    private String option;

    private String detailImg;

    public void updateItemDetail(int price, int stockQuantity, String option, String detailImg){
        this.price=price;
        this.stockQuantity=stockQuantity;
        this.option=option;
        this.detailImg=detailImg;
    }

    public void removeStockQuantity(int quantity){
        int result=this.stockQuantity-quantity;
        if(result<0){
            result=0;
        }
        this.stockQuantity=result;
    }

    public void addStockQuantity(int quantity){
        this.stockQuantity+=quantity;
    }

    @Override
    public String toString() {
        return "ItemDetail{" +
                "id=" + id +
                ", item_id=" + item.getId() +
                ", price=" + price +
                ", stockQuantity=" + stockQuantity +
                ", option='" + option + '\'' +
                ", detailImg='" + detailImg + '\'' +
                '}';
    }
}
