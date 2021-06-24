package com.alconn.copang.item;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ItemMapper {

    ItemDetail dtoToDomain(final ItemDetailForm itemDetailform);
    ItemDetailForm domainToDto(final ItemDetail itemDetail);

    List<ItemDetail> listDtoToDomain(final List<ItemDetailForm> itemDetailForms);
    List<ItemDetailForm> listDomainToDto(final List<ItemDetail> itemDetail);

}
