package com.alconn.copang.inquiry;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDateTime;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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

        @NotEmpty(message = "내용은 비어있으면 안됩니다")
        private String content;

        @NotNull(message = "문의하실 상품 옵션 아이디는 존재해야합니다")
        private Long itemDetailId;

        private Long itemId;

    }

    @JsonInclude(Include.NON_NULL)
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

        @JsonFormat(timezone = "Seoul/Asia", pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime registerDate;

        private ReplyForm reply;

        private String itemName;

        private String optionName;

        private String optionValue;
    }

    @JsonInclude(Include.NON_NULL)
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
