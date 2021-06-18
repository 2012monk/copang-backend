package com.alconn.copang.exceptions;

public class UnauthorizedException extends Throwable {

    private int code;

    public int getCode() {
        return code;
    }
}
