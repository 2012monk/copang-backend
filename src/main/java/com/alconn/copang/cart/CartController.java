package com.alconn.copang.cart;

import com.alconn.copang.common.ResponseMessage;
import com.alconn.copang.exceptions.NoSuchEntityExceptions;
import com.alconn.copang.exceptions.NoSuchUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService service;

    @Secured("ROLE_CLIENT")
    @PostMapping("/item")
    public ResponseMessage<?> addItem(@RequestBody CartForm.Add addForm, Long clientId) throws NoSuchUserException {

        return ResponseMessage.<CartForm.Response>builder()
                .message("success")
                .data(service.addCartItem(clientId, addForm))
                .build();
    }

    @PostMapping("/item/amount/{amount}")
    public ResponseMessage<?> updateAmount(@RequestBody CartForm.Add addForm, Long clientId,
                                           @PathVariable(name = "amount") int amount) throws NoSuchEntityExceptions {
        return ResponseMessage.builder()
                .data(service.addAmountItem(clientId, addForm.getItemDetailId(), amount))
                .build();
    }

    @DeleteMapping("/item")
    public ResponseMessage<?> deleteItem(@RequestBody CartForm.Add form, Long clientId) throws NoSuchEntityExceptions {

        return ResponseMessage.builder()
                .data(service.deleteItem(clientId, form.getItemDetailId()))
                .build();
    }

    @GetMapping
    public ResponseMessage<CartForm.Response> getClientCart(Long clientId) {
        return ResponseMessage.<CartForm.Response>builder()
                .data(service.getCart(clientId))
                .message("success")
                .build();
    }
}
