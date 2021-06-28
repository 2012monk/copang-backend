package com.alconn.copang.order.mapper;

import com.alconn.copang.mapper.EntityMapper;
import com.alconn.copang.order.Orders;
import com.alconn.copang.order.dto.OrderForm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper extends EntityMapper<OrderForm, Orders> {

//    @Mapping(target = "address.addressId", source = "addressId")
    Orders convertCreate(OrderForm.Create dto);

    OrderForm.Response toResponse(Orders orders);
}
