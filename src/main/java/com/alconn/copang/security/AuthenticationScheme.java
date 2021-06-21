package com.alconn.copang.security;

public enum AuthenticationScheme {

    BEARER("Bearer"),
    BASIC("Basic");

    String val;

    AuthenticationScheme(String val) {
        this.val = val;
    }

    public String getVal(){
        return this.val;
    }


}
