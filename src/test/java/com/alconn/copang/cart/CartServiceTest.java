package com.alconn.copang.cart;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.alconn.copang.client.Client;
import com.alconn.copang.client.ClientRepo;
import com.alconn.copang.exceptions.NoSuchEntityExceptions;
import com.alconn.copang.exceptions.NoSuchUserException;
import com.alconn.copang.item.Item;
import com.alconn.copang.item.ItemDetail;
import com.alconn.copang.item.ItemDetailService;
import com.alconn.copang.utils.TestUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class CartServiceTest {

    static boolean init = false;
    @Autowired
    CartService service;
    @Autowired
    TestUtils utils;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    ItemDetailService detailService;
    @Autowired
    EntityManager m;
    @Autowired
    ClientRepo repo;
    Client client;
    Item item;
    ItemDetail detail;

    @Transactional
    @BeforeEach
    void setUp() {
//        repo.deleteAll();
        System.out.println("init = " + init);
        client = utils.generateRealClient();

        item = Item.builder().itemName("고구마").build();

        detail = ItemDetail.builder()
            .optionName("박스")
            .optionValue("5KG")
            .item(item)
            .mainImg("no image")
            .price(56900)
            .build();
        detailService.itemDetailSave(detail);
        init = true;
    }

    @Transactional
    @Test
    void addCartItem() throws NoSuchUserException, JsonProcessingException {
        repo.save(client);
        CartForm.Add add =
            CartForm.Add.builder()
                .itemDetailId(detail.getItemDetailId())
                .itemId(detail.getItem().getItemId())
                .amount(3)
                .build();

        service.addCartItem(client.getClientId(), add);
//        System.out.println("service = " + mapper.writeValueAsString(response));

        m.flush();
        m.clear();

        CartForm.Response cart = service.getCart(client.getClientId());

        System.out.println("mapper = " + mapper.writeValueAsString(cart));

    }


    @Transactional
    @Test
    void addItemAmount() throws NoSuchEntityExceptions, JsonProcessingException {
        repo.save(client);
        CartForm.Add add =
            CartForm.Add.builder()
                .itemDetailId(detail.getItemDetailId())
                .itemId(detail.getItem().getItemId())
                .amount(3)
                .build();

        System.out.println("detail = " + detail.getOptionName());
        service.addCartItem(client.getClientId(), add);

        CartItemForm result = service
            .addAmountItem(client.getClientId(), detail.getItemDetailId(), 10);

        m.flush();
        m.clear();

        CartForm.Response response =
            service.getCart(client.getClientId());

        System.out.println(
            "mapper.writeValueAsString(response) = " + mapper.writeValueAsString(response));

        assertEquals(result.getAmount(), 11);
    }

    @Transactional
    @Test
    void updateAmount() throws NoSuchEntityExceptions, JsonProcessingException {
        repo.save(client);
        CartForm.Add add =
            CartForm.Add.builder()
                .itemDetailId(detail.getItemDetailId())
                .itemId(detail.getItem().getItemId())
                .amount(3)
                .build();
        service.addCartItem(client.getClientId(), add);

        CartItemForm result = service
            .updateAmountItem(client.getClientId(), detail.getItemDetailId(), 60);

        m.flush();
        m.clear();

        CartForm.Response response =
            service.getCart(client.getClientId());

        System.out.println(
            "mapper.writeValueAsString(response) = " + mapper.writeValueAsString(response));

        assertEquals(response.getTotalAmount(), 60);
        assertEquals(response.getTotalPrice(), detail.getPrice() * 60);
    }

    @Transactional
    @Test
    void addAndAdd() throws NoSuchUserException, JsonProcessingException {
        repo.save(client);
        CartForm.Add add =
            CartForm.Add.builder()
                .itemDetailId(detail.getItemDetailId())
                .itemId(detail.getItem().getItemId())
                .amount(3)
                .build();

        service.addCartItem(client.getClientId(), add);

        m.flush();
        m.clear();
        // when

        service.addCartItem(client.getClientId(), add);

        m.flush();
        m.clear();

        CartForm.Response response =
            service.getCart(client.getClientId());

        System.out.println(
            "mapper.writeValueAsString(response) = " + mapper.writeValueAsString(response));

        assertEquals(response.getTotalAmount(), 2);
        assertEquals(response.getTotalPrice(), detail.getPrice() * 2);
    }

    @Transactional
    @Test
    void clearCart() throws NoSuchEntityExceptions {
        repo.save(client);
        CartForm.Add add =
            CartForm.Add.builder()
                .itemDetailId(detail.getItemDetailId())
                .itemId(detail.getItem().getItemId())
                .amount(3)
                .build();

        service.addCartItem(client.getClientId(), add);

        Item item = Item.builder().itemName("고구마").build();

        ItemDetail detail = ItemDetail.builder()
            .optionName("박스")
            .optionValue("5KG")
            .item(item)
            .mainImg("no image")
            .price(56900)
            .build();
        detailService.itemDetailSave(detail);

        add =
            CartForm.Add.builder()
                .itemDetailId(detail.getItemDetailId())
                .itemId(detail.getItem().getItemId())
                .amount(3)
                .build();

        service.addCartItem(client.getClientId(), add);


        m.flush();
        m.clear();

        service.clearCart(client.getClientId());

        m.flush();
        m.clear();

        CartForm.Response response =
            service.getCart(client.getClientId());

        assertEquals(response.getCartItems().size(), 0);
    }
}