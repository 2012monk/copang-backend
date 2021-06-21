package com.alconn.copang.client;

import lombok.Getter;

import javax.persistence.Embeddable;

@Getter
@Embeddable
public class Address {

    private String city;

    private String detail;

}
