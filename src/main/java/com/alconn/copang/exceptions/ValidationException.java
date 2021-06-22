package com.alconn.copang.exceptions;

public class ValidationException extends Exception {
    private String[] messages = new String[4];

    public ValidationException(String... messages) {
        super(String.join("\n", messages));
        this.messages = messages;
    }

    public ValidationException() {
        super("요청하신 정보가 잘못 되었습니다");
    }


}
