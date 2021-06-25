package com.alconn.copang.client;

import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotEmpty;

@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class UserForm {

//    public UserForm(PasswordEncoder encoder) {
//    }
    @Setter
    private Long id;

    @NotEmpty( message = "아이디는 존재해야 합니다")
    private String username;

    @NotEmpty( message = "패스워드는 존재해야 합니다")
    private String password;

    private String phone;

    private String realName;

    private String description;

    @Setter
    private Role role;

    public String getPassword() {
        return password;
    }
}
