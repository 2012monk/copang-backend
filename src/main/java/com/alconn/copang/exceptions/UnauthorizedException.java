package com.alconn.copang.exceptions;

public class UnauthorizedException extends Exception {

    private int code = -403;

    private String msg = "권한이 없습니다!";

    public int getCode() {

        return code;
    }

    public UnauthorizedException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public UnauthorizedException() {
        super("권한이 없습니다!");
    }
}
