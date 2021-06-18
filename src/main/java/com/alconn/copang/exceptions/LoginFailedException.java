package com.alconn.copang.exceptions;

public class LoginFailedException extends Exception {

    private static final String msg = "Login failed";

    private int code;

    public LoginFailedException(String message) {
        super(message);
    }

    public LoginFailedException() {
        super(msg);
    }

    public int getCode() {
        return 0;
    }
}
