package com.alconn.copang.exceptions;

public class InvalidTokenException extends Exception {

    private static final String msg = "토큰 정보가 유효하지 않습니다! invalid token";

    public InvalidTokenException() {
        super(msg);
    }

    public InvalidTokenException(String message) {
        super(message);
    }
}
