package com.alconn.copang.cart;


import static com.alconn.copang.ApiDocumentUtils.getAuthHeaderField;
import static com.alconn.copang.ApiDocumentUtils.getDocumentRequest;
import static com.alconn.copang.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedRequestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.alconn.copang.auth.AccessTokenContainer;
import com.alconn.copang.auth.LoginToken;
import com.alconn.copang.client.Client;
import com.alconn.copang.client.ClientRepo;
import com.alconn.copang.client.ClientService;
import com.alconn.copang.exceptions.InvalidTokenException;
import com.alconn.copang.exceptions.LoginFailedException;
import com.alconn.copang.utils.TestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@AutoConfigureRestDocs
@SpringBootTest
@AutoConfigureMockMvc
class CartControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    TestUtils utils;

    Client client;

    @Autowired
    ClientRepo repo;

    @Autowired
    ClientService service;

    @MockBean
    CartService cartService;

    @Autowired
    ObjectMapper mapper;

    AccessTokenContainer container;

    @Autowired
    EntityManager m;

    @Disabled
    @Test
    void injectTest() throws Exception {
//        AccessTokenContainer container = getAccessTokenContainer();

        this.mvc.perform(
            get("/api/cart/test")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + container.getAccess_token())
        ).andExpect(status().isOk())
            .andDo(print())
            .andExpect(content().string(String.valueOf(client.getClientId())));
    }


    //    @BeforeEach
    @Transactional
//    @Rollback
    AccessTokenContainer getAccessTokenContainer()
        throws LoginFailedException, InvalidTokenException {
        this.repo.deleteAll();

        m.flush();
        m.clear();
        if (client == null) {
            client = utils.generateRealClient();
            repo.save(client);
        }

        LoginToken token = new LoginToken();
        ReflectionTestUtils.setField(token, "username", client.getUsername());
        ReflectionTestUtils.setField(token, "password", client.getPassword());
        AccessTokenContainer container = service.login(token);
        this.container = container;
        return container;
    }


    @Transactional
    @Test
    void addCartItem() throws Exception {
        AccessTokenContainer container = getAccessTokenContainer();
        CartItemForm res =
            CartItemForm.builder()
                .itemId(1L)
                .itemDetailId(2L)
                .itemName("망고")
                .optionName("중량")
                .optionValue("1KG")
                .amount(3)
                .price(4500)
                .build();

        CartForm.Add addForm =
            CartForm.Add.builder()
                .itemDetailId(res.getItemDetailId())
                .itemId(res.getItemId())
//                .amount(3)
                .build();

        given(cartService.addCartItem(eq(client.getClientId()), any(CartForm.Add.class)))
            .willReturn(res);

        this.mvc.perform(
            post("/api/cart/item")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(addForm))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + container.getAccess_token())
        ).andExpect(status().isOk())
            .andDo(print())
            .andDo(
                document("cart/{method-name}",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    getAuthHeaderField(),
                    relaxedRequestFields(
                        fieldWithPath("itemDetailId").type(JsonFieldType.NUMBER)
                            .description("옵션아이디"),
                        fieldWithPath("amount").type(JsonFieldType.NUMBER).description("수량"),
                        fieldWithPath("itemId").type(JsonFieldType.NUMBER).description("상품아이디")
                            .optional()
                    ),
                    relaxedResponseFields(
                        fieldWithPath("data.itemId").type(JsonFieldType.NUMBER)
                            .description("상품아이디"),
                        fieldWithPath("data.itemDetailId").type(JsonFieldType.NUMBER)
                            .description("옵션아이디"),
                        fieldWithPath("data.itemName").type(JsonFieldType.STRING)
                            .description("상품명"),
                        fieldWithPath("data.optionName").type(JsonFieldType.STRING)
                            .description("옵션명"),
                        fieldWithPath("data.optionValue").type(JsonFieldType.STRING)
                            .description("옵션값"),
                        fieldWithPath("data.amount").type(JsonFieldType.NUMBER).description("수량"),
                        fieldWithPath("data.price").type(JsonFieldType.NUMBER).description("가격")
                    )

                )
            );

    }

    @Transactional
    @Test
    void updateCartItemAmount()
        throws Exception {
        AccessTokenContainer container = getAccessTokenContainer();
        CartItemForm res =
            CartItemForm.builder()
                .itemId(1L)
                .itemDetailId(2L)
                .itemName("망고")
                .optionName("중량")
                .optionValue("1KG")
                .amount(50)
                .price(4500)
                .registerDate(LocalDateTime.now())
                .build();
        CartForm.Add add =
            CartForm.Add.builder()
                .itemId(1L)
                .itemDetailId(2L)
                .amount(50)
                .build();
        given(this.cartService.updateAmountItem(eq(client.getClientId()), eq(2L), eq(50)))
            .willReturn(res);

//        System.out.println("service = " + mapper.writeValueAsString(cartService.updateAmountItem(client.getClientId(), 2L, 50)));
//        System.out.println("client.getClientId() = " + client.getClientId());

        this.mvc.perform(
            RestDocumentationRequestBuilders.
                post("/api/cart/item/amount")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(add))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + container.getAccess_token())
        )
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(
                document("cart/{method-name}",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    getAuthHeaderField(),
                    relaxedRequestFields(
                        fieldWithPath("itemDetailId").type(JsonFieldType.NUMBER)
                            .description("옵션아이디"),
                        fieldWithPath("amount").type(JsonFieldType.NUMBER).description("수량"),
                        fieldWithPath("itemId").type(JsonFieldType.NUMBER).description("상품아이디")
                            .optional()
                    ),
                    relaxedResponseFields(
                        fieldWithPath("data.itemId").type(JsonFieldType.NUMBER)
                            .description("상품아이디"),
                        fieldWithPath("data.itemDetailId").type(JsonFieldType.NUMBER)
                            .description("옵션아이디"),
                        fieldWithPath("data.itemName").type(JsonFieldType.STRING)
                            .description("상품명"),
                        fieldWithPath("data.optionName").type(JsonFieldType.STRING)
                            .description("옵션명"),
                        fieldWithPath("data.optionValue").type(JsonFieldType.STRING)
                            .description("옵션값"),
                        fieldWithPath("data.amount").type(JsonFieldType.NUMBER).description("수량"),
                        fieldWithPath("data.price").type(JsonFieldType.NUMBER).description("가격"),
                        fieldWithPath("data.registerDate").type(JsonFieldType.STRING).description("최초 추가 날짜")
                    )

                )
            );
//            .andDo(
//                document()
//            )

    }

    @Transactional
    @Test
    void getClientCart() throws Exception {
        AccessTokenContainer container = getAccessTokenContainer();
        Map<String, Object[]> maps = new HashMap<String, Object[]>() {{
            put("망고", new Object[]{
                "1KG", 2, 1L, 4500, 5L
            });
            put("바나나", new Object[]{
                "6KG", 5, 2L, 8988, 32L
            });
            put("키위", new Object[]{
                "99KG", 2, 3L, 56000, 23L
            });
        }};

        List<CartItemForm> cartItemForms = maps.keySet().stream().map(
            n -> CartItemForm.builder()
                .itemName(n)
                .optionValue((String) maps.get(n)[0])
                .optionName("중량")
                .amount((Integer) maps.get(n)[1])
                .itemDetailId((Long) maps.get(n)[2])
                .price((Integer) maps.get(n)[3])
                .itemId((Long) maps.get(n)[4])
                .unitTotal((Integer) maps.get(n)[1] * (Integer) maps.get(n)[3])
                .build()
        ).collect(Collectors.toCollection(ArrayList::new));

        int total = cartItemForms.stream().mapToInt(c -> c.getAmount() * c.getPrice()).sum();
        int amount = cartItemForms.stream().mapToInt(CartItemForm::getAmount).sum();

        CartForm.Response response =
            CartForm.Response.builder()
                .cartId(542L)
                .cartItems(cartItemForms)
                .clientId(client.getClientId())
                .totalAmount(amount)
                .totalPrice(total)
                .build();
        given(this.cartService.getCart(eq(client.getClientId()))).willReturn(response);

        this.mvc.perform(
            get("/api/cart")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + container.getAccess_token())
        )
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(
                document("cart/{method-name}",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    getAuthHeaderField(),
                    relaxedResponseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메세지"),
                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("결과 코드"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                        fieldWithPath("data.cartId").type(JsonFieldType.NUMBER)
                            .description("카트아이디"),
                        fieldWithPath("data.clientId").type(JsonFieldType.NUMBER)
                            .description("유저 식별자"),
                        fieldWithPath("data.totalAmount").type(JsonFieldType.NUMBER)
                            .description("총수량"),
                        fieldWithPath("data.totalPrice").type(JsonFieldType.NUMBER)
                            .description("총가격"),

                        fieldWithPath("data.cartItems.[].itemId").type(JsonFieldType.NUMBER)
                            .description("상품아이디"),
                        fieldWithPath("data.cartItems.[].itemDetailId").type(JsonFieldType.NUMBER)
                            .description("옵션아이디"),
                        fieldWithPath("data.cartItems.[].itemName").type(JsonFieldType.STRING)
                            .description("상품명"),
                        fieldWithPath("data.cartItems.[].optionName").type(JsonFieldType.STRING)
                            .description("옵션명"),
                        fieldWithPath("data.cartItems.[].optionValue").type(JsonFieldType.STRING)
                            .description("옵션값"),
                        fieldWithPath("data.cartItems.[].amount").type(JsonFieldType.NUMBER)
                            .description("수량"),
                        fieldWithPath("data.cartItems.[].price").type(JsonFieldType.NUMBER)
                            .description("가격"),
                        fieldWithPath("data.cartItems.[].unitTotal").type(JsonFieldType.NUMBER)
                            .description("상품합계금액")
                    )

                )
            );

    }

    @Transactional
    @Test
    void clearCart() throws Exception {
        AccessTokenContainer container = getAccessTokenContainer();
        this.mvc.perform(
            delete("/api/cart")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + container.getAccess_token())
        )
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document(
                "cart/{method-name}",
                getDocumentRequest(),
                getDocumentResponse(),
                getAuthHeaderField(),
                responseFields(
                    fieldWithPath("message").description("결과 메세지"),
                    fieldWithPath("code").description("결과 코드")
                )
            ));

    }

    @Transactional
    @Test
    void deleteItem() throws Exception {
        AccessTokenContainer container = getAccessTokenContainer();

        CartItemForm res =
            CartItemForm.builder()
                .itemName("삼성 전기 자전거")
                .itemDetailId(54L)
                .build();

        CartForm.Add add =
            CartForm.Add.builder()
                .itemDetailId(res.getItemDetailId())
                .build();

        given(this.cartService.deleteItem(eq(client.getClientId()), eq(54L))).willReturn(res);

        this.mvc.perform(
            RestDocumentationRequestBuilders.
                delete("/api/cart/item/{itemDetailId}", res.getItemDetailId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + container.getAccess_token())
        ).andExpect(status().isOk())
            .andDo(print())
            .andDo(
                document(
                    "cart/{method-name}",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    getAuthHeaderField(),
                    pathParameters(
                        parameterWithName("itemDetailId").description("옵션아이디")
                    ),
                    relaxedResponseFields(
                        fieldWithPath("data.itemName").type(JsonFieldType.STRING)
                            .description("상품명"),
                        fieldWithPath("data.itemDetailId").type(JsonFieldType.NUMBER)
                            .description("옵션아이디")
                    )
                )
            );

    }
}