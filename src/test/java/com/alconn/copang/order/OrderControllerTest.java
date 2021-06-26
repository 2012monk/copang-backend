package com.alconn.copang.order;

import com.alconn.copang.address.AddressForm;
import com.alconn.copang.client.UserForm;
import com.alconn.copang.order.dto.OrderForm;
import com.alconn.copang.order.dto.OrderItemForm;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
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

        given(service.createOrder(any(OrderForm.Create.class))).willReturn(response);

        // when
        OrderForm.Create create = OrderForm.Create.builder()
                .clientId(1L)
                .addressId(1L)
                .orderItems(orderItems)
                .totalAmount(4)
                .totalPrice(80000)
                .build();

        System.out.println("service = " + mapper.writeValueAsString(service.createOrder(create)));

        String token = genToken();

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
                .city("서울")
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
                            .option((i + 1L) + "KG")
                            .unitTotal(2000)
                            .build()
            );
        }
        return orderItems;
    }

    private RequestFieldsSnippet getRequestFieldsSnippet() {
        return relaxedRequestFields(
                fieldWithPath("clientId").type(JsonFieldType.NUMBER).description("유저 식별 코드"),
                fieldWithPath("addressId").type(JsonFieldType.NUMBER).description("주소 식별 코드"),
                fieldWithPath("totalPrice").type(JsonFieldType.NUMBER).description("총금액"),
                fieldWithPath("totalAmount").description(JsonFieldType.NUMBER).description("상품 총 갯수"),
                fieldWithPath("orderItems").type(JsonFieldType.ARRAY).description("주문 아이템 리스트"),
                fieldWithPath("orderItems.[].itemName").type(JsonFieldType.STRING).description("상품명"),
                fieldWithPath("orderItems.[].itemId").type(JsonFieldType.NUMBER).description("아이템 식별자"),
                fieldWithPath("orderItems.[].itemDetailId").type(JsonFieldType.NUMBER).description("옵션 식별자"),
                fieldWithPath("orderItems.[].option").type(JsonFieldType.STRING).description("옵션명").optional(),
                fieldWithPath("orderItems.[].price").type(JsonFieldType.NUMBER).description("가격").optional(),
                fieldWithPath("orderItems.[].amount").type(JsonFieldType.NUMBER).description("개별 상품수량"),
                fieldWithPath("orderItems.[].unitTotal").type(JsonFieldType.NUMBER).description("개별상품 합산 금액")
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
                fieldWithPath("data.address.city").type(JsonFieldType.STRING).description("도시"),
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
                fieldWithPath("data.orderItems.[].option").type(JsonFieldType.STRING).description("옵션명"),
                fieldWithPath("data.orderItems.[].price").type(JsonFieldType.NUMBER).description("가격"),
                fieldWithPath("data.orderItems.[].amount").type(JsonFieldType.NUMBER).description("개별 상품수량"),
                fieldWithPath("data.orderItems.[].unitTotal").type(JsonFieldType.NUMBER).description("개별상품 합산 금액")
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
                fieldWithPath("data[].address.city").type(JsonFieldType.STRING).description("도시"),
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
                fieldWithPath("data[].orderItems.[].option").type(JsonFieldType.STRING).description("옵션명"),
                fieldWithPath("data[].orderItems.[].price").type(JsonFieldType.NUMBER).description("가격"),
                fieldWithPath("data[].orderItems.[].amount").type(JsonFieldType.NUMBER).description("개별 상품수량"),
                fieldWithPath("data[].orderItems.[].unitTotal").type(JsonFieldType.NUMBER).description("개별상품 합산 금액")
        );
    }


    private String genToken() {
        return Jwts.builder()
                .setIssuer("11")
                .setAudience("123")
                .signWith(Keys.secretKeyFor(SignatureAlgorithm.HS512))
                .compact();
    }


    @Test
    void proceedOrder() throws Exception {
        Long orderId = 19082L;
        OrderStatus status = OrderStatus.PROCEED;

        OrderForm.Response response =
                OrderForm.Response
                        .builder()
                        .orderId(orderId)
                        .orderStatus(status)
                        .build();

        given(service.proceedOrder(orderId)).willReturn(response);

        ResultActions result = this.mvc.perform(
                RestDocumentationRequestBuilders.
                patch("/api/orders/{orderId}/proceed", orderId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + genToken())
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
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + genToken())
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
                .header(HttpHeaders.AUTHORIZATION, "Bearer "+genToken())
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
                get("/api/orders/client/{clientId}", 1L)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + genToken())

        );

        actions.andExpect(status().isOk())
                .andDo(document("orders/{method-name}",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("clientId").description("고객 고유 식별자")
                        ),
                        getAuthHeader(),
                        getResponseFieldsSnippetList()
                ));

    }
}