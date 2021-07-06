package com.alconn.copang.wishlist;


import com.alconn.copang.ApiDocumentUtils;
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
import com.alconn.copang.item.Item;
import com.alconn.copang.item.ItemDetail;
import com.alconn.copang.item.ItemDetailRepository;
import com.alconn.copang.item.ItemRepository;
import com.alconn.copang.utils.TestUtils;
import com.alconn.copang.wishlist.dto.WishRequest;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static com.alconn.copang.ApiDocumentUtils.getAuthHeaderField;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@AutoConfigureRestDocs
@Transactional
public class WishControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemDetailRepository itemDetailRepository;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    WishRepository wishRepository;

    @Autowired
    ClientRepo clientRepo;

    @Autowired
    EntityManager m;

    AccessTokenContainer container;

    @Autowired
    TestUtils utils;

    @Autowired
    ClientService clientService;

    @Autowired
    AddressRepository addressRepository;

    @BeforeEach
    void setUp() {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }



    //=================
    private ItemDetail testItem(){
        Item item = Item.builder()
                    .itemName("티셔츠")
                .build();

        itemRepository.save(item);

        ItemDetail itemDetail = ItemDetail.builder()
                .mainImg("메인사진")
                .item(item)
                .stockQuantity(10)
                .optionName("색상")
                .optionValue("검은색")
                .mainImg("메인사진")
                .price(10000)
                .build();
        itemDetail.itemConnect(item);
        itemDetailRepository.save(itemDetail);
        return itemDetail;
    }



    //=================
    @Transactional
    @Test
    public void addTest() throws Exception{
        AccessTokenContainer container = getAccessTokenContainer(client());
        ItemDetail itemDetail=testItem();

        WishRequest wishRequest=WishRequest.builder()
                .itemDetailId(itemDetail.getItemDetailId())
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/wishlist/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(wishRequest))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + container.getAccess_token())
                .characterEncoding("utf-8")
        ).andExpect(status().isOk()).andDo(print()).andDo(document("wishlist/post-save",
                ApiDocumentUtils.getDocumentRequest(),
                ApiDocumentUtils.getDocumentResponse(),
                getAuthHeaderField(),
                relaxedRequestFields(
                        fieldWithPath("itemDetailId").type(JsonFieldType.NUMBER).description("상품옵션번호")
                ),
                relaxedResponseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                        fieldWithPath("data..wishId").type(JsonFieldType.NUMBER).description("찜등록번호"),
                        fieldWithPath("data.itemDetailId").type(JsonFieldType.NUMBER).description("상품옵션번호"),
                        fieldWithPath("data.price").type(JsonFieldType.NUMBER).description("상품가격"),
                        fieldWithPath("data.mainImg").type(JsonFieldType.STRING).description("부모카테고리번호"),
                        fieldWithPath("data.itemName").type(JsonFieldType.STRING).description("상품명")
                )));
    }

    @Transactional
    @Test
    public void list() throws Exception{
        Client client=client();
        AccessTokenContainer container = getAccessTokenContainer(client);

        ItemDetail itemDetail=testItem();

        Wish wish=Wish.builder()
                .client(client)
                .itemDetail(itemDetail)
                .build();
        wishRepository.save(wish);


        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/wishlist/list")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + container.getAccess_token())
                .characterEncoding("utf-8")
        ).andExpect(status().isOk()).andDo(print()).andDo(document("wishlist/get-list",
                ApiDocumentUtils.getDocumentRequest(),
                ApiDocumentUtils.getDocumentResponse(),
                getAuthHeaderField(),
                relaxedResponseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                        fieldWithPath("data.[].wishId").type(JsonFieldType.NUMBER).description("찜등록번호"),
                        fieldWithPath("data.[].itemDetailId").type(JsonFieldType.NUMBER).description("상품옵션번호"),
                        fieldWithPath("data.[].price").type(JsonFieldType.NUMBER).description("상품가격"),
                        fieldWithPath("data.[].mainImg").type(JsonFieldType.STRING).description("부모카테고리번호"),
                        fieldWithPath("data.[].itemName").type(JsonFieldType.STRING).description("상품명")
                )));
    }
    @Transactional
    @Test
    public void delList() throws Exception{
        Client client=client();
        AccessTokenContainer container = getAccessTokenContainer(client);
        ItemDetail itemDetail=testItem();
        ItemDetail itemDetail2=testItem();
        Wish wish= Wish.builder()
                .client(client)
                .itemDetail(itemDetail)
                .build();

        Wish wish2= Wish.builder()
                .client(client)
                .itemDetail(itemDetail2)
               .build();

        wishRepository.save(wish);
        wishRepository.save(wish2);

        List<Long> wishList=new ArrayList<>();
        wishList.add(wish.getWishId());
        wishList.add(wish2.getWishId());

        WishRequest.WishRequestdel wishRequestdel= WishRequest.WishRequestdel.builder()
                .wishId(wishList)
                .build();


        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/wishlist/del")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(wishRequestdel))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + container.getAccess_token())
                .characterEncoding("utf-8")
        ).andExpect(status().isOk()).andDo(print()).andDo(document("wishlist/delete",
                ApiDocumentUtils.getDocumentRequest(),
                ApiDocumentUtils.getDocumentResponse(),
                getAuthHeaderField(),
                relaxedRequestFields(
                        fieldWithPath("wishId").type(JsonFieldType.ARRAY).description("찜등록번호")
                ),
                relaxedResponseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                        fieldWithPath("data.[].wishId").type(JsonFieldType.NUMBER).description("찜등록번호"),
                        fieldWithPath("data.[].itemDetailId").type(JsonFieldType.NUMBER).description("상품옵션번호"),
                        fieldWithPath("data.[].price").type(JsonFieldType.NUMBER).description("상품가격"),
                        fieldWithPath("data.[].mainImg").type(JsonFieldType.STRING).description("부모카테고리번호"),
                        fieldWithPath("data.[].itemName").type(JsonFieldType.STRING).description("상품명")
                )));

    }
    private Client client(){
        Client client1=Client.builder()
                .password("3371")
                .role(Role.CLIENT)
                .username("아아아아아아앙앙ㄱ")
                .build();
        clientRepo.save(client1);
        return client1;
    }

    AccessTokenContainer getAccessTokenContainer(Client client) throws LoginFailedException, InvalidTokenException {

        LoginToken token = new LoginToken();
        ReflectionTestUtils.setField(token, "username", client.getUsername());
        ReflectionTestUtils.setField(token, "password", client.getPassword());
        AccessTokenContainer container = clientService.login(token);
        this.container = container;
        return container;
    }
}
