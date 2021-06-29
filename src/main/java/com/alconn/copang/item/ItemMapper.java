package com.alconn.copang.item;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ItemMapper {

    //상품메인 조회
    List<ItemDetailForm.MainForm> mainPage(List<ItemDetail> itemDetailList);

    @Mapping(target = ".", source = "item")
    ItemDetailForm.MainForm toPage(ItemDetail detail);

    @Mapping(target = "item", source = ".")
    ItemDetail toEntity(ItemDetailForm.MainForm mainForm);



    @Mapping(source = "itemName",target = "item.itemName")
    @Mapping(source = "itemId",target = "item.itemId")
    ItemDetail dtoToDomain(final ItemDetailForm itemDetailform);

     @Mapping(source = "item.itemName",target = "itemName")
     @Mapping(source = "item.itemId",target = "itemId")
    ItemDetailForm domainToDto(final ItemDetail itemDetail);

    List<ItemDetail> listDtoToDomain(final List<ItemDetailForm> itemDetailForms);


    List<ItemDetailForm> listDomainToDto(final List<ItemDetail> itemDetail);

    //상품상세 조회
    @Mapping(source = "itemDetailList",target = "itemDetailFormList")
    ItemForm itemDetailToDto(Item item, List<ItemDetail> itemDetailList);

    //업데이트
//    @Mapping(source = "itemDetailList",target = "itemDetailUpdateList")
//    ItemForm.ItemFormUpdate itemDetailUpdateToDto(Item item, List<ItemDetail> itemDetailList);




    List<ItemDetail> listDtoToDomainN(List<ItemDetailForm.DetailForm> detailFormList);


    List<ItemDetail> updateItemDetaillistToDomain(List<ItemDetailForm.DetailUpdateClass> detailUpdateClassList);




}
