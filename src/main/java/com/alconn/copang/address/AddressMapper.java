package com.alconn.copang.address;

import com.alconn.copang.client.Client;
import com.alconn.copang.mapper.EntityMapper;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AddressMapper extends EntityMapper<AddressForm, Address>{

    @Mapping(target = "priority", ignore = true)
    Address toEntity(AddressForm address, Client client);

    @Mapping(target = "priority", ignore = true)
    @Mapping(source = "clientId", target = "client.clientId")
    @Override
    Address toEntity(final AddressForm dto);

    List<AddressForm> toDto(List<Address> list);

    void update(@MappingTarget Address address, AddressForm form);


}
