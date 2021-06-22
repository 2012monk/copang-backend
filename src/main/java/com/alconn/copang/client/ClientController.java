package com.alconn.copang.client;

import com.alconn.copang.common.ResponseMessage;
import com.alconn.copang.exceptions.ValidationException;
import com.alconn.copang.exceptions.NoSuchUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class ClientController {

    public final ClientService service;

    @GetMapping("/{id}")
    public ResponseMessage<Client> getUser(@PathVariable Long id) throws NoSuchUserException {
        Client client = service.getClient(id);
        return ResponseMessage.<Client>builder()
                .data(client)
                .build();
    }

    @GetMapping("/list")
    public ResponseMessage<List<Client>> getAllUsers() {
        return ResponseMessage.<List<Client>>builder()
                .data(service.getAllClients())
                .message("success")
                .build();
    }

    @PutMapping
    public ResponseMessage<Client> updateUser(@RequestBody @Valid UserForm form, BindingResult result) throws ValidationException, NoSuchUserException {
        if (result.hasErrors()){
            throw new ValidationException(result.getSuppressedFields());
        }
        return ResponseMessage.<Client>builder()
                .data(service.updateClient(form))
                .message("update_success")
                .build();
    }
}
