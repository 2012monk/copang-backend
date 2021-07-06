package com.alconn.copang.client;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import lombok.*;
import lombok.experimental.SuperBuilder;
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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Getter
    @SuperBuilder
    @AllArgsConstructor @NoArgsConstructor
    public static class Response {

        private Long clientId;

        private String username;

        private String phone;

        private String realName;

        private Role role;

        @JsonFormat(pattern = "yyyy.MM.dd", locale = "Seoul/Asia", shape = JsonFormat.Shape.STRING)
        private LocalDateTime signInDate;
    }


    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Getter
    @SuperBuilder
    @AllArgsConstructor @NoArgsConstructor
    public static class SellerResponse extends Response{

        private Long sellerCode;

        private String sellerName;


    }
}
