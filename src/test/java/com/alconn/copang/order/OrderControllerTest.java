package com.alconn.copang.order;

import com.alconn.copang.address.AddressForm;
import com.alconn.copang.client.UserForm;
import com.alconn.copang.exceptions.InvalidTokenException;
import com.alconn.copang.exceptions.NoSuchEntityExceptions;
import com.alconn.copang.order.dto.OrderForm;
import com.alconn.copang.order.dto.OrderItemForm;
import com.alconn.copang.order.dto.ReturnOrderForm;
import com.alconn.copang.order.dto.SellerOrderForm;
import com.alconn.copang.shipment.ShipmentForm;
import com.alconn.copang.utils.TestUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.nio.file.AccessDeniedException;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.headers.RequestHeadersSnippet;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.alconn.copang.ApiDocumentUtils.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@SpringBootApplication(exclude = SecurityConfig.class)
//@WebMvcTest(OrderController.class)
//@ContextConfiguration(classes = SecurityConfig.class)
//@ImportAutoConfiguration(exclude = SecurityConfig.class)
//@SpringBootTest
//@AutoConfigureMockMvc(addFilters = false)
// ??????! Spring boot test without spring security config
//@SpringBootTest(classes = {OrderController.class}, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
//@ImportAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
//    @TestConfiguration
class OrderControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    OrderService service;

    @Autowired
    TestUtils utils;

    @Autowired
    ObjectMapper mapper;


    @WithMockUser(roles = "USER", password = "password")
    @Test
    void startOrder() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        UserForm.Response client =
                get????????????();

        AddressForm address = getAddressForm();

        List<OrderItemForm> orderItems = genOrderItems("?????????");


        OrderForm.Response response =
                OrderForm.Response.builder()
                        .orderId(1L)
                .client(client)
                .orderDate(LocalDateTime.now())
                .orderStatus(OrderStatus.READY)
                .address(address)
                .totalPrice(80000)
                .totalAmount(4)
                .orderItems(orderItems)
                .build();

        given(service.placeOrder(any(OrderForm.Create.class), eq(client.getClientId()))).willReturn(response);

        // when
        OrderForm.Create create = OrderForm.Create.builder()
                .addressId(1L)
                .orderItems(orderItems)
                .totalAmount(4)
                .totalPrice(80000)
                .build();

        System.out.println("service = " + mapper.writeValueAsString(service.placeOrder(create, client.getClientId())));

        String token = utils.genToken();

        System.out.println("token = " + token);
        this.mvc.perform(
                post("/api/orders")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(create))
                .characterEncoding("utf-8")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        )
        .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document(
                        "orders/{method-name}",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        getAuthHeader(),
                        getRequestFieldsSnippet(),
                        getResponseFieldsSnippet()
                ));




    }

    private AddressForm getAddressForm() {
        return AddressForm
                .builder()
                .addressId(1L)
                .detail("?????? 1??????")
                .address("??????")
                .receiverName("?????? ?????????????????????")
                .preRequest("???????????????")
                .receiverPhone("010-8989-9898")
                .build();
    }

    private UserForm.Response get????????????() {
        return UserForm.Response.builder()
                .clientId(1L)
                .username("test@testclient.com")
                .phone("010-9090-8989")
                .realName("????????????")
//                .role(Role.CLIENT)
                .build();
    }

    private List<OrderItemForm> genOrderItems(String name) {
        List<OrderItemForm> orderItems = new ArrayList<>();
        for (long i = 0; i < 4; i++) {
            orderItems.add(
                    OrderItemForm.builder()
                            .itemId(1L)
                            .itemDetailId(i + 2L)
                            .itemName(name)
                            .amount(1)
                            .price(20000)
                            .optionName("??????")
                            .optionValue((i + 1L) + "KG")
                            .unitTotal(2000)
                            .build()
            );
        }
        return orderItems;
    }

    private RequestFieldsSnippet getRequestFieldsSnippet() {
        return relaxedRequestFields(
                fieldWithPath("addressId").type(JsonFieldType.NUMBER).description("?????? ?????? ??????"),
                fieldWithPath("orderItems").type(JsonFieldType.ARRAY).description("?????? ????????? ?????????"),
                fieldWithPath("orderItems.[].itemId").type(JsonFieldType.NUMBER).description("????????? ?????????").optional(),
                fieldWithPath("orderItems.[].itemDetailId").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                fieldWithPath("orderItems.[].amount").type(JsonFieldType.NUMBER).description("?????? ????????????")
//                fieldWithPath("totalPrice").type(JsonFieldType.NUMBER).description("?????????"),
//                fieldWithPath("totalAmount").description(JsonFieldType.NUMBER).description("?????? ??? ??????"),
//                fieldWithPath("orderItems.[].itemName").type(JsonFieldType.STRING).description("?????????"),
//                fieldWithPath("orderItems.[].optionName").type(JsonFieldType.STRING).description("?????????").optional(),
//                fieldWithPath("orderItems.[].optionValue").type(JsonFieldType.STRING).description("?????????").optional(),
//                fieldWithPath("orderItems.[].price").type(JsonFieldType.NUMBER).description("??????").optional(),
//                fieldWithPath("orderItems.[].unitTotal").type(JsonFieldType.NUMBER).description("???????????? ?????? ??????")
        );
    }

    private ResponseFieldsSnippet getResponseFieldsSnippetList() {
        return relaxedResponseFields(
                fieldWithPath("message").type(JsonFieldType.STRING).description("?????? ?????????"),
                fieldWithPath("code").type(JsonFieldType.NUMBER).description("?????? ??????"),
                fieldWithPath("data").type(JsonFieldType.ARRAY).description("?????? ?????????"),
                fieldWithPath("data[].orderId").type(JsonFieldType.NUMBER).description("????????????"),
                fieldWithPath("data[].orderDate").type(JsonFieldType.STRING).description("????????????"),
                fieldWithPath("data[].orderStatus").type(JsonFieldType.STRING).description("????????????"),
                fieldWithPath("data[].totalPrice").type(JsonFieldType.NUMBER).description("?????????"),
                fieldWithPath("data[].totalAmount").description(JsonFieldType.NUMBER).description("?????? ??? ??????"),
                fieldWithPath("data[].address").type(JsonFieldType.OBJECT).description("????????????"),
                fieldWithPath("data[].address.addressId").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                fieldWithPath("data[].address.address").type(JsonFieldType.STRING).description("??????"),
                fieldWithPath("data[].address.detail").type(JsonFieldType.STRING).description("????????????"),
                fieldWithPath("data[].address.receiverPhone").type(JsonFieldType.STRING).description("???????????? ????????????"),
                fieldWithPath("data[].address.receiverName").type(JsonFieldType.STRING).description("???????????? ??????"),
                fieldWithPath("data[].address.preRequest").type(JsonFieldType.STRING).description("????????????"),
                fieldWithPath("data[].client").type(JsonFieldType.OBJECT).description("????????? ??????"),
                fieldWithPath("data[].client.clientId").type(JsonFieldType.NUMBER).description("????????? ?????????"),
                fieldWithPath("data[].client.username").type(JsonFieldType.STRING).description("????????? ?????????"),
                fieldWithPath("data[].client.phone").type(JsonFieldType.STRING).description("????????? ????????????"),
                fieldWithPath("data[].client.realName").type(JsonFieldType.STRING).description("????????? ??????"),
                fieldWithPath("data[].orderItems").type(JsonFieldType.ARRAY).description("?????? ????????? ?????????"),
                fieldWithPath("data[].orderItems.[].itemName").type(JsonFieldType.STRING).description("?????????"),
                fieldWithPath("data[].orderItems.[].itemId").type(JsonFieldType.NUMBER).description("????????? ?????????"),
                fieldWithPath("data[].orderItems.[].itemDetailId").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                fieldWithPath("data[].orderItems.[].optionName").type(JsonFieldType.STRING).description("?????????"),
                fieldWithPath("data[].orderItems.[].optionValue").type(JsonFieldType.STRING).description("?????????"),
                fieldWithPath("data[].orderItems.[].price").type(JsonFieldType.NUMBER).description("??????"),
                fieldWithPath("data[].orderItems.[].amount").type(JsonFieldType.NUMBER).description("?????? ????????????"),
                fieldWithPath("data[].orderItems.[].unitTotal").type(JsonFieldType.NUMBER).description("???????????? ?????? ??????")
        );
    }
    private ResponseFieldsSnippet getResponseFieldsSnippet() {
        return relaxedResponseFields(
            fieldWithPath("message").type(JsonFieldType.STRING).description("?????? ?????????"),
            fieldWithPath("code").type(JsonFieldType.NUMBER).description("?????? ??????"),
            fieldWithPath("data").type(JsonFieldType.OBJECT).description("?????? ?????????"),
            fieldWithPath("data.orderId").type(JsonFieldType.NUMBER).description("????????????"),
            fieldWithPath("data.orderDate").type(JsonFieldType.STRING).description("????????????"),
            fieldWithPath("data.orderStatus").type(JsonFieldType.STRING).description("????????????"),
            fieldWithPath("data.totalPrice").type(JsonFieldType.NUMBER).description("?????????"),
            fieldWithPath("data.totalAmount").description(JsonFieldType.NUMBER).description("?????? ??? ??????"),
            fieldWithPath("data.address").type(JsonFieldType.OBJECT).description("????????????"),
            fieldWithPath("data.address.addressId").type(JsonFieldType.NUMBER).description("?????? ?????????"),
            fieldWithPath("data.address.address").type(JsonFieldType.STRING).description("??????"),
            fieldWithPath("data.address.detail").type(JsonFieldType.STRING).description("????????????"),
            fieldWithPath("data.address.receiverPhone").type(JsonFieldType.STRING).description("???????????? ????????????"),
            fieldWithPath("data.address.receiverName").type(JsonFieldType.STRING).description("???????????? ??????"),
            fieldWithPath("data.address.preRequest").type(JsonFieldType.STRING).description("????????????"),
            fieldWithPath("data.client").type(JsonFieldType.OBJECT).description("????????? ??????"),
            fieldWithPath("data.client.clientId").type(JsonFieldType.NUMBER).description("????????? ?????????"),
            fieldWithPath("data.client.username").type(JsonFieldType.STRING).description("????????? ?????????"),
            fieldWithPath("data.client.phone").type(JsonFieldType.STRING).description("????????? ????????????"),
            fieldWithPath("data.client.realName").type(JsonFieldType.STRING).description("????????? ??????"),
            fieldWithPath("data.orderItems").type(JsonFieldType.ARRAY).description("?????? ????????? ?????????"),
            fieldWithPath("data.orderItems.[].itemName").type(JsonFieldType.STRING).description("?????????"),
            fieldWithPath("data.orderItems.[].itemId").type(JsonFieldType.NUMBER).description("????????? ?????????"),
            fieldWithPath("data.orderItems.[].itemDetailId").type(JsonFieldType.NUMBER).description("?????? ?????????"),
            fieldWithPath("data.orderItems.[].optionName").type(JsonFieldType.STRING).description("?????????"),
            fieldWithPath("data.orderItems.[].optionValue").type(JsonFieldType.STRING).description("?????????"),
            fieldWithPath("data.orderItems.[].price").type(JsonFieldType.NUMBER).description("??????"),
            fieldWithPath("data.orderItems.[].amount").type(JsonFieldType.NUMBER).description("?????? ????????????"),
            fieldWithPath("data.orderItems.[].unitTotal").type(JsonFieldType.NUMBER).description("???????????? ?????? ??????")
        );
    }





    @Test
    void getSellerOrder() throws Exception {
        Long orderId = 19082L;

        List<OrderItemForm> orderItemForms = new ArrayList<>();
        for (int i=0;i<2;i++){
            orderItemForms.add(
                OrderItemForm.builder()
                .optionName("??????")
                .optionValue(i + "KG")
                .itemDetailId((long) i)
                .itemId(1L)
                .price(2000)
                .itemName("??????")
                .amount(2)
                    .unitTotal(2000 * 2)
                .mainImg("no image")
                .build()
            );
        }

        UserForm.Response client =
            UserForm.Response.builder()
            .clientId(5L)
            .phone("010-9090-9090")
            .realName("?????????")
            .build();

        SellerOrderForm.Response response =
            SellerOrderForm.Response.builder()
            .orderDate(LocalDateTime.now())
            .orderItems(orderItemForms)
            .totalAmount(orderItemForms.stream().mapToInt(OrderItemForm::getAmount).sum())
            .totalPrice(orderItemForms.stream().mapToInt(o -> o.getPrice() * o.getAmount()).sum())
            .sellerOrderId(3L)
            .client(client)
            .address(getAddressForm())
            .build();

        given(this.service.getOrdersBySeller(eq(1L))).willReturn(Collections.singletonList(response));

        this.mvc.perform(
            get("/api/orders/seller")
            .header(HttpHeaders.AUTHORIZATION, utils.getSellerAuthHeader())
        ).andExpect(status().isOk())
            .andDo(print())
            .andDo(
                document(
                    "orders/{method-name}",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    getAuthHeader(),
                    relaxedResponseFields(
                        fieldWithPath("message").description("?????? ?????????"),
                        fieldWithPath("code").description("?????? ??????"),
                        fieldWithPath("data").description("???????????????")
                    ),
                    relaxedResponseFields(
                        beneathPath("data").withSubsectionId("data"),
                        fieldWithPath("sellerOrderId").type(JsonFieldType.NUMBER).description("????????? ????????? ??????"),
                        fieldWithPath("client").type(JsonFieldType.OBJECT).description("????????? ??????"),
                        fieldWithPath("orderItems").type(JsonFieldType.ARRAY).description("?????? ?????? ??????"),
                        fieldWithPath("orderDate").type(JsonFieldType.STRING).description("?????? ??????"),
                        fieldWithPath("address").type(JsonFieldType.OBJECT).description("????????? ??????"),
                        fieldWithPath("totalPrice").type(JsonFieldType.NUMBER).description("?????????"),
                        fieldWithPath("totalAmount").type(JsonFieldType.NUMBER).description("??? ??????")

                    )
                )
            );
    }

    @Test
    void cancelOrder() throws Exception {
        Long orderId = 19082L;
        OrderStatus status = OrderStatus.CANCELED;

        OrderForm.Response response =
                OrderForm.Response
                        .builder()
                        .orderId(orderId)
                        .orderStatus(status)
                        .build();

        given(service.cancelOrder(orderId)).willReturn(response);

        ResultActions result = this.mvc.perform(
                RestDocumentationRequestBuilders.
                        patch("/api/orders/{orderId}/cancel", orderId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + utils.genToken())
        );

        result.andExpect(status().isOk())
                .andDo(document("orders/{method-name}",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        getAuthHeader(),
                        pathParameters(
                                parameterWithName("orderId").description("????????????")
                        ),
//                        commonFields(),
                        relaxedResponseFields(
                                fieldWithPath("message").description("?????? ?????????"),
                                fieldWithPath("code").description("?????? ??????"),
                                fieldWithPath("data").description("???????????????"),
                                fieldWithPath("data.orderId").type(JsonFieldType.NUMBER).description("????????????"),
                                fieldWithPath("data.orderStatus").type(JsonFieldType.STRING).description("????????????")
                        )
                ));
    }

    private RequestHeadersSnippet getAuthHeader() {
        return requestHeaders(
                headerWithName(HttpHeaders.AUTHORIZATION).description("????????????")
        );
    }

    @Test
    void getOneOrder() throws Exception {
        OrderForm.Response response =
                getOrderResponse("?????????");

        given(service.getOneOrder(451L)).willReturn(response);

        ResultActions result = this.mvc.perform(
                RestDocumentationRequestBuilders.
                get("/api/orders/{orderId}", 451L)
                .header(HttpHeaders.AUTHORIZATION, "Bearer "+utils.genToken())
        );

        result.andExpect(status().isOk())
                .andDo(
                        document("orders/{method-name}",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                getAuthHeader(),
                                pathParameters(
                                        parameterWithName("orderId").description("????????????")
                                ),
                                getResponseFieldsSnippet()
                        )
                );
    }

    private OrderForm.Response getOrderResponse(String name) {
        return OrderForm.Response
                .builder()
                .orderId(451L)
                .orderStatus(OrderStatus.PROCEED)
                .address(getAddressForm())
                .client(get????????????())
                .orderItems(genOrderItems(name))
                .totalAmount(5)
                .totalPrice(540000)
                .orderDate(LocalDateTime.now())
                .build();
    }


    @Test
    void getClientOrderList() throws Exception {


        String[] names = new String[]{
                "??????", "?????????" , "??????"
        };
        List<OrderForm.Response> orderList = Arrays.stream(names).map(this::getOrderResponse
        ).collect(Collectors.toList());

        given(service.listOrderClient(1L)).willReturn(orderList);

        ResultActions actions = this.mvc.perform(
                RestDocumentationRequestBuilders.
                get("/api/orders/client")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + utils.genToken())

        );

        actions.andExpect(status().isOk())
                .andDo(document("orders/{method-name}",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        getAuthHeader(),
                        getResponseFieldsSnippetList()
                ));

    }

    @Test
    void exception() throws Exception {
        OrderItemForm form = OrderItemForm.builder()
            .itemName("name")
            .amount(1)
            .price(20000)
            .build();

        OrderForm.Create create=
            OrderForm.Create.builder()
            .orderItems(Collections.singletonList(form))
            .addressId(1L)
            .build();
        this.mvc.perform(
            post("/api/orders")
            .header(HttpHeaders.AUTHORIZATION, utils.genAuthHeader())
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(create))
        ).andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    void orderReadyTest() throws Exception {

        OrderForm.Response res =
            OrderForm.Response.builder()
            .orderId(1L)
            .build();

        OrderItemForm form = OrderItemForm.builder()
            .itemName("name")
            .amount(1)
            .price(20000)
            .build();

        OrderForm.Create create=
            OrderForm.Create.builder()
                .orderItems(Collections.singletonList(form))
                .addressId(1L)
                .build();

        given(this.service.readyOrder(any(OrderForm.Create.class), eq(1L))).willReturn(res);

        this.mvc.perform(
            post("/api/orders/ready")
            .header(HttpHeaders.AUTHORIZATION, utils.genAuthHeader())
            .content(mapper.writeValueAsString(create))
            .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isCreated())
            .andDo(print())
            .andDo(
                document(
                    "orders/{method-name}",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    getAuthHeader()
                )
            );

    }

    @Test
    void orderPayment() throws Exception {

        UserForm.Response client =
            get????????????();

        AddressForm address = getAddressForm();

        List<OrderItemForm> orderItems = genOrderItems("?????????");


        OrderForm.Response response =
            OrderForm.Response.builder()
                .orderId(1L)
                .client(client)
                .orderDate(LocalDateTime.now())
                .orderStatus(OrderStatus.READY)
                .address(address)
                .totalPrice(80000)
                .totalAmount(4)
                .orderItems(orderItems)
                .build();

        given(this.service.orderPayment(eq("imp-uid"), eq(response.getOrderId()), eq(1L))).willReturn(response);
        this.mvc.perform(
            RestDocumentationRequestBuilders.
            post("/api/orders/{orderId}/pay/{uid}", 1L, "imp-uid")
            .header(HttpHeaders.AUTHORIZATION, utils.genAuthHeader())
        )
            .andDo(print())
            .andDo(
                document(
                    "orders/{method-name}",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    getAuthHeader(),
                    pathParameters(
                        parameterWithName("uid").description("imp_uid ?????? ??????"),
                        parameterWithName("orderId").description("????????????")
                    )

                )
            );
    }


    @Test
    void returnOrder() throws Exception {

        ReturnOrderForm.Response response =
            ReturnOrderForm.Response.builder()
            .returnOrderId(1L)
            .cancelReceiptUrl("https://mockup-pg-web.kakao.com/v1/confirmation/p/T2918109671627384610/d2d665c7f2895baa7837f79eeda69be759fe35a58f4757ce3045f5983dba485d")
            .pickupRequest("??????")
                .returnReason("???")
            .address(utils.getMockAddress())
            .returnAmount(1)
            .returnPrice(4000)
            .orderTotalPrice(10000)
            .build();


        given(this.service.receiptReturnOrder(any(ReturnOrderForm.Request.class), eq(1L), eq(1L)))
            .willReturn(response);

        ReturnOrderForm.Request request =
            ReturnOrderForm.Request.builder()
            .returnReason("???")
            .amount(1)
            .addressId(1L)
            .pickupRequest(response.getPickupRequest())
            .build();

        this.mvc.perform(
            RestDocumentationRequestBuilders.
            post("/api/orders/return/{orderItemId}", 1L)
            .header(HttpHeaders.AUTHORIZATION, utils.genAuthHeader())
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(request))
        )
            .andDo(print())
            .andDo(
                document(
                    "orders/{method-name}",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    getAuthHeader(),
                    pathParameters(
                        parameterWithName("orderItemId").description("????????? ?????? ?????? ?????????")
                    )
                )
            );
    }

    @Test
    void placeShipment() throws Exception {

        ShipmentForm.Response response =
            ShipmentForm.Response.builder()
            .orderItemId(1L)
            .trackingNumber("111230000321")
            .build();



        given(this.service.placeShipment(anyList(), eq(1L)))
            .willReturn(Collections.singletonList(response));

        ShipmentForm.Request request =
            ShipmentForm.Request.builder()
            .orderItemId(1L)
            .trackingNumber(response.getTrackingNumber())
            .build();

        this.mvc.perform(
            post("/api/orders/shipment")
            .content(mapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, utils.getSellerAuthHeader())
        )
            .andDo(print())
            .andDo(
                document(
                    "orders/{method-name}",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    getAuthHeader()
                )
            );



    }

    @Test
    void placeShipments() throws Exception {
        ShipmentForm.Response response =
            ShipmentForm.Response.builder()
                .orderItemId(1L)
                .trackingNumber("111230000321")
                .build();



        given(this.service.placeShipment(anyList(), eq(1L)))
            .willReturn(Collections.singletonList(response));

        ShipmentForm.Request request =
            ShipmentForm.Request.builder()
                .orderItemId(1L)
                .trackingNumber(response.getTrackingNumber())
                .build();

        this.mvc.perform(
            post("/api/orders/shipment")
                .content(mapper.writeValueAsString(Collections.singletonList(request)))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, utils.getSellerAuthHeader())
        )
            .andDo(print())
            .andDo(
                document(
                    "orders/{method-name}",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    getAuthHeader()
                )
            );


    }
}