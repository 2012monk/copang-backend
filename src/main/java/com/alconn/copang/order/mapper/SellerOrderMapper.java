package com.alconn.copang.order.mapper;

import com.alconn.copang.order.OrderItem;
import com.alconn.copang.order.Orders;
import com.alconn.copang.order.SellerOrder;
import com.alconn.copang.order.dto.OrderItemForm;
import com.alconn.copang.order.dto.SellerOrderForm;
import com.alconn.copang.shipment.Shipment;
import com.alconn.copang.shipment.ShipmentForm.Request;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SellerOrderMapper {

    //    @Mapping(source = "")
    @Mapping(source = "sellerOrder.totalPrice", target = "totalPrice")
    @Mapping(source = "sellerOrder.totalAmount", target = "totalAmount")
    @Mapping(source = "orders.client", target = "client")
    @Mapping(source = "orders.address.address", target = "address.address")
    SellerOrderForm.Response mtoForm(SellerOrder sellerOrder, Orders orders);

    @Mapping(source = "itemDetail", target = ".")
    @Mapping(source = "itemDetail.item", target = ".")
    OrderItemForm toForm(OrderItem item);

    Shipment toShipment(Request form);
}
