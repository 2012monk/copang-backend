package com.alconn.copang.order.mapper;

import com.alconn.copang.mapper.EntityMapper;
import com.alconn.copang.order.OrderItem;
import com.alconn.copang.order.Orders;
import com.alconn.copang.order.dto.OrderForm;
import com.alconn.copang.order.dto.OrderItemForm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper extends EntityMapper<OrderForm, Orders> {

//    @Mapping(target = "address.addressId", source = "addressId")


//    @Mapping(source = "dto.itemDetailId", target = "itemDetail.itemDetailId")
//    @Mapping(source = "dto.", target = "itemDetail.item")
//    @Mapping(source = "dto.", target = "itemDetail.")
//    @Mapping(source = "dto.orderItems.", target = "orderItemList.itemDetail.item")
    @Mapping(source = "dto.orderItems", target = "orderItemList")
    @Mapping(source = "clientId", target = "client.clientId")
    @Mapping(source = "dto.addressId", target = "address.addressId")
    Orders placeOrder(OrderForm.Create dto, Long clientId);

    OrderForm.Response toResponse(Orders orders);


    @Mapping(source = "itemDetailId", target = "itemDetail.itemDetailId")
    @Mapping(source = ".", target = "itemDetail.item")
    @Mapping(source = ".", target = "itemDetail.")
    OrderItem toItem(OrderItemForm dto);
}
