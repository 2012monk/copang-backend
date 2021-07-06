package com.alconn.copang.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@NoArgsConstructor @AllArgsConstructor
public class ImpResponse<T> {

    private Integer code;

    private String message;

    private T response;

}
