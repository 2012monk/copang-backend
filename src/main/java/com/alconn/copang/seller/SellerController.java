package com.alconn.copang.seller;

import com.alconn.copang.annotations.InjectId;
import com.alconn.copang.common.ResponseMessage;

import com.alconn.copang.item.ItemDetailForm;
import com.alconn.copang.shipment.Shipment;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/seller")
public class SellerController {

    @PostMapping("/item")
    public ResponseMessage<?> registerItem(@RequestBody ItemDetailForm form,
        @InjectId Long sellerId) {
        return null;
    }

    @PostMapping("/items")
    public ResponseMessage<?> registerItems(@RequestBody List<ItemDetailForm> forms,
        @InjectId Long sellerId) {
        return null;
    }

    @GetMapping("/orders")
    public ResponseMessage<?> getOrderList() {
        return null;
    }

    @GetMapping("/order")
    public ResponseMessage<?> getOrder(Long orderId) {
        return null;
    }


    @PostMapping("/order/proceed")
    public ResponseMessage<?> proceedOrder(Long orderId) {
        return null;
    }

    @PostMapping("/shipment")
    public ResponseMessage<?> placeShipment(@RequestBody Shipment shipment, Long orderId) {
        return null;
    }

    @GetMapping("/sales")
    public ResponseMessage<?> getSales(@InjectId Long id) {
        return null;
    }


}