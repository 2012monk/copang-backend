package com.alconn.copang.cart;

import com.alconn.copang.item.Item;
import com.alconn.copang.item.ItemDetail;
import java.util.List;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CartMapper {

    CartItemForm cartItemToDto(ItemDetail itemDetail, Item item, int amount);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "item.item", target = ".")
    @Mapping(source = "item", target = ".")
    @Mapping(source = "cart", target = ".")
    CartItemForm cartItemToDto(CartItem item);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "cartItems", target = ".")
    CartForm.Response cartToResponse(Cart cart);

    CartForm.Response convert(Cart cart);

    @Mapping(source = "list", target = "cartItems")
    CartForm.Response convert(Long cartId, List<CartItem> list);

}
