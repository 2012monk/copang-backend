package com.alconn.copang.shipment;

import com.alconn.copang.order.OrderItem;
import com.alconn.copang.order.Orders;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;

    public ShipmentForm.Response placeShipment(List<OrderItem> orderItems, ShipmentForm shipmentForm){
        return null;
    }

}
