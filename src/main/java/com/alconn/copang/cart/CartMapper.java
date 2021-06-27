package com.alconn.copang.cart;

import com.alconn.copang.item.Item;
import com.alconn.copang.item.ItemDetail;
import com.alconn.copang.item.ItemDetailForm;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CartMapper {

    CartItemForm cartItemToDto(ItemDetail itemDetail, Item item, int amount) ;


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "item.item", target = ".")
    @Mapping(source = "item", target = ".")
    CartItemForm cartItemToDto(CartItem item);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "cartItems", target = ".")
    CartForm.Response cartToResponse(Cart cart);

}
