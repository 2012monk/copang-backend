package com.alconn.copang.inquiry;


import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class InquiryForm {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class Request {

        private String content;

        private Long itemDetailId;

        private Long itemId;


    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class Response {

        private Long inquiryId;

        private Long clientId;

        private String clientName;

        private Long itemDetailId;

        private Long itemId;

        private String content;

        private LocalDateTime registerDate;

        private ReplyForm reply;

        private String itemName;

        private String optionName;

        private String optionValue;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class ReplyForm {

        private Long replyId;

        private String sellerName;

        private String content;

        private Long sellerId;

        @JsonFormat(timezone = "Seoul/Asia", pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime registerDate;

        private Long sellerCode;

    }


}
