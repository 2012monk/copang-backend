package com.alconn.copang.item;


import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDetailForm {

    private Long id;

    private Item item;

    @NotBlank(message = "가격이 없습니다")
    private int price;

    @NotBlank(message = "재고수량이 없습니다")
    private int stockQuantity;

    private String option;

    private String detailImg;

    @Override
    public String toString() {
        return "ItemDetailForm{" +
                "id=" + id +
                ", item=" + item.getId() +
                ", item_time=" + item.getItemCreate() +
                ", price=" + price +
                ", stockQuantity=" + stockQuantity +
                ", option='" + option + '\'' +
                ", detailImg='" + detailImg + '\'' +
                '}';
    }
}
