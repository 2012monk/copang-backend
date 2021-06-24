package com.alconn.copang.auth;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Getter
public class LoginToken {

    @NotEmpty(message = "아이디는 필수입니다")
    private String username;

    @NotEmpty(message = "패스워드는 필수 입력값입니다")
    private String password;
}
