package com.alconn.copang.auth;

import com.alconn.copang.client.Client;
import com.alconn.copang.client.ClientService;
import com.alconn.copang.client.UserForm;
import com.alconn.copang.common.ResponseMessage;
import com.alconn.copang.exceptions.InvalidTokenException;
import com.alconn.copang.exceptions.LoginFailedException;
import com.alconn.copang.exceptions.NoSuchUserException;
import java.util.Date;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final ClientService service;

    @PostMapping("/token")
    public ResponseMessage<AccessTokenContainer> refreshAccessToken(@CookieValue(name = "ref") String token) throws InvalidTokenException, NoSuchUserException {
        AccessTokenContainer accessTokenContainer =
                service.getAccessTokenFromRefreshToken(token);
        return ResponseMessage.<AccessTokenContainer>builder()
                .data(accessTokenContainer)
                .message("success")
                .code(10)
                .build();
    }

    // TODO 받아들이는 타입에따라 분기 가능한가?
//    @PostMapping("/signup")
//    @ResponseStatus(HttpStatus.CREATED)
//    public ResponseMessage<Client> createClient(@RequestBody Client client) {
//        return ResponseMessage.<Client>builder()
//                .data(service.signupClient(client))
//                .message("created")
//                .code(200)
//                .build();
//    }


    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseMessage<Client> createClient(@RequestBody @Validated UserForm form) throws SQLIntegrityConstraintViolationException {
        return ResponseMessage.<Client>builder()
                .data(service.signupClient(form))
                .message("created")
                .code(200)
                .build();
    }

    @PostMapping("/login")
    public ResponseMessage<AccessTokenContainer> login(@RequestBody LoginToken loginToken, HttpServletResponse response) throws InvalidTokenException, LoginFailedException {
        AccessTokenContainer container = service.login(loginToken);
        String refToken = service.getRefreshToken(loginToken.getUsername());
        setRef(response, refToken);
        return ResponseMessage.<AccessTokenContainer>builder()
                .message("success")
                .data(service.login(loginToken))
                .build();
    }

    private void setRef(HttpServletResponse response, String refToken) {
        int e =1000 * 60 * 60 * 24 * 3;
        Date exp = new Date(new Date().getTime() + e);
        final String HEADER = "ref=" + refToken +"; path=/; Secure; SameSite-None; HttpOnly; Max-Age=" + e;
        response.setHeader("Set-Cookie", HEADER);
    }



}
