package com.alconn.copang.payment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class ImpPayResponse extends ImpResponse<PaymentInfoFrom> {

}
