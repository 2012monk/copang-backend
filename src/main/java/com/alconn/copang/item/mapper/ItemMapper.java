package com.alconn.copang.item.mapper;

import com.alconn.copang.item.Item;
import com.alconn.copang.item.ItemDetail;
import com.alconn.copang.item.dto.ItemDetailForm;
import com.alconn.copang.item.dto.ItemForm;
import com.alconn.copang.item.dto.ItemViewForm;
import com.alconn.copang.shipment.ShipmentForm;
import com.alconn.copang.shipment.ShipmentInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ItemMapper {

    //배송정보 매핑
    @Mapping(source = "shipmentInfoForm",target = ".")
    ShipmentInfo shipToEntity(ItemForm itemForm);

    //상품메인 조회
    @Mapping(source = "itemDetailList.item.averageRating", target = "averageRating")
    @Mapping(source = "itemDetailList.item.shipmentInfo",target = "shipmentInfoForm")
    List<ItemDetailForm.MainForm> mainPage(List<ItemDetail> itemDetailList);



    @Mapping(target = ".", source = "item.seller")
    @Mapping(target = ".", source = "item")
    @Mapping(target = ".",source = "item.category")
    @Mapping(source = "item.shipmentInfo", target = "shipmentInfoForm")
    ItemDetailForm.MainForm toPage(ItemDetail detail);

    // Seller mapping 추가
//    @Mapping(source = "sellerId", target = "item.seller.clientId")
    @Mapping(target = "item", source = ".")
    ItemDetail toEntity(ItemDetailForm.MainForm mainForm);

    @Mapping(source = "itemName",target = "item.itemName")
    @Mapping(source = "itemId",target = "item.itemId")
    ItemDetail dtoToDomain(final ItemDetailForm itemDetailform);

    @Mapping(source = "item.itemName",target = "itemName")
    @Mapping(source = "item.itemId",target = "itemId")
    ItemDetailForm domainToDto(final ItemDetail itemDetail);


    List<ItemDetailForm> listDomainToDto(final List<ItemDetail> itemDetail);

    //상품상세 조회
    @Mapping(source = "itemDetailList",target = "itemDetailFormList")
    @Mapping(source = "item.category.categoryId",target = "categoryId")
    @Mapping(source = "item.shipmentInfo", target = "shipmentInfoForm")
    ItemForm itemDetailToDto(Item item, List<ItemDetail> itemDetailList);


    @Mapping(target = "item", source = ".")
    List<ItemDetail> listDtoToDomainN(List<ItemDetailForm.DetailForm> detailFormList);

    @Mapping(source = "itemDetailList", target = "itemDetailFormList")
    @Mapping(source = "item.category.categoryId", target = "categoryId")
    @Mapping(source = "item.shipmentInfo", target = "shipmentInfoForm")
    ItemForm  updateItemForm(Item item,List<ItemDetail> itemDetailList);


    //단일 저장
    ItemDetail saveOneToEntity(ItemDetailForm.DetailForm detailForm);


    //단일저장, 수정
    @Mapping(source = "item", target = ".")
    //====
    @Mapping(source = "item.category",target = ".")
    //====
    @Mapping(source = ".", target = "itemDetailViewForm")
    ItemViewForm detailViewForm(ItemDetail itemDetail);




}

//    List<ItemDetail> listDtoToDomain(final List<ItemDetailForm> itemDetailForms);

//    @Mapping(source = "item", target = ".")
//    @Mapping(source = ".", target = "detailForm")
//    ItemForm.ItemSingle saveOneToForm(ItemDetail itemDetail);
//
//    //단일 수정-
//    @Mapping(source = "item", target = ".")
//    @Mapping(source = ".", target = "detailUpdateClass")
//    ItemForm.ItemFormUpdateSingle updateOneToForm(ItemDetail itemDetail);
//    List<ItemDetail> updateItemDetaillistToDomain(List<ItemDetailForm.DetailUpdateClass> detailUpdateClassList);
//    List<ItemDetail> listDtoToDomain(final List<ItemDetailForm> itemDetailForms);
