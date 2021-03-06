package com.alconn.copang.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.alconn.copang.address.Address;
import com.alconn.copang.address.AddressRepository;
import com.alconn.copang.auth.AccessTokenContainer;
import com.alconn.copang.auth.LoginToken;
import com.alconn.copang.client.Client;
import com.alconn.copang.client.ClientRepo;
import com.alconn.copang.client.ClientService;
import com.alconn.copang.client.Role;
import com.alconn.copang.exceptions.InvalidTokenException;
import com.alconn.copang.exceptions.LoginFailedException;
import com.alconn.copang.exceptions.NoSuchEntityExceptions;
import com.alconn.copang.exceptions.UnauthorizedException;
import com.alconn.copang.exceptions.ValidationException;
import com.alconn.copang.item.Item;
import com.alconn.copang.item.ItemDetail;
import com.alconn.copang.item.ItemDetailRepository;
import com.alconn.copang.item.ItemDetailService;
import com.alconn.copang.item.ItemRepository;
import com.alconn.copang.order.dto.OrderForm;
import com.alconn.copang.order.dto.OrderForm.Response;
import com.alconn.copang.order.dto.OrderItemForm;
import com.alconn.copang.order.dto.ReturnOrderForm;
import com.alconn.copang.order.dto.SellerOrderForm;
import com.alconn.copang.order.dto.SellerOrderForm;
import com.alconn.copang.payment.ImpPaymentInfo;
import com.alconn.copang.payment.PaymentService;
import com.alconn.copang.seller.Seller;
import com.alconn.copang.seller.SellerRepository;
import com.alconn.copang.shipment.LogisticCode;
import com.alconn.copang.shipment.ShipmentForm;
import com.alconn.copang.utils.TestUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

//@Disabled
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
    private ClientService clientService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ItemDetailRepository itemDetailRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private TestUtils utils;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private SellerOrderService sellerOrderService;
    @Test
    void listClient() {

    }

    @DisplayName("?????? ????????? ????????????")
    @Transactional
    @Test
    void creatOrderServiceTest() throws Exception {

        // setup
        objectMapper.writerWithDefaultPrettyPrinter();
        Client client = Client.builder()
//            .clientId(1L)
            .username("test@testclient.com")
            .password("1234")
            .phone("010-9090-8989")
            .role(Role.CLIENT)
            .description("????????????")
            .build();

        Address address = getAddress(client);

        repo.save(client);
        manager.flush();
        manager.clear();
        //  Client, Address ??????
        addressRepository.save(address);

        // ItemDetail, Item ??????
        List<ItemDetail> details = new ArrayList<>();
        Item item = Item.builder()
            .itemName("name123")
            .build();
        itemRepository.save(item);

        for (int i = 0; i < 4; i++) {
            ItemDetail detail = ItemDetail.builder()
                .stockQuantity(400)
                .mainImg("noimage")
                .optionName("??????")
                .optionValue("1KG")
                .price(5000)
                .build();
            details.add(detail);
        }
        details.forEach(d -> d.itemConnect(item));
//        detailService.itemDetailSaveList(details);
        itemDetailRepository.saveAll(details);
        manager.flush();
        manager.clear();
//        System.out.println("ItemDetail = " + objectMapper.writeValueAsString(details));

        Address rec = addressRepository.getById(address.getAddressId());
        assertNotNull(rec);
        assertEquals(rec.getAddress(), address.getAddress());
        List<OrderItemForm> orderItemForms = details
            .stream().map(
                i -> OrderItemForm.builder()
                    .itemDetailId(i.getItemDetailId())
                    .itemId(i.getItem().getItemId())
                    .itemName(i.getItem().getItemName())
                    .amount(3)
                    .build()
            ).collect(Collectors.toList());

        // Order ????????? ??????
        OrderForm.Create create = OrderForm.Create.builder()
            .addressId(address.getAddressId())
            .orderItems(orderItemForms)
            .totalAmount(12)
            .totalPrice(2034000)
            .build();

        System.out.println("objectMapper = Create form" + objectMapper.writeValueAsString(create));

        LoginToken loginToken = new LoginToken();

        ReflectionTestUtils.setField(loginToken, "username", client.getUsername());
        ReflectionTestUtils.setField(loginToken, "password", client.getPassword());

        AccessTokenContainer container = clientService.login(loginToken);

        // when
        ResultActions actions = this.mvc.perform(
            post("/api/orders")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + container.getAccess_token())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(create))
        );

        actions.andExpect(status().isCreated())
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$..totalPrice").value(create.getTotalPrice()))
            .andDo(print());

        OrderForm.Response o = this.service.placeOrder(create, client.getClientId());
        System.out
            .println("this.service.createOrder(create) = " + objectMapper.writeValueAsString(o));

        manager.flush();
        manager.clear();
//
//        OrderForm.Response selected = service.getOneOrder(o.getOrderId());
//        System.out.println("service.getOneOrder(o.getOrderId()) = " + objectMapper.writeValueAsString(selected));

        this.mvc.perform(
            get("/api/orders/{orderId}", o.getOrderId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + container.getAccess_token())
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").exists())
            .andDo(print());


    }

    @Transactional
    @Test
    void paymentTest()
        throws JsonProcessingException, InvalidTokenException, LoginFailedException, ValidationException, NoSuchEntityExceptions, UnauthorizedException, AccessDeniedException {
        objectMapper.writerWithDefaultPrettyPrinter();
        Client client = Client.builder()
//            .clientId(1L)
            .username("test@testclient.com")
            .password("1234")
            .phone("010-9090-8989")
            .role(Role.CLIENT)
            .description("????????????")
            .build();
        Seller seller = utils.getSeller();
        sellerRepository.save(seller);


        Address address = getAddress(client);

        repo.save(client);
        manager.flush();
        manager.clear();
        //  Client, Address ??????
        addressRepository.save(address);

        // ItemDetail, Item ??????
        Item item = Item.builder()
            .itemName("name123")
            .seller(seller)
            .build();
        itemRepository.save(item);

        ItemDetail detail = ItemDetail.builder()
            .stockQuantity(400)
            .mainImg("noimage")
            .optionName("??????")
            .optionValue("1KG")
            .price(1000)
            .build();
        detail.itemConnect(item);
        itemDetailRepository.save(detail);
        manager.flush();
        manager.clear();

        Address rec = addressRepository.getById(address.getAddressId());
        assertNotNull(rec);
        assertEquals(rec.getAddress(), address.getAddress());
        List<OrderItemForm> orderItemForms =
            Collections.singletonList(
                OrderItemForm.builder()
                    .itemDetailId(detail.getItemDetailId())
//                    .itemId(detail.getItem().getItemId())
//                    .itemName(detail.getItem().getItemName())
                    .amount(1)
                    .build());

        // Order ????????? ??????
        OrderForm.Create create = OrderForm.Create.builder()
            .addressId(address.getAddressId())
            .orderItems(orderItemForms)
//            .totalAmount(orderItemForms.stream().mapToInt(OrderItemForm::getAmount).sum())
//            .totalPrice(orderItemForms.stream().mapToInt(o -> o.getAmount() * details.get(0).getPrice()).sum())
            .build();

        System.out.println("objectMapper = Create form" + objectMapper.writeValueAsString(create));

        LoginToken loginToken = new LoginToken();

        ReflectionTestUtils.setField(loginToken, "username", client.getUsername());
        ReflectionTestUtils.setField(loginToken, "password", client.getPassword());

        AccessTokenContainer container = clientService.login(loginToken);

        OrderForm.Response response = service.readyOrder(create, client.getClientId());

        OrderForm.Response res = service.getOneOrder(response.getOrderId());
        System.out.println("res.getTotalPrice() = " + res.getTotalPrice());
        Long id = response.getOrderId();

        String impId = "imp_828634498901";

//        ImpPaymentInfo impPaymentInfo = paymentService.validatePayment(impId, id);

        manager.flush();
        manager.clear();

        Response response1 = service.orderPayment(impId, client.getClientId(), id);
//        Response response1 = service.orderPayment(impId, 2L, id);

        assertNotNull(response1);
        assertEquals(client.getClientId(), response1.getClient().getClientId());

        manager.flush();
        manager.clear();

        List<SellerOrderForm.Response> l = service.getOrdersBySeller(seller.getClientId());
        assertEquals(1, l.size());
    }

    @Transactional
    @Test
    void orderBySeller()
        throws Exception {
        objectMapper.writerWithDefaultPrettyPrinter();

        Seller seller = utils.getSeller();

        sellerRepository.save(seller);
        Client client = Client.builder()
//            .clientId(1L)
            .username("test@testclient.com")
            .password("1234")
            .phone("010-9090-8989")
            .role(Role.CLIENT)
            .description("????????????")
            .build();

        Address address = getAddress(client);

        repo.save(client);
        manager.flush();
        manager.clear();
        //  Client, Address ??????
        addressRepository.save(address);

        // ItemDetail, Item ??????
        Item item = Item.builder()
            .itemName("name123")
            .seller(seller)
            .build();
        itemRepository.save(item);

        ItemDetail detail = ItemDetail.builder()
            .stockQuantity(400)
            .mainImg("noimage")
            .optionName("??????")
            .optionValue("1KG")
            .price(1000)
            .build();

        ItemDetail detail1 = ItemDetail.builder()
            .stockQuantity(400)
            .mainImg("noimage")
            .optionName("??????")
            .optionValue("1KG")
            .price(1000)
            .build();
        detail.itemConnect(item);
        detail1.itemConnect(item);
        itemDetailRepository.save(detail);
        itemDetailRepository.save(detail1);
        manager.flush();
        manager.clear();

        Address rec = addressRepository.getById(address.getAddressId());
        assertNotNull(rec);
        assertEquals(rec.getAddress(), address.getAddress());
        List<OrderItemForm> orderItemForms =
            Collections.singletonList(
                OrderItemForm.builder()
                    .itemDetailId(detail.getItemDetailId())
//                    .itemId(detail.getItem().getItemId())
//                    .itemName(detail.getItem().getItemName())
                    .amount(1)
                    .build());

        // Order ????????? ??????
        OrderForm.Create create = OrderForm.Create.builder()
            .addressId(address.getAddressId())
            .orderItems(orderItemForms)
//            .totalAmount(orderItemForms.stream().mapToInt(OrderItemForm::getAmount).sum())
//            .totalPrice(orderItemForms.stream().mapToInt(o -> o.getAmount() * details.get(0).getPrice()).sum())
            .build();

        Long oid1 = service.placeOrder(create, client.getClientId()).getOrderId();

        manager.flush();
        manager.clear();
        service.setSellerOrder(oid1);

        Long oid2 = service.placeOrder(create, client.getClientId()).getOrderId();

        manager.flush();
        manager.clear();
        service.setSellerOrder(oid2);

        List<OrderItemForm> items = new ArrayList<OrderItemForm>(){{
            add(OrderItemForm.builder().itemDetailId(detail.getItemDetailId()).amount(2).build());
            add(OrderItemForm.builder().itemDetailId(detail1.getItemDetailId()).amount(1).build());
        }};

        OrderForm.Create create1 = OrderForm.Create.builder()
            .addressId(address.getAddressId())
            .orderItems(items)
            .build();

        Response response = service.placeOrder(create1, client.getClientId());


        manager.flush();
        manager.clear();

        service.setSellerOrder(response.getOrderId());

        manager.flush();
        manager.clear();
//        List<Response> sellers = service.getOrdersBySeller(seller.getClientId());
//
//        assertEquals(1, sellers.size());

//        List<OrderItem> orders = orderRepository.joinTest(seller.getClientId());
        List<SellerOrder> orders = service.getSellers(seller.getClientId());

        System.out.println("orders.size() = " + orders.size());
        assertEquals(3, orders.size());

        List<SellerOrderForm.Response> orderForms = service.getOrdersBySeller(seller.getClientId());

        orderForms.forEach(o -> {
            try {
                System.out.println("objectMapper\n            .writeValueAsString(o) = " + objectMapper
                    .writeValueAsString(o));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });

        List<SellerOrderForm.Response> sellerOrder = sellerOrderService
            .getSellerOrder(seller.getClientId());

        assertEquals(1, sellerOrder.size());
        for (SellerOrderForm.Response o : sellerOrder) {
            try {
                System.out.println(" sellerOrders " + objectMapper.writeValueAsString(o));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        List<ShipmentForm.Request> requests = new ArrayList<>();
        sellerOrder.forEach(o ->
            o.getOrderItems().forEach(
                i -> requests.add(
                    ShipmentForm.Request.builder()
                    .orderItemId(i.getOrderItemId())
                    .trackingNumber("123333")
                        .logisticCode(LogisticCode.EPOST)
                    .build()
                )
            ));

//        for (ShipmentForm.Request r: requests) {
//            System.out.println("objectMapper = " + objectMapper.writeValueAsString(r));
//        }
        String json = "{"
            + "      \"orderItemId\": 6031,"
            + "      \"trackingNumber\": \"6865113737890\","
            + "      \"logisticCode\": \"EPOST\""
            + "}";
//        json = "[{\n"
//            + "      \"orderItemId\": 6031,\n"
//            + "      \"trackingNumber\": \"6865113737890\",\n"
//            + "      \"logisticCode\": \"EPOST\"\n"
//            + "}]";
//        service.placeShipment(requests, seller.getClientId());
//
//        manager.flush();
//        manager.clear();
//
//        sellerOrder = sellerOrderService
//            .getSellerOrder(seller.getClientId());
//
//        assertEquals(1, sellerOrder.size());
//        for (SellerOrderForm.Response o : sellerOrder) {
//            try {
//                System.out.println(" sellerOrders " + objectMapper.writeValueAsString(o));
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//            }
//        }

        mvc.perform(
            post("/api/orders/shipment")
            .content(json)
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, utils.genHeader(client))
        )
            .andDo(print());

    }

    private Address getAddress(Client client) {
        return Address.builder()
//            .addressId(1L)
            .receiverPhone("1")
            .receiverName("3")
            .addressName("41")
            .preRequest("4124")
            .address("??????")
            .detail("??????1123")
            .client(client)
            .build();
    }

    private void setUpClient() {
        objectMapper.writerWithDefaultPrettyPrinter();

        Seller seller = utils.getSeller();

        sellerRepository.save(seller);
        Client client = Client.builder()
            .username("test@testclient.com")
            .password("1234")
            .phone("010-9090-8989")
            .role(Role.CLIENT)
            .description("????????????")
            .build();

        Address address = getAddress(client);

        repo.save(client);
        addressRepository.save(address);

    }

    @Transactional
    @Test
    void searchTest() throws NoSuchEntityExceptions {
        Client client = utils.generateRealClient();
        Seller seller = utils.getSeller();
        Address address = getAddress(client);
        sellerRepository.save(seller);
        repo.save(client);
        addressRepository.save(address);
        String[] names = new String[]{
            "??????", "?????????", "?????????", "??????"
        };

        List<ItemDetail> items = Arrays.stream(names).map(
            n -> ItemDetail.builder()
                .item(
                    Item.builder()
                        .itemName(n)
                        .seller(seller)
                        .build()
                )
                .optionName("??????")
                .optionValue("??????")
                .price(1200)
                .stockQuantity(2)
                .mainImg("123")
                .build()
        ).collect(Collectors.toList());
        items.forEach(i -> i.itemConnect(i.getItem()));
        items.forEach(i -> itemRepository.save(i.getItem()));
        itemDetailRepository.saveAll(items);


        Orders orders =
            Orders.builder()
            .client(client)
            .address(address)
                .orderItemList(
                    items.stream().map(i -> OrderItem.builder().itemDetail(i).amount(1).build())
                    .collect(Collectors.toList())
                )
            .build();

        orders.connectOrderItems();
        orderRepository.save(orders);

        service.setSellerOrder(orders.getOrderId());


        manager.flush();
        manager.clear();


        List<SellerOrder> sellers = service.getSellers(seller.getClientId());
        assertEquals(1, sellers.size());

//        List<SellerOrder> list = sellerOrderService.searchByKeyWords(seller.getClientId(), "?????? ?????????");
        List<SellerOrderForm.Response> list = sellerOrderService.searchByKeyWords(seller.getClientId(), "abb df");
        list.forEach(s -> {
            try {
                System.out.println("objectMapper = " + objectMapper.writeValueAsString(s));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
    }


    @Disabled
    @Transactional
    @Test
    void returnTest()
        throws NoSuchEntityExceptions, AccessDeniedException, ValidationException, UnauthorizedException, JsonProcessingException {
        Client client = utils.generateRealClient();
        Seller seller = utils.getSeller();
        Address address = getAddress(client);
        sellerRepository.save(seller);
        repo.save(client);
        addressRepository.save(address);
        String[] names = new String[]{
            "??????", "?????????", "?????????", "??????"
        };

        List<ItemDetail> items = Arrays.stream(names).map(
            n -> ItemDetail.builder()
                .item(
                    Item.builder()
                        .itemName(n)
                        .seller(seller)
                        .build()
                )
                .optionName("??????")
                .optionValue("??????")
                .price(50)
                .stockQuantity(2)
                .mainImg("123")
                .build()
        ).collect(Collectors.toList());
        items.forEach(i -> i.itemConnect(i.getItem()));
        items.forEach(i -> itemRepository.save(i.getItem()));
        itemDetailRepository.saveAll(items);


        Orders orders =
            Orders.builder()
                .client(client)
                .address(address)
                .orderItemList(
                    items.stream().map(i -> OrderItem.builder().itemDetail(i).amount(1).build())
                        .collect(Collectors.toList())
                )
                .build();

        orders.connectOrderItems();
        orderRepository.save(orders);

        service.setSellerOrder(orders.getOrderId());

        OrderForm.Create create=
            OrderForm.Create.builder()
            .orderItems(items.stream().map(i ->
                OrderItemForm.builder()
            .amount(1)
            .itemDetailId(i.getItemDetailId())
            .build())
                .collect(Collectors.toList())
            )
            .addressId(address.getAddressId())
            .build();

        String uid = "imp_067362900733";
        Response response = service.readyOrder(create, client.getClientId());
        manager.flush();
        manager.clear();

        Response response1 = service.orderPayment(uid, client.getClientId(), response.getOrderId());

        Orders o1 = orderRepository.getById(response1.getOrderId());

        manager.flush();
        manager.clear();
        System.out.println("o1.getImpPaymentInfo().getImp_uid() = " + o1.getImpPaymentInfo().getImp_uid());

        ReturnOrderForm.Request request =

            ReturnOrderForm.Request.builder()
            .pickupRequest("??????")
            .returnReason("???")
            .amount(1)
            .addressId(address.getAddressId())
            .build();
        ReturnOrderForm.Response response2 = service
            .receiptReturnOrder(request, response1.getOrderItems().get(0).getOrderItemId(),
                client.getClientId());
        System.out.println("objectMapper.writeValueAsString(response2) = " + objectMapper
            .writeValueAsString(response2));

        manager.flush();
        manager.clear();

        List<ReturnOrderForm.Response> canceledItems = service
            .getCanceledItems(client.getClientId());

        canceledItems.forEach(c -> {
            try {
                System.out.println("objectMapper = " + objectMapper.writeValueAsString(c));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });

    }
}