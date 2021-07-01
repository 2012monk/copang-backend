package com.alconn.copang.review;

import com.alconn.copang.mapper.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReviewMapper {


//    @Mapping(source = "form.itemId", target = "orderItem.itemDetail.item.itemId")
//    @Mapping(source = "form.orderItemId", target = "orderItem.orderItemId")
    @Mapping(source = "form.itemDetailId", target = "orderItem.itemDetail.itemDetailId")
    @Mapping(source = "clientId", target = "writer.clientId")
    Review toEntity(ReviewForm.Request form, Long clientId);

    @Mapping(source = "orderItem.itemDetail", target = "orderItem.")
    @Mapping(source = "orderItem.itemDetail.item.itemName", target = "orderItem.itemName")
    @Mapping(source = "orderItem", target = ".")
    ReviewForm.Response toDto(Review review);

}
