package com.alconn.copang.cart;

import com.alconn.copang.client.ClientRepo;
import com.alconn.copang.exceptions.NoSuchEntityExceptions;
import com.alconn.copang.exceptions.NoSuchUserException;
import com.alconn.copang.item.ItemDetail;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CartService {

    private final CartRepository repository;

    private final ClientRepo clientRepo;

    private final CartMapper mapper;

    @Transactional
    public CartItemForm addCartItem(Long clientId, CartForm.Add form)
        throws NoSuchUserException {
        clientRepo.findById(clientId).orElseThrow(NoSuchUserException::new);

        Cart cart = repository.findCartByClientId(clientId).orElseGet(() ->
            Cart.builder().clientId(clientId).build());

        CartItem cartItem = cart.getCartItems().stream()
            .filter(i -> i.getItem().getItemDetailId().equals(form.getItemDetailId()))
            .collect(Collectors.toList()).stream().findFirst().orElseGet(() ->null);
//            .orElseGet(() ->
//                CartItem.builder()
//                    .item(ItemDetail.builder().itemDetailId(form.getItemDetailId()).build())
////                    .amount(form.getAmount())
//                    .build()
//            );

        if (cartItem == null) {
            cartItem = CartItem.builder()
                .item(ItemDetail.builder().itemDetailId(form.getItemDetailId()).build())
                .build();
            cart.addCartItem(cartItem);
        }

        cartItem.updateAmount(cartItem.getAmount() + 1);

//        CartItem cartItem = CartItem.builder()
//                .item(ItemDetail.builder().itemDetailId(form.getItemDetailId()).build())
//                .amount(form.getAmount())
//                .build();
        repository.save(cart);

//        Set<ItemDetailForm> items = getItemDetailForms(cart);

        CartItemForm res = mapper.cartItemToDto(cartItem);

        CartForm.Response response =
            CartForm.Response.builder()
                .cartId(cart.getCartId())
                .cartItemId(cartItem.getCartItemId())
                .totalAmount(cartItem.getAmount())
                .itemId(form.getItemId())
                .itemDetailId(form.getItemDetailId())
                .build();
        return res;

    }

    @Transactional
    public CartItemForm deleteItem(Long clientId, Long detailId) throws NoSuchEntityExceptions {
        CartItem item = findCartItem(clientId, detailId);
        item.disconnectToCart();

        CartItemForm res = CartItemForm.builder()
            .itemName(item.getItem().getItem().getItemName())
            .itemDetailId(item.getItem().getItemDetailId())
//            .itemId(item.getItem().getItem().getItemId())
            .build();

        return res;
    }

    @Transactional
    public boolean clearCart(Long clientId) throws NoSuchEntityExceptions {
        repository.findCartByClientId(clientId).ifPresent(repository::delete);
//        cart.getCartItems().forEach(CartItem::disconnectToCart);

//        repository.save(cart);

//        CartForm.Response response = mapper.cartToResponse(cart);
        return true;
    }

    private CartItem findCartItem(Long clientId, Long detailId) throws NoSuchEntityExceptions {
        Cart cart = repository.findCartByClientId(clientId)
            .orElseThrow(() -> new NoSuchEntityExceptions("카트가 비어있습니다"));
        Set<CartItem> items = cart.getCartItems();

        CartItem item = items.stream().filter(i -> i.getItem().getItemDetailId().equals(detailId))
            .findFirst().orElseThrow(() -> new NoSuchEntityExceptions("아이템이 없습니다"));
        return item;
    }

    @Transactional
    public CartItemForm updateAmountItem(Long clientId, Long detailId, int amount)
        throws NoSuchEntityExceptions {
        CartItem item = findCartItem(clientId, detailId);
        item.updateAmount(amount);
        CartItemForm res = mapper.cartItemToDto(item);
        return res;
    }

    @Transactional
    public CartItemForm addAmountItem(Long clientId, Long detailId, int amount)
        throws NoSuchEntityExceptions {
        CartItem item = findCartItem(clientId, detailId);

        item.addAmount(amount);

        return mapper.cartItemToDto(item);

    }

    @Transactional
    public CartItemForm subtractItem(Long clientId, Long detailId, Long itemId, int amount)
        throws NoSuchEntityExceptions {
        CartItem item = findCartItem(clientId, detailId);

        item.subtractAmount(amount);

        CartItemForm res = mapper.cartItemToDto(item);
        return res;

    }

    @Transactional
    public CartForm.Response getCart(Long clientId) {
        Cart cart = repository.findCartByClientId(clientId).orElseGet(Cart::new);

        cart.setTotalAmount();
        cart.setTotalPrice();
        cart.getCartItems().forEach(i -> System.out.println("i.getoption() = " + i.getItem().getOptionName()));
        Set<CartItemForm> items = getItemDetailForms(cart);
        items.forEach(i -> System.out.println("i.getItemName() = " + i.getItemName()));
        CartForm.Response r = mapper.cartToResponse(cart);
        CartForm.Response response
            = CartForm.Response.builder()
            .cartId(cart.getCartId())
            .cartItems(items)
//                 .cartItems(cart.getCartItems())
            .totalPrice(cart.getTotalPrice())
            .totalAmount(cart.getTotalAmount())
            .build();

        return r;

    }

//    @Transactional
    private Set<CartItemForm> getItemDetailForms(Cart cart) {
        return cart.getCartItems()
            .stream().map(mapper::cartItemToDto)
//                .stream().map(i -> mapper.cartItemToDto(i.getItem(), i.getItem().getItem(), i.getAmount()))
            .collect(Collectors.toCollection(HashSet::new));
    }
}
