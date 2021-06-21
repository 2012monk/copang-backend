package com.alconn.copang.auth;

import com.alconn.copang.client.Client;
import com.alconn.copang.client.ClientService;
import com.alconn.copang.common.AccessTokenContainer;
import com.alconn.copang.common.LoginToken;
import com.alconn.copang.common.ResponseMessage;
import com.alconn.copang.exceptions.InvalidTokenException;
import com.alconn.copang.exceptions.LoginFailedException;
import com.alconn.copang.exceptions.NoSuchUserException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseMessage<Client> createClient(@RequestBody Client client) {
        return ResponseMessage.<Client>builder()
                .data(service.signupClient(client))
                .message("created")
                .code(200)
                .build();
    }

    @PostMapping("/login")
    public ResponseMessage<AccessTokenContainer> login(@RequestBody LoginToken loginToken) throws InvalidTokenException, LoginFailedException {
        return ResponseMessage.<AccessTokenContainer>builder()
                .message("success")
                .data(service.login(loginToken))
                .build();
    }



}