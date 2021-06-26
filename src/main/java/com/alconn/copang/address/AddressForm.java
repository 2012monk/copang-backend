package com.alconn.copang.address;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AddressForm {

    private Long addressId;

    private String city;

    private String detail;

    private String receiverName;

    private String receiverPhone;

    private String preRequest;

}
