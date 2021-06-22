package com.alconn.copang.controller;

import com.alconn.copang.auth.LoginToken;
import lombok.Getter;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ValidateController {

    @PostMapping("/validate")
    public LoginToken val(@RequestBody @Validated LoginToken token) {
        return token;
    }
}
