package com.alconn.copang.exceptions;

public class NoSuchEntityExceptions extends Exception{

    private static final String msg = "요청하신 리소스가 없습니다!";

    public NoSuchEntityExceptions() {
        super(msg);
    }

    public NoSuchEntityExceptions(String message) {
        super(message);
    }
}
