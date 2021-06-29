package com.alconn.copang.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Builder
public class ResponseMessage<T> {

        public String message;

//        @JsonInclude(JsonInclude.Include.NON_NULL)
        public T data;

        public int code;


        public ResponseMessage(String message, T data, int code) {
            this.message = message;
            this.data = data;
            this.code = code;
        }

        public ResponseMessage() {
        }

        public String getMessage() {
            return message;
        }

        public T getData() {
            return data;
        }

        public int getCode() {
            return code;
        }

        public static <T> ResponseMessage<T> success(T data) {
            return ResponseMessage.<T>builder()
                .message("success")
                .code(200)
                .data(data)
                .build();
        }

        public static ResponseMessage<String> successMessage(boolean success) {
            return ResponseMessage.<String>builder()
                .message(success ? "success" : "failed")
                .code(200)
                .build();
        }
}
