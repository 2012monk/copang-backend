package com.alconn.copang.order;

import com.alconn.copang.address.Address;
import com.alconn.copang.address.AddressRepository;
import com.alconn.copang.client.Client;
import com.alconn.copang.client.ClientRepo;
import com.alconn.copang.client.Role;
import com.alconn.copang.common.EntityPriority;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class OrderServiceTest {

    //    @InjectMocks
    @Autowired
    private OrderService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClientRepo repo;

    @Autowired
    private ItemRepository itemRepository;

    //    @Mock
    @Autowired
    private ItemDetailService detailService;

    @Autowired
    private EntityManager manager;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private MockMvc mvc;

    @DisplayName("주문 요청이 저장된다")
    @Transactional
    @Test
    void creatOrderServiceTest() throws JsonProcessingException {

        // TODO itemdetail 저장과 select 시에 detail mapping 안됨
        objectMapper.writerWithDefaultPrettyPrinter();
        Client client = Client.builder()
                .clientId(1L)
                .username("test@testclient.com")
                .password("1234")
                .phone("010-9090-8989")
                .role(Role.CLIENT)
                .description("쿠팡노예")
                .build();

        Address address = Address.builder()
                .addressId(1L)
                .receiverPhone("1")
                .receiverName("3")
                .addressName("41")
                .preRequest("4124")
                .client(client)
                .build();
//                new Address(1L,
//                        "좌동 123", "철원 1번지", "010-9090-8989", "문앞요", client, EntityPriority.PRIMARY);

        //  Client, Address 생성
        repo.save(client);
        addressRepository.save(address);

        // ItemDetail, Item 생성
        List<ItemDetail> details = new ArrayList<>();
        Item item = Item.builder()
                .itemName("name123")
                .mainImg("noimage")
                .build();
        itemRepository.save(item);

        for (int i = 0; i < 4; i++) {
            ItemDetail detail = ItemDetail.builder()
                    .stockQuantity(400)
                    .detailImg("noimage")
                    .option("1KG")
                    .price(5000)
                    .build();
            details.add(detail);
        }
        detailService.itemDetailSaveList(details);
        details.forEach(d -> d.setItem(item));
        manager.flush();
        manager.clear();
//        System.out.println("ItemDetail = " + objectMapper.writeValueAsString(details));


        List<OrderItemForm> orderItemForms = details
                .stream().map(
                        i -> OrderItemForm.builder()
                                .itemDetailId(i.getId())
                                .itemId(i.getItem().getId())
                                .itemName(i.getItem().getItemName())
                                .amount(3)
                                .build()
                ).collect(Collectors.toList());


        // Order 요청폼 작성
        OrderForm.Create create = OrderForm.Create.builder()
                .clientId(1L)
                .addressId(1L)
                .orderItems(orderItemForms)
                .totalAmount(12)
                .totalPrice(2034000)
                .build();

        System.out.println("objectMapper = Create form" + objectMapper.writeValueAsString(create));

        OrderForm.Response o = this.service.createOrder(create);
        System.out.println("this.service.createOrder(create) = " + objectMapper.writeValueAsString(o));

        manager.flush();
        manager.clear();

        OrderForm.Response selected = service.getOneOrder(o.getOrderId());
        System.out.println("service.getOneOrder(o.getOrderId()) = " + objectMapper.writeValueAsString(selected));
    }

}