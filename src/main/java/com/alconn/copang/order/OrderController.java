package com.alconn.copang.order;

import com.alconn.copang.annotations.InjectId;
import com.alconn.copang.client.Role;
import com.alconn.copang.common.ResponseMessage;
import com.alconn.copang.exceptions.NoSuchEntityExceptions;
import com.alconn.copang.exceptions.UnauthorizedException;
import com.alconn.copang.exceptions.ValidationException;
import com.alconn.copang.order.dto.OrderForm;
import com.alconn.copang.order.dto.ReturnOrderForm;
import com.alconn.copang.order.dto.SellerOrderForm.Response;
import com.alconn.copang.shipment.ShipmentForm;
import java.nio.file.AccessDeniedException;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService service;

    @GetMapping
    public String prepareOrder() {
        return "ok";
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseMessage<OrderForm.Response> startOrder(
        @Validated @RequestBody OrderForm.Create form,
        @InjectId Long clientId) {

        return ResponseMessage.<OrderForm.Response>builder()
            .message("order_ready")
            .code(200)
            .data(service.placeOrder(form, clientId))
            .build();
    }

    @PostMapping("/ready")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseMessage<OrderForm.Response> prepareOrder(@RequestBody OrderForm.Create form,
        @InjectId Long clientId) {
        return ResponseMessage.success(
            service.readyOrder(form, clientId)
        );
    }

    @PostMapping("/{orderId}/pay/{uid}")
    public ResponseMessage<OrderForm.Response> paymentCheck(
        @InjectId Long clientId, @PathVariable(value = "orderId") Long orderId,
        @PathVariable(value = "uid") String uid)
        throws NoSuchEntityExceptions, ValidationException, UnauthorizedException, AccessDeniedException {
        return ResponseMessage.success(service.orderPayment(uid, clientId, orderId));
    }

//    @PatchMapping("/{sellerOrderId}/shipment")
//    public ResponseMessage<OrderForm.Response> updateOrderState(@PathVariable Long sellerOrderId)
//        throws NoSuchEntityExceptions {
//
//        return ResponseMessage.<OrderForm.Response>builder()
//            .message("success")
//            .data(service.placeShipment(sellerOrderId))
//            .build();
//    }

    @PatchMapping("/{orderId}/cancel")
    public ResponseMessage<OrderForm.Response> cancelOrder(@PathVariable Long orderId)
        throws NoSuchEntityExceptions {

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

    @GetMapping("/seller")
    public ResponseMessage<List<Response>> getSellerOrders(
        @InjectId(role = Role.SELLER) Long sellerId) {
        return ResponseMessage.success(
            service.getOrdersBySeller(sellerId)
        );
    }


    @GetMapping("/{orderId}")
    public ResponseMessage<OrderForm.Response> getClientOrder(
        @PathVariable(name = "orderId") Long orderId) {
        return ResponseMessage.<OrderForm.Response>builder()
            .message("success")
            .data(service.getOneOrder(orderId))
            .build();
    }

    @PostMapping("/shipments")
    public ResponseMessage<?> placeShipment(@Validated @RequestBody List<ShipmentForm.Request> form,
        @InjectId(role = Role.SELLER) Long sellerId) throws AccessDeniedException {
        return ResponseMessage.success(
            service.placeShipment(form, sellerId)
        );
    }

    @PostMapping("/shipment")
    public ResponseMessage<?> placeShipment(@Validated @RequestBody ShipmentForm.Request form,
        @InjectId(role = Role.SELLER) Long sellerId) throws AccessDeniedException {
        return ResponseMessage.success(
            service.placeShipment(Collections.singletonList(form), sellerId)
        );
    }

    @PostMapping("/return/{orderItemId}")
    public ResponseMessage<?> returnOrderItem(@RequestBody ReturnOrderForm.Request request,
        @InjectId Long clientId, @PathVariable Long orderItemId)
        throws NoSuchEntityExceptions, AccessDeniedException {
        return ResponseMessage.success(
            service.receiptReturnOrder(request, orderItemId, clientId)
        );
    }

    @GetMapping("/orders/items/canceled")
    public ResponseMessage<?> returnCanceledOrderItems(@InjectId Long clineId) {
        return ResponseMessage.success(
            service.getCanceledItems(clineId)
        );
    }

}
