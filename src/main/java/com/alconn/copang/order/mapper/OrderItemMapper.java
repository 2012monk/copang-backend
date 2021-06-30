package com.alconn.copang.order.mapper;

import com.alconn.copang.mapper.EntityMapper;
import com.alconn.copang.order.OrderItem;
import com.alconn.copang.order.dto.OrderItemForm;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderItemMapper extends EntityMapper<OrderItemForm, OrderItem> {


}
