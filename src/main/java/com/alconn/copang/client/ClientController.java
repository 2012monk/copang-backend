package com.alconn.copang.client;

import com.alconn.copang.common.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ClientController {

    private final ClientRepo repo;

    @GetMapping("/user/{id}")
    public ResponseMessage<Client> getUser(@PathVariable Long id)  {
        return ResponseMessage.<Client>builder()
                .data(repo.getById(id))
                .build();
    }

    @GetMapping("/user/list")
    public ResponseMessage<List<Client>> getAllUsers() {
        return ResponseMessage.<List<Client>>builder()
                .data(repo.findAll())
                .message("success")
                .build();
    }
}
