package com.alconn.copang.order;

import com.alconn.copang.common.ResponseMessage;
import com.alconn.copang.exceptions.NoSuchEntityExceptions;
import com.alconn.copang.order.dto.OrderForm;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService service;

    @PostMapping
    public ResponseMessage<OrderForm.Response> startOrder(@RequestBody OrderForm.Create form){

        return ResponseMessage.<OrderForm.Response>builder()
                .message("order_ready")
                .code(200)
                .data(service.createOrder(form))
                .build();

    }

    @PatchMapping("/{orderId}/proceed")
    public ResponseMessage<OrderForm.Response> updateOrderState(@PathVariable Long orderId) throws NoSuchEntityExceptions {

        return ResponseMessage.<OrderForm.Response>builder()
                .message("success")
                .data(service.updateOrderState(orderId, OrderStatus.PROCEED))
                .code(143)
                .build();
    }

    @PatchMapping("/{orderId}/cancel")
    public ResponseMessage<OrderForm.Response> cancelOrder(@PathVariable Long orderId) throws NoSuchEntityExceptions {

        return ResponseMessage.<OrderForm.Response>builder()
                .message("success")
                .data(service.updateOrderState(orderId, OrderStatus.CANCELED))
                .code(111)
                .build();
    }

    @GetMapping("/{clientId}")
    public ResponseMessage<List<OrderForm.Response>> getClientOrders(@PathVariable(name = "clientId") Long clientId) {

        return ResponseMessage.<List<OrderForm.Response>>builder()
                .message("success")
                .data(service.listOrderClient(clientId))
                .build();
    }
}
