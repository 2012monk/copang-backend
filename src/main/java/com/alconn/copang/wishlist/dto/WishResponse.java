package com.alconn.copang.wishlist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WishResponse {

    private Long wishId;

//    private Long clientid;

    private Long itemDetailId;

    private String ItemName;

    private int price;

    private String mainImg;

    @Override
    public String toString() {
        return "WithResponse{" +
                "wishId=" + wishId +
//                ", clientid=" + clientid +
                ", itemDetailId=" + itemDetailId +
                ", ItemName='" + ItemName + '\'' +
                ", price=" + price +
                ", mainImg='" + mainImg + '\'' +
                '}';
    }

}
