package com.alconn.copang.client;

import com.alconn.copang.common.ResponseMessage;
import com.alconn.copang.exceptions.NoSuchUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ClientController {

    public final ClientService service;

    @GetMapping("/user/{id}")
    public ResponseMessage<Client> getUser(@PathVariable Long id) throws NoSuchUserException {
        Client client = service.getClient(id);
        return ResponseMessage.<Client>builder()
                .data(client)
                .build();
    }

    @GetMapping("/user/list")
    public ResponseMessage<List<Client>> getAllUsers() {
        return ResponseMessage.<List<Client>>builder()
                .data(service.getAllClients())
                .message("success")
                .build();
    }
}
