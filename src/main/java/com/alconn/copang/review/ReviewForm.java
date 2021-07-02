package com.alconn.copang.review;

import com.alconn.copang.client.UserForm;
import com.alconn.copang.order.dto.OrderItemForm;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ReviewForm {

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    public static class Request {

        @NotNull
        private Long orderId;

        @NotNull
        private Long itemId;

        @NotNull
        private Long itemDetailId;

        @NotNull
        private Long orderItemId;

        private String title;

        private String content;

        private String image;

        private Integer rating;

        private Boolean satisfied;

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    public static class Response {

        private Long reviewId;

        private Long itemId;

        private UserForm.Response writer;

        private OrderItemForm orderItem;

        private String content;

        private String image;

        private int rating;

        private boolean satisfied;

        private LocalDateTime registerDate;


    }
}
