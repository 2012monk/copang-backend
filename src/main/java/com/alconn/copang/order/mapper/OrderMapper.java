package com.alconn.copang.order.mapper;

import com.alconn.copang.mapper.EntityMapper;
import com.alconn.copang.order.OrderItem;
import com.alconn.copang.order.Orders;
import com.alconn.copang.order.dto.OrderForm;
import com.alconn.copang.order.dto.OrderItemForm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper extends EntityMapper<OrderForm, Orders> {

//    @Mapping(target = "address.addressId", source = "addressId")


    //    @Mapping(source = "dto.itemDetailId", target = "itemDetail.itemDetailId")
    // TODO 주문 기능과 연동후 풀어야됨
//    @Mapping(source = "dto.", target = "itemDetail.item")
//    @Mapping(source = "dto.", target = "itemDetail.")
//    @Mapping(source = "dto.orderItems.", target = "orderItemList.itemDetail.item")
    @Mapping(source = "dto.orderItems", target = "orderItemList")
    @Mapping(source = "clientId", target = "client.clientId")
    @Mapping(source = "dto.addressId", target = "address.addressId")
    Orders placeOrder(OrderForm.Create dto, Long clientId);

//    @Mapping(source = "orderItemList.itemDetail.item", target = "orderItems.itemDetail.item")
//    @Mapping(source = "orderItemList.itemDetail", target = "orderItems.optionName")
//    @Mapping(source = "orderItemList.itemDetail", target = "orderItems.value")
//    @Mapping(source = "orderItemList.itemDetail", target = "orderItems.itemDetailId")
//    @Mapping(source = "", target = "orderItems.price")
    @Mapping(source = "impPaymentInfo.imp_uid", target = "uid")
    @Mapping(source = "orderItemList.", target = "orderItems.")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "client", target = ".")
    OrderForm.Response toResponse(Orders orders);


    @Mapping(source = "itemDetailId", target = "itemDetail.itemDetailId")
//    @Mapping(source = ".", target = "itemDetail.item")
    @Mapping(source = ".", target = "itemDetail.")
    @Mapping(target = "orderItemId", ignore = true)
    @Mapping(target = "itemDetail.item", ignore = true) // item 아이디 무시 deatil id 만으로 빌드
//    @Mapping(source = "orderItemId", target = "orderItemId", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    OrderItem toItem(OrderItemForm dto);

    @Mapping(source = "shipment", target = ".")
    @Mapping(source = "itemDetail", target = ".")
    @Mapping(source = "itemDetail.item", target = ".")
    OrderItemForm toForm(OrderItem item);
}
