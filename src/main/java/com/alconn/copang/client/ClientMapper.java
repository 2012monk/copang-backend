package com.alconn.copang.client;

import com.alconn.copang.mapper.EntityMapper;
import com.alconn.copang.seller.Seller;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

//@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ClientMapper extends EntityMapper<UserForm, Client> {


    UserForm c(final Client client);


    UserForm.Response toResponse(final Client client);

    UserForm.SellerResponse toResponse(final Seller seller);

    //    @Mapping(target = "id", ignore = true)
    Client toEntity(UserForm form);

    Seller toSeller(UserForm form);
}
