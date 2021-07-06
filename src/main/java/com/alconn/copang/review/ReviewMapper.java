package com.alconn.copang.review;

import com.alconn.copang.mapper.EntityMapper;
import com.alconn.copang.review.ReviewForm.Response;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReviewMapper {


    @Mapping(source = "form.orderItemId", target = "orderItem.orderItemId", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    @Mapping(source = "form.itemDetailId", target = "orderItem.itemDetail.itemDetailId", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "clientId", target = "writer.clientId")
    Review toEntity(ReviewForm.Request form, Long clientId);

//    @Mapping(source = "orderItem.itemDetail", target = "orderItem.")
    @Mapping(source = "writer.realName", target = "writerName")
    @Mapping(source = "orderItem.itemDetail", target = ".")
    @Mapping(source = "orderItem.itemDetail.item", target = ".")
    @Mapping(source = "orderItem", target = ".")
    ReviewForm.Response toDto(Review review);

    List<Response> toDto(List<Review> list);
}
