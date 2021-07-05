package com.alconn.copang.item.mapper;

import com.alconn.copang.item.Item;
import com.alconn.copang.item.ItemDetail;
import com.alconn.copang.item.dto.ItemDetailForm;
import com.alconn.copang.item.dto.ItemForm;
import com.alconn.copang.item.dto.ItemViewForm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ItemMapper {

    //상품메인 조회
    List<ItemDetailForm.MainForm> mainPage(List<ItemDetail> itemDetailList);

    @Mapping(target = ".", source = "item")
    @Mapping(target = ".",source = "item.category")
    ItemDetailForm.MainForm toPage(ItemDetail detail);

    // Seller mapping 추가
    @Mapping(source = "sellerId", target = "item.seller.clientId")
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
    //=====
    @Mapping(source = "item.category.categoryId",target = "categoryId")
    //=====
    ItemForm itemDetailToDto(Item item, List<ItemDetail> itemDetailList);

    @Mapping(source = "sellerId" , target = "clientId")
    @Mapping(target = "item", source = ".")
    List<ItemDetail> listDtoToDomainN(List<ItemDetailForm.DetailForm> detailFormList);

    //update ItemDetailList + item => to Form 매핑
    //update 받고 보내는 form
    @Mapping(source = "itemDetailList", target = "itemDetailUpdateClassList")
    //====
    @Mapping(source = "item.category.categoryId", target = "categoryId")
    //====
    ItemForm.ItemFormUpdate  updateItemForm(Item item,List<ItemDetail> itemDetailList);


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
