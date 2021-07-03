package com.alconn.copang.inquiry;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InquiryMapper {


    Inquiry toEntity(InquiryForm.Request request);

//    @Mapping(source = "reply.seller.sellerName", target = "reply.sellerName")
//    @Mapping(source = "inquiry.realName", target = "clientName")
    @Mapping(source = "reply", target = ".")
    InquiryForm.Response toDto(Inquiry inquiry);


}
