package com.alconn.copang.item;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ItemMapper {

    @Mapping(source = "itemName",target = "item.itemName")
    @Mapping(source = "itemId",target = "item.itemId")
    ItemDetail dtoToDomain(final ItemDetailForm itemDetailform);

     @Mapping(source = "item.itemName",target = "itemName")
     @Mapping(source = "item.itemId",target = "itemId")
    ItemDetailForm domainToDto(final ItemDetail itemDetail);


    List<ItemDetail> listDtoToDomain(final List<ItemDetailForm> itemDetailForms);


    List<ItemDetailForm> listDomainToDto(final List<ItemDetail> itemDetail);

}
