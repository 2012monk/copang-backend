package com.alconn.copang.exceptions;

public class NoSuchUserException extends Exception{

    private static final String msg = "유저정보를 불러오는데 실패했습니다!";

    public NoSuchUserException(String message) {
        super(message);
    }

    public NoSuchUserException() {
        super(msg);
    }
}
