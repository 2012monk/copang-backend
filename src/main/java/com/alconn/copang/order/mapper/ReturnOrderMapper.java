package com.alconn.copang.order.mapper;

import com.alconn.copang.order.OrderItem;
import com.alconn.copang.order.ReturnOrder;
import com.alconn.copang.order.dto.OrderItemForm;
import com.alconn.copang.order.dto.ReturnOrderForm.Response;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReturnOrderMapper {

    @Mapping(source = "impCancelInfo.canceledAt", target = "canceledAt")
    @Mapping(source = "impCancelInfo.receipt_url", target = "cancelReceiptUrl")
    @Mapping(source = "address.address", target = "address.address")
    @Mapping(source = "orderItems", target = "returnItems")
    Response toResponse(ReturnOrder order);

    @Mapping(source = "shipment", target = ".")
    @Mapping(source = "itemDetail", target = ".")
    @Mapping(source = "itemDetail.item", target = ".")
    OrderItemForm toForm(OrderItem orderItem);


    List<Response> toResponse(List<ReturnOrder> orders);
}
