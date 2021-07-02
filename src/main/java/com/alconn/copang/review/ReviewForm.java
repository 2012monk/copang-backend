package com.alconn.copang.review;

import com.alconn.copang.client.UserForm;
import com.alconn.copang.order.dto.OrderItemForm;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
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

    @JsonInclude(Include.NON_NULL)
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    public static class Response {

        private Long reviewId;

        private Long itemId;

        private Long orderId;

        private Long itemDetailId;

        private Long orderItemId;

        private String writerName;

        private String content;

        private String image;

        private int rating;

        private boolean satisfied;

        private String title;

        private String itemName;

        private String optionName;

        private String optionValue;

        private Integer amount;


        @JsonFormat(pattern = "yyyy.MM.dd", locale = "Seoul/Asia", shape = JsonFormat.Shape.STRING)
        private LocalDateTime registerDate;


    }

    @NoArgsConstructor @AllArgsConstructor
    @Builder
    @Getter
    public static class Update {
        private String content;

        private String image;

        private int rating;

        private Boolean satisfied;

        private String title;
    }
}
