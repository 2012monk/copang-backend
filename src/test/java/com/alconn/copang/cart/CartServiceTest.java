package com.alconn.copang.cart;

import com.alconn.copang.client.Client;
import com.alconn.copang.exceptions.NoSuchEntityExceptions;
import com.alconn.copang.exceptions.NoSuchUserException;
import com.alconn.copang.item.Item;
import com.alconn.copang.item.ItemDetail;
import com.alconn.copang.item.ItemDetailService;
import com.alconn.copang.utils.TestUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CartServiceTest {

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

    Client client;

    Item item;

    ItemDetail detail;

    @Transactional
    @BeforeEach
    void setUp() {
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
    }

    @Transactional
    @Test
    void addCartItem() throws NoSuchUserException, JsonProcessingException {

        CartForm.Add add =
                CartForm.Add.builder()
                .itemDetailId(detail.getItemDetailId())
                .itemId(detail.getItem().getItemId())
                .amount(3)
                .build();

        CartForm.Response response = service.addCartItem(client.getClientId(), add);
//        System.out.println("service = " + mapper.writeValueAsString(response));


        m.flush();
        m.clear();

        CartForm.Response cart = service.getCart(client.getClientId());

        System.out.println("mapper = " + mapper.writeValueAsString(cart));

    }


    @Test
    void addItemAmount() throws NoSuchEntityExceptions, JsonProcessingException {
        CartForm.Add add =
                CartForm.Add.builder()
                        .itemDetailId(detail.getItemDetailId())
                        .itemId(detail.getItem().getItemId())
                        .amount(3)
                        .build();

        service.addCartItem(client.getClientId(), add);

        CartItemForm result = service.addAmountItem(client.getClientId(), detail.getItemDetailId(), 10);

        CartForm.Response response =
                service.getCart(client.getClientId());


        System.out.println("mapper.writeValueAsString(response) = " + mapper.writeValueAsString(response));

        assertEquals(result.getAmount(), 13);
    }
}