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
// 할일! Spring boot test without spring security config
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
                get쿠팡노예();

        AddressForm address = getAddressForm();

        List<OrderItemForm> orderItems = genOrderItems("고구마");


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
                .detail("철원 1번지")
                .address("서울")
                .receiverName("제가 받는사람이에요")
                .preRequest("빨리요제발")
                .receiverPhone("010-8989-9898")
                .build();
    }

    private UserForm.Response get쿠팡노예() {
        return UserForm.Response.builder()
                .clientId(1L)
                .username("test@testclient.com")
                .phone("010-9090-8989")
                .realName("쿠팡노예")
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
                            .optionName("박스")
                            .optionValue((i + 1L) + "KG")
                            .unitTotal(2000)
                            .build()
            );
        }
        return orderItems;
    }

    private RequestFieldsSnippet getRequestFieldsSnippet() {
        return relaxedRequestFields(
                fieldWithPath("addressId").type(JsonFieldType.NUMBER).description("주소 식별 코드"),
                fieldWithPath("orderItems").type(JsonFieldType.ARRAY).description("주문 아이템 리스트"),
                fieldWithPath("orderItems.[].itemId").type(JsonFieldType.NUMBER).description("아이템 식별자").optional(),
                fieldWithPath("orderItems.[].itemDetailId").type(JsonFieldType.NUMBER).description("옵션 식별자"),
                fieldWithPath("orderItems.[].amount").type(JsonFieldType.NUMBER).description("개별 상품수량")
//                fieldWithPath("totalPrice").type(JsonFieldType.NUMBER).description("총금액"),
//                fieldWithPath("totalAmount").description(JsonFieldType.NUMBER).description("상품 총 갯수"),
//                fieldWithPath("orderItems.[].itemName").type(JsonFieldType.STRING).description("상품명"),
//                fieldWithPath("orderItems.[].optionName").type(JsonFieldType.STRING).description("옵션명").optional(),
//                fieldWithPath("orderItems.[].optionValue").type(JsonFieldType.STRING).description("옵션값").optional(),
//                fieldWithPath("orderItems.[].price").type(JsonFieldType.NUMBER).description("가격").optional(),
//                fieldWithPath("orderItems.[].unitTotal").type(JsonFieldType.NUMBER).description("개별상품 합산 금액")
        );
    }

    private ResponseFieldsSnippet getResponseFieldsSnippetList() {
        return relaxedResponseFields(
                fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메세지"),
                fieldWithPath("code").type(JsonFieldType.NUMBER).description("결과 코드"),
                fieldWithPath("data").type(JsonFieldType.ARRAY).description("주문 데이터"),
                fieldWithPath("data[].orderId").type(JsonFieldType.NUMBER).description("주문번호"),
                fieldWithPath("data[].orderDate").type(JsonFieldType.STRING).description("주문날짜"),
                fieldWithPath("data[].orderStatus").type(JsonFieldType.STRING).description("주문상태"),
                fieldWithPath("data[].totalPrice").type(JsonFieldType.NUMBER).description("총금액"),
                fieldWithPath("data[].totalAmount").description(JsonFieldType.NUMBER).description("상품 총 갯수"),
                fieldWithPath("data[].address").type(JsonFieldType.OBJECT).description("주소정보"),
                fieldWithPath("data[].address.addressId").type(JsonFieldType.NUMBER).description("주소 식별자"),
                fieldWithPath("data[].address.address").type(JsonFieldType.STRING).description("도시"),
                fieldWithPath("data[].address.detail").type(JsonFieldType.STRING).description("상세주소"),
                fieldWithPath("data[].address.receiverPhone").type(JsonFieldType.STRING).description("받는사람 전화번호"),
                fieldWithPath("data[].address.receiverName").type(JsonFieldType.STRING).description("받는사람 이름"),
                fieldWithPath("data[].address.preRequest").type(JsonFieldType.STRING).description("요청사항"),
                fieldWithPath("data[].client").type(JsonFieldType.OBJECT).description("주문자 정보"),
                fieldWithPath("data[].client.clientId").type(JsonFieldType.NUMBER).description("주문자 식별자"),
                fieldWithPath("data[].client.username").type(JsonFieldType.STRING).description("주문자 아이디"),
                fieldWithPath("data[].client.phone").type(JsonFieldType.STRING).description("주문자 전화번호"),
                fieldWithPath("data[].client.realName").type(JsonFieldType.STRING).description("주문자 이름"),
                fieldWithPath("data[].orderItems").type(JsonFieldType.ARRAY).description("주문 아이템 리스트"),
                fieldWithPath("data[].orderItems.[].itemName").type(JsonFieldType.STRING).description("상품명"),
                fieldWithPath("data[].orderItems.[].itemId").type(JsonFieldType.NUMBER).description("아이템 식별자"),
                fieldWithPath("data[].orderItems.[].itemDetailId").type(JsonFieldType.NUMBER).description("옵션 식별자"),
                fieldWithPath("data[].orderItems.[].optionName").type(JsonFieldType.STRING).description("옵션명"),
                fieldWithPath("data[].orderItems.[].optionValue").type(JsonFieldType.STRING).description("옵션값"),
                fieldWithPath("data[].orderItems.[].price").type(JsonFieldType.NUMBER).description("가격"),
                fieldWithPath("data[].orderItems.[].amount").type(JsonFieldType.NUMBER).description("개별 상품수량"),
                fieldWithPath("data[].orderItems.[].unitTotal").type(JsonFieldType.NUMBER).description("개별상품 합산 금액")
        );
    }
    private ResponseFieldsSnippet getResponseFieldsSnippet() {
        return relaxedResponseFields(
            fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메세지"),
            fieldWithPath("code").type(JsonFieldType.NUMBER).description("결과 코드"),
            fieldWithPath("data").type(JsonFieldType.OBJECT).description("주문 데이터"),
            fieldWithPath("data.orderId").type(JsonFieldType.NUMBER).description("주문번호"),
            fieldWithPath("data.orderDate").type(JsonFieldType.STRING).description("주문날짜"),
            fieldWithPath("data.orderStatus").type(JsonFieldType.STRING).description("주문상태"),
            fieldWithPath("data.totalPrice").type(JsonFieldType.NUMBER).description("총금액"),
            fieldWithPath("data.totalAmount").description(JsonFieldType.NUMBER).description("상품 총 갯수"),
            fieldWithPath("data.address").type(JsonFieldType.OBJECT).description("주소정보"),
            fieldWithPath("data.address.addressId").type(JsonFieldType.NUMBER).description("주소 식별자"),
            fieldWithPath("data.address.address").type(JsonFieldType.STRING).description("도시"),
            fieldWithPath("data.address.detail").type(JsonFieldType.STRING).description("상세주소"),
            fieldWithPath("data.address.receiverPhone").type(JsonFieldType.STRING).description("받는사람 전화번호"),
            fieldWithPath("data.address.receiverName").type(JsonFieldType.STRING).description("받는사람 이름"),
            fieldWithPath("data.address.preRequest").type(JsonFieldType.STRING).description("요청사항"),
            fieldWithPath("data.client").type(JsonFieldType.OBJECT).description("주문자 정보"),
            fieldWithPath("data.client.clientId").type(JsonFieldType.NUMBER).description("주문자 식별자"),
            fieldWithPath("data.client.username").type(JsonFieldType.STRING).description("주문자 아이디"),
            fieldWithPath("data.client.phone").type(JsonFieldType.STRING).description("주문자 전화번호"),
            fieldWithPath("data.client.realName").type(JsonFieldType.STRING).description("주문자 이름"),
            fieldWithPath("data.orderItems").type(JsonFieldType.ARRAY).description("주문 아이템 리스트"),
            fieldWithPath("data.orderItems.[].itemName").type(JsonFieldType.STRING).description("상품명"),
            fieldWithPath("data.orderItems.[].itemId").type(JsonFieldType.NUMBER).description("아이템 식별자"),
            fieldWithPath("data.orderItems.[].itemDetailId").type(JsonFieldType.NUMBER).description("옵션 식별자"),
            fieldWithPath("data.orderItems.[].optionName").type(JsonFieldType.STRING).description("옵션명"),
            fieldWithPath("data.orderItems.[].optionValue").type(JsonFieldType.STRING).description("옵션값"),
            fieldWithPath("data.orderItems.[].price").type(JsonFieldType.NUMBER).description("가격"),
            fieldWithPath("data.orderItems.[].amount").type(JsonFieldType.NUMBER).description("개별 상품수량"),
            fieldWithPath("data.orderItems.[].unitTotal").type(JsonFieldType.NUMBER).description("개별상품 합산 금액")
        );
    }





    @Test
    void getSellerOrder() throws Exception {
        Long orderId = 19082L;

        List<OrderItemForm> orderItemForms = new ArrayList<>();
        for (int i=0;i<2;i++){
            orderItemForms.add(
                OrderItemForm.builder()
                .optionName("중량")
                .optionValue(i + "KG")
                .itemDetailId((long) i)
                .itemId(1L)
                .price(2000)
                .itemName("감자")
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
            .realName("길동홍")
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
                        fieldWithPath("message").description("결과 메세지"),
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("data").description("응답데이터")
                    ),
                    relaxedResponseFields(
                        beneathPath("data").withSubsectionId("data"),
                        fieldWithPath("sellerOrderId").type(JsonFieldType.NUMBER).description("판매자 발주서 번호"),
                        fieldWithPath("client").type(JsonFieldType.OBJECT).description("구매자 정보"),
                        fieldWithPath("orderItems").type(JsonFieldType.ARRAY).description("발주 상품 목록"),
                        fieldWithPath("orderDate").type(JsonFieldType.STRING).description("주문 날짜"),
                        fieldWithPath("address").type(JsonFieldType.OBJECT).description("주문자 주소"),
                        fieldWithPath("totalPrice").type(JsonFieldType.NUMBER).description("총금액"),
                        fieldWithPath("totalAmount").type(JsonFieldType.NUMBER).description("총 수량")

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
                                parameterWithName("orderId").description("주문번호")
                        ),
//                        commonFields(),
                        relaxedResponseFields(
                                fieldWithPath("message").description("결과 메세지"),
                                fieldWithPath("code").description("결과 코드"),
                                fieldWithPath("data").description("응답데이터"),
                                fieldWithPath("data.orderId").type(JsonFieldType.NUMBER).description("주문번호"),
                                fieldWithPath("data.orderStatus").type(JsonFieldType.STRING).description("주문상태")
                        )
                ));
    }

    private RequestHeadersSnippet getAuthHeader() {
        return requestHeaders(
                headerWithName(HttpHeaders.AUTHORIZATION).description("인증토큰")
        );
    }

    @Test
    void getOneOrder() throws Exception {
        OrderForm.Response response =
                getOrderResponse("자동차");

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
                                        parameterWithName("orderId").description("주문번호")
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
                .client(get쿠팡노예())
                .orderItems(genOrderItems(name))
                .totalAmount(5)
                .totalPrice(540000)
                .orderDate(LocalDateTime.now())
                .build();
    }


    @Test
    void getClientOrderList() throws Exception {


        String[] names = new String[]{
                "커피", "냉장고" , "생수"
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
            get쿠팡노예();

        AddressForm address = getAddressForm();

        List<OrderItemForm> orderItems = genOrderItems("고구마");


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
                        parameterWithName("uid").description("imp_uid 결제 번호"),
                        parameterWithName("orderId").description("주문번호")
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
            .pickupRequest("문앞")
                .returnReason("걍")
            .address(utils.getMockAddress())
            .returnAmount(1)
            .returnPrice(4000)
            .orderTotalPrice(10000)
            .build();


        given(this.service.receiptReturnOrder(any(ReturnOrderForm.Request.class), eq(1L), eq(1L)))
            .willReturn(response);

        ReturnOrderForm.Request request =
            ReturnOrderForm.Request.builder()
            .returnReason("걍")
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
                        parameterWithName("orderItemId").description("환불한 주문 상품 아이디")
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