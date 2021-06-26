package com.alconn.copang.order;

import com.alconn.copang.client.Client;
import com.alconn.copang.client.ClientRepo;
import com.alconn.copang.client.Role;
import com.alconn.copang.item.Item;
import com.alconn.copang.item.ItemDetail;
import com.alconn.copang.item.ItemDetailService;
import com.alconn.copang.item.ItemRepository;
import com.alconn.copang.order.dto.OrderForm;
import com.alconn.copang.order.dto.OrderItemForm;
import com.alconn.copang.order.mapper.OrderItemMapper;
import com.alconn.copang.order.mapper.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

//@ExtendWith(MockitoExtension.class)
@SpringBootTest
class OrderServiceTest {

//    @InjectMocks
    @Autowired
    private OrderService service;

//    @Mock
    @Autowired
    private OrderRepository repository;

//    @Mock
    @Autowired
    private OrderItemMapper mapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClientRepo repo;

    @Autowired
    private ItemRepository itemRepository;
//    = new ObjectMapper();

//    @Mock
    @Autowired
    private ItemDetailService detailService;

    @Transactional
    @Test
    void name() throws JsonProcessingException {

        objectMapper.writerWithDefaultPrettyPrinter();
        Client client = Client.builder()
                .clientId(1L)
                .username("test@testclient.com")
                .password("1234")
                .phone("010-9090-8989")
                .role(Role.CLIENT)
                .description("쿠팡노예")
                .build();

        repo.save(client);
        List<OrderItem> items = new ArrayList<>();

        List<ItemDetail> details = new ArrayList<>();
        Item item = Item.builder()
                .itemName("name123")
                .mainImg("noimage")
                .build();



        for (int i=0;i<4; i++){
            ItemDetail detail = ItemDetail.builder()
                    .stockQuantity(400)
                    .price(5000)
                    .item(item)
                    .build();
            details.add(detail);
            items.add(OrderItem.builder().itemDetail(detail).amount(50).build());
        }
        itemRepository.saveAndFlush(item);
        System.out.println("objectMapper = " + objectMapper.writeValueAsString(detailService.itemDetailSaveList(details)));

        List<OrderItemForm> orderItemForms = details.stream().map(
                i -> OrderItemForm.builder()
                .itemDetailId(i.getId())
                .itemId(i.getItem().getId())
                .itemName(i.getItem().getItemName())
                .amount(23)
                .build()
        ).collect(Collectors.toList());





        OrderForm.Create create = OrderForm.Create.builder()
                .clientId(1L)
                .addressId(1L)
                .orderItems(orderItemForms)
                .orderItemList(items)
                .build();

        System.out.println("objectMapper = " + objectMapper.writeValueAsString(create));

        System.out.println("this.service.createOrder(create) = " + objectMapper.writeValueAsString(this.service.createOrder(create)));
    }
}