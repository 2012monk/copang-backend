package com.alconn.copang.client;

import com.alconn.copang.annotations.IdentitySecured;
import com.alconn.copang.annotations.InjectId;
import com.alconn.copang.common.ResponseMessage;
import com.alconn.copang.exceptions.ValidationException;
import com.alconn.copang.exceptions.NoSuchUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class ClientController {

    public final ClientService service;

    @Secured("ROLE_CLIENT")
    @GetMapping()
    public ResponseMessage<Client> getUser(@InjectId Long id) throws NoSuchUserException {
        Client client = service.getClient(id);
        return ResponseMessage.<Client>builder()
                .message("success")
                .data(client)
                .build();
    }

//    @Secured("ROLE_CLIENT")
    @GetMapping("/list")
    public ResponseMessage<List<Client>> getAllUsers() {
        return ResponseMessage.<List<Client>>builder()
                .data(service.getAllClients())
                .message("success")
                .build();
    }

    @Secured("ROLE_CLIENT")
    @PutMapping
    public ResponseMessage<Client> updateUser(
            @InjectId Long id,
            @RequestBody @Valid UserForm form) throws ValidationException, NoSuchUserException {
        return ResponseMessage.<Client>builder()
                .data(service.updateClient(form, id))
                .message("update_success")
                .build();
    }


    @Secured("ROLE_CLIENT")
    @IdentitySecured
    @DeleteMapping
    public ResponseMessage<String> deleteUser(@InjectId Long id) throws NoSuchUserException {
        service.deleteClient(id);
        return ResponseMessage.<String>builder()
                .message("success")
                .data("blbl")
                .build();
    }
}
