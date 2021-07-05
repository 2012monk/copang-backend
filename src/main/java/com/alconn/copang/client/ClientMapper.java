package com.alconn.copang.client;

import com.alconn.copang.mapper.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

//@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ClientMapper extends EntityMapper<UserForm, Client> {


    UserForm c(final Client client);


    UserForm.Response toResponse(final Client client);

//    @Mapping(target = "id", ignore = true)
    Client toEntity(UserForm form);
}
