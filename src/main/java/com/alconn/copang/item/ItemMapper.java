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

    List<ItemDetail> listDtoToDomainN(List<ItemDetailForm.DetailForm> detailFormList);

    List<ItemDetail> updateItemDetaillistToDomain(List<ItemDetailForm.DetailUpdateClass> detailUpdateClassList);

    //update ItemDetailList + item => to Form 매핑
    //update 받고 보내는 form
    @Mapping(source = "itemDetailList", target = "itemDetailUpdateClassList")
    ItemForm.ItemFormUpdate  updateItemForm(Item item,List<ItemDetail> itemDetailList);


    //단일 저장
    ItemDetail saveOneToEntity(ItemDetailForm.DetailForm detailForm);

    @Mapping(source = "item", target = ".")
    @Mapping(source = ".", target = "detailForm")
    ItemForm.ItemSingle saveOneToForm(ItemDetail itemDetail);

    //단일 수정-
    @Mapping(source = "item", target = ".")
    @Mapping(source = ".", target = "detailUpdateClass")
    ItemForm.ItemFormUpdateSingle updateOneToForm(ItemDetail itemDetail);

    //단일저장, 수정
    @Mapping(source = "item", target = ".")
    @Mapping(source = ".", target = "itemDetailViewForm")
    ItemViewForm detailViewForm(ItemDetail itemDetail);
}
