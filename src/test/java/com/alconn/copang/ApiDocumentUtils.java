package com.alconn.copang;

import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

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
}
