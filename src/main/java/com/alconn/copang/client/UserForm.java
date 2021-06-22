package com.alconn.copang.client;

import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotEmpty;

@Getter
public class UserForm {

//    public UserForm(PasswordEncoder encoder) {
//    }
    private Long id;

    @NotEmpty( message = "아이디는 존재해야 합니다")
    private String username;

    @NotEmpty( message = "패스워드는 존재해야 합니다")
    private String password;

    private String mobile;

    private String realName;

    private String description;

    public String getPassword() {
        return password;
    }
}
