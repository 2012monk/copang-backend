package com.alconn.copang.payment;

import com.alconn.copang.payment.dto.PaymentInfoFrom;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PaymentMapper {

    ImpPaymentInfo toEntity(PaymentInfoFrom paymentInfoFrom);


}
