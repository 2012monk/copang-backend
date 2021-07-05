package com.alconn.copang.inquiry;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InquiryMapper {


    @Mapping(source = "clientId", target = "client.clientId")
    @Mapping(source = "request.itemDetailId", target = "itemDetail.itemDetailId")
    Inquiry toEntity(InquiryForm.Request request, Long clientId);

    @Mapping(source = "sellerId", target = "seller.clientId")
    Reply toReply(InquiryForm.Request request, Long sellerId);

//    @Mapping(source = "reply", target = "reply")
//    @Mapping(source = "reply.seller.sellerName", target = "reply.sellerName")
    @Mapping(source = "itemDetail.item", target = ".")
    @Mapping(source = "itemDetail", target = ".")
    @Mapping(source = "inquiry.client.realName", target = "clientName")
    @Mapping(source = "reply.seller.sellerName", target = "reply.sellerName")
    @Mapping(source = "reply.seller.clientId", target = "reply.sellerId")
    @Mapping(source = "reply.seller.sellerCode", target = "reply.sellerCode")
    @Mapping(source = "reply", target = "reply", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    @Mapping(source = "content", target = "content")
    InquiryForm.Response toDto(Inquiry inquiry);

//    @Mapping(target = "sellerName", source = "seller.sellerName")
//    @Mapping(target = "sellerId", source = "seller.clientId")
//    @Mapping(target = ".", source = "seller.")
//    InquiryForm.ReplyForm toReplyForm(Reply reply);

}
