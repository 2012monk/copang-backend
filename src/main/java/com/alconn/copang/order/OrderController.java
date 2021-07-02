package com.alconn.copang.order;

import com.alconn.copang.annotations.InjectId;
import com.alconn.copang.common.ResponseMessage;
import com.alconn.copang.exceptions.NoSuchEntityExceptions;
import com.alconn.copang.order.dto.OrderForm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    // TODO INJECT ID
    private final OrderService service;

    @GetMapping
    public String prepareOrder() {
        return "ok";
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseMessage<OrderForm.Response> startOrder(@Validated @RequestBody OrderForm.Create form,
        @InjectId Long clientId){

        return ResponseMessage.<OrderForm.Response>builder()
                .message("order_ready")
                .code(200)
                .data(service.createOrder(form, clientId))
                .build();
    }

    @PatchMapping("/{orderId}/proceed")
    public ResponseMessage<OrderForm.Response> updateOrderState(@PathVariable Long orderId) throws NoSuchEntityExceptions {

        return ResponseMessage.<OrderForm.Response>builder()
                .message("success")
                .data(service.proceedOrder(orderId))
                .build();
    }

    @PatchMapping("/{orderId}/cancel")
    public ResponseMessage<OrderForm.Response> cancelOrder(@PathVariable Long orderId) throws NoSuchEntityExceptions {

        return ResponseMessage.<OrderForm.Response>builder()
                .message("success")
                .data(service.cancelOrder(orderId))
                .build();
    }

    @GetMapping("/client")
    public ResponseMessage<List<OrderForm.Response>> getClientOrders(@InjectId Long clientId) {

        return ResponseMessage.<List<OrderForm.Response>>builder()
                .message("success")
                .data(service.listOrderClient(clientId))
                .build();
    }


    @GetMapping("/{orderId}")
    public ResponseMessage<OrderForm.Response> getClientOrder(@PathVariable(name = "orderId") Long orderId) {
        return ResponseMessage.<OrderForm.Response>builder()
                .message("success")
                .data(service.getOneOrder(orderId))
                .build();
    }
}
