package com.alconn.copang.client;

import com.alconn.copang.common.AccessTokenContainer;
import com.alconn.copang.common.LoginToken;
import com.alconn.copang.common.ResponseMessage;
import com.alconn.copang.exceptions.InvalidTokenException;
import com.alconn.copang.exceptions.LoginFailedException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ClientAuthController {

    private final ClientService service;

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
