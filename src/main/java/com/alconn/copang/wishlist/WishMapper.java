package com.alconn.copang.wishlist;

import com.alconn.copang.wishlist.dto.WishResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface WishMapper {

    @Mapping(source = "itemDetail", target = ".")
    @Mapping(source = "itemDetail.item.itemName", target = "ItemName")
//    @Mapping(source = "client.clientId", target = "clientid")
    WishResponse toRes(Wish wish);

    List<WishResponse> toResList(List<Wish> wishList);

}
