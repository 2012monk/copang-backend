package com.alconn.copang.cart;

import com.alconn.copang.client.ClientRepo;
import com.alconn.copang.exceptions.NoSuchEntityExceptions;
import com.alconn.copang.exceptions.NoSuchUserException;
import com.alconn.copang.item.ItemDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CartService {

    private final CartRepository repository;

    private final ClientRepo clientRepo;

    private final CartMapper mapper;

    public CartForm.Response addCartItem(Long clientId, CartForm.Add form) throws NoSuchUserException {
        clientRepo.findById(clientId).orElseThrow(NoSuchUserException::new);

        Cart cart = repository.findCartByClientId(clientId).orElseGet(() ->
                Cart.builder().clientId(clientId).build());

        CartItem cartItem = cart.getCartItems().stream()
                .filter(i -> i.getItem().getItemDetailId().equals(form.getItemDetailId()))
                .collect(Collectors.toList()).stream().findFirst().orElseGet(() ->
                        CartItem.builder()
                                .item(ItemDetail.builder().itemDetailId(form.getItemDetailId()).build())
                                .amount(form.getAmount())
                                .build()
                );

//        CartItem cartItem = CartItem.builder()
//                .item(ItemDetail.builder().itemDetailId(form.getItemDetailId()).build())
//                .amount(form.getAmount())
//                .build();
        cart.addCartItem(cartItem);
        repository.save(cart);



//        Set<ItemDetailForm> items = getItemDetailForms(cart);

        CartForm.Response response =
                CartForm.Response.builder()
                        .cartId(cart.getCartId())
                        .cartItemId(cartItem.getCartItemId())
                        .totalAmount(cartItem.getAmount())
                        .itemId(form.getItemId())
                        .itemDetailId(form.getItemDetailId())
                        .build();
        return response;

    }

    public CartItemForm deleteItem(Long clientId, Long detailId) throws NoSuchEntityExceptions {
        CartItem item = findCartItem(clientId, detailId);
        item.disconnectToCart();

        CartItemForm res = CartItemForm.builder()
                .itemName(item.getItem().getItem().getItemName())
                .itemDetailId(item.getItem().getItemDetailId())
                .itemId(item.getItem().getItem().getItemId())
                .build();

        return res;
    }

    public CartForm.Response clearCart(Long clientId) throws NoSuchEntityExceptions {
        Cart cart = repository.findCartByClientId(clientId).orElseThrow(() -> new NoSuchEntityExceptions("카트가 비어있습니다"));

        cart.getCartItems().forEach(CartItem::disconnectToCart);

        CartForm.Response response = mapper.cartToResponse(cart);
        return response;
    }

    private CartItem findCartItem(Long clientId, Long detailId) throws NoSuchEntityExceptions {
        Cart cart = repository.findCartByClientId(clientId).orElseThrow(() -> new NoSuchEntityExceptions("카트가 비어있습니다"));
        Set<CartItem> items = cart.getCartItems();

        CartItem item = items.stream().filter(i -> i.getItem().getItemDetailId().equals(detailId))
                .findFirst().orElseThrow(() -> new NoSuchEntityExceptions("아이템이 없습니다"));
        return item;
    }

    public CartItemForm updateAmountItem(Long clientId, Long detailId, int amount) throws NoSuchEntityExceptions {
        CartItem item = findCartItem(clientId, detailId);
        item.updateAmount(amount);
        return mapper.cartItemToDto(item);
    }

    @Transactional
    public CartItemForm addAmountItem(Long clientId, Long detailId, int amount) throws NoSuchEntityExceptions {
        CartItem item = findCartItem(clientId, detailId);

        item.addAmount(amount);

        return mapper.cartItemToDto(item);

    }

    @Transactional
    public CartItemForm subtractItem(Long clientId, Long detailId, Long itemId, int amount) throws NoSuchEntityExceptions {
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
        Set<CartItemForm> items = getItemDetailForms(cart);
        CartForm.Response response
                 = CartForm.Response.builder()
                 .cartId(cart.getCartId())
                 .cartItems(items)
//                 .cartItems(cart.getCartItems())
                 .totalPrice(cart.getTotalPrice())
                .totalAmount(cart.getTotalAmount())
                 .build();

         return response;

    }

    private Set<CartItemForm> getItemDetailForms(Cart cart) {
        return cart.getCartItems()
                .stream().map(mapper::cartItemToDto)
//                .stream().map(i -> mapper.cartItemToDto(i.getItem(), i.getItem().getItem(), i.getAmount()))
                .collect(Collectors.toCollection(HashSet::new));
    }
}
