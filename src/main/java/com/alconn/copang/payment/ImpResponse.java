package com.alconn.copang.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor @AllArgsConstructor
public class ImpResponse<T> {

    private Integer code;

    private String message;

    private T response;

}
