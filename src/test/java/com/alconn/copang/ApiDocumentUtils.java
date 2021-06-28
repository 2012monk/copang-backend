package com.alconn.copang;

import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;
import org.springframework.restdocs.snippet.Snippet;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.attributes;
import static org.springframework.restdocs.snippet.Attributes.key;

public interface ApiDocumentUtils {

    static OperationRequestPreprocessor getDocumentRequest() {
        return preprocessRequest(
                modifyUris()
                .scheme("https")
                .host("alconn.co")
                .removePort(),
                prettyPrint()
        );
    }

    static OperationResponsePreprocessor getDocumentResponse() {
        return preprocessResponse(
                prettyPrint(),
                removeMatchingHeaders("Origin",
                        "Access-Control-Request-Method",
                        "Access-Control-Request-Headers",
                        "X-Content-Type-Options",
                        "X-XSS-Protection",
                        "Cache-Control",
                        "Pragma",
                        "Expires",
                        "X-Frame-Options",
                        "Vary"
                        )
        );
    }

    static Snippet commonFields(String dataType) {
        return relaxedResponseFields(
//                beneathPath("common").withSubsectionId("data"),
//                attributes(key("title").value("공통응답")),
                fieldWithPath("data").description(dataType),
//                subsectionWithPath("data").description(dataType),
                fieldWithPath("message").description("결과 메세지"),
                fieldWithPath("code").description("결과 코드")
        );
    }

//    static Snippet common(String dataType) {
//        return s -> {
//            fieldWithPath("data").description(dataType),
////                subsectionWithPath("data").description(dataType),
//                    fieldWithPath("message").description("결과 메세지"),
//                    fieldWithPath("code").description("결과 코드")
//        };
//    }

    static Snippet commonFields(){
        return commonFields("응답 데이터");
    }
}
