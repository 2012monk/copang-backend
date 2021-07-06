package com.alconn.copang.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor @AllArgsConstructor
@Builder
@Getter
public class ImpTokenContainer {

    private String access_token;

    private Integer expired_at;

    private Integer now;

}
