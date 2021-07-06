package com.alconn.copang.wishlist.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

//등록, 삭제
//삭제시 wishId는 null값인지 체크
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WishRequest {

    private Long wishId;

    @NotNull
    private Long itemDetailId;


    //다중삭제
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WishRequestdel {

        @NotNull
        private List<Long> wishId;
    }
}
