package com.alconn.copang.cart;

import com.alconn.copang.annotations.InjectId;
import com.alconn.copang.common.ResponseMessage;
import com.alconn.copang.exceptions.NoSuchEntityExceptions;
import com.alconn.copang.exceptions.NoSuchUserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService service;

    @GetMapping("/test")
    public Long testInject(@InjectId Long id) {
        return id;
    }

    @Secured("ROLE_CLIENT")
    @PostMapping("/item")
    public ResponseMessage<?> addItem(@RequestBody CartForm.Add addForm, @InjectId Long clientId)
        throws NoSuchUserException {

        return ResponseMessage.builder()
            .message("success")
            .data(service.addCartItem(clientId, addForm))
            .build();
    }

    @PostMapping("/item/amount")
    public ResponseMessage<CartItemForm> updateAmount(@RequestBody CartForm.Add addForm,
        @InjectId Long clientId)
        throws NoSuchEntityExceptions {
        log.info("cart add {} client ={}", addForm.getItemDetailId(), clientId);
        return ResponseMessage.<CartItemForm>builder()
            .data(
                service.updateAmountItem(clientId, addForm.getItemDetailId(), addForm.getAmount()))
            .message("success")
            .build();
    }

    @DeleteMapping("/item/{itemDetailId}")
    public ResponseMessage<?> deleteItem(@PathVariable(name = "itemDetailId") Long itemDetailId,
        @InjectId Long clientId)
        throws NoSuchEntityExceptions {

        return ResponseMessage.builder()
            .data(service.deleteItem(clientId, itemDetailId))
            .message("success")
            .build();
    }

    @GetMapping
    public ResponseMessage<CartForm.Response> getClientCart(@InjectId Long clientId) {
        return ResponseMessage.<CartForm.Response>builder()
            .data(service.getCart(clientId))
            .message("success")
            .build();
    }

    @DeleteMapping
    public ResponseMessage<String> clearCart(@InjectId Long clientId)
        throws NoSuchEntityExceptions {
        service.clearCart(clientId);
        return ResponseMessage.<String>builder()
            .message("success")
            .build();
    }
}
