package com.alconn.copang.inquiry;


import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

public class InquiryForm {

    public static class Request {


    }

    @Builder
    @NoArgsConstructor @AllArgsConstructor
    
    public static class Response {

        private Long inquiryId;

        private Long clientId;

        private String clientName;

        private LocalDateTime registerDate;

        private InquiryForm.Reply reply;
    }

    public static class Reply {

        private String sellerName;

        private String content;

        private Long sellerId;

        private LocalDateTime registerDate;

    }


}
