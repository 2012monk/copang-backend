package com.alconn.copang.utils;

import com.alconn.copang.client.Client;
import com.alconn.copang.client.ClientRepo;
import com.alconn.copang.client.Role;
import com.alconn.copang.exceptions.InvalidTokenException;
import com.alconn.copang.item.Item;
import com.alconn.copang.item.ItemDetail;
import com.alconn.copang.security.provider.JwtTokenProvider;
import com.alconn.copang.seller.Seller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
//@Repository
public class TestUtils {

    public static Client client;
    @Autowired
    ClientRepo repo;

    @Autowired
    JwtTokenProvider provider;


    public Client generateRealClient() {
        return Client.builder()
            .username("coppang143")
            .password("비밀번호123!")
            .description("안녕하세요!")
            .phone("010-0030-9090")
            .realName("길동홍길동")
            .role(Role.CLIENT)
            .build();
    }

    public Seller getSeller() {
        return Seller.builder()
            .username("coppang-seller")
            .password("비밀번호123!")
            .description("안녕하세요!")
            .phone("010-0030-9090")
            .realName("길동홍길동")
            .role(Role.SELLER)
            .build();
    }

    public Client generateMockClient() {
        return Client.builder()
            .clientId(1L)
            .username("coppang143")
            .password("비밀번호123!")
            .description("안녕하세요!")
            .phone("010-0030-9090")
            .realName("길동홍길동")
            .role(Role.CLIENT)
            .build();
    }

    public String genHeader(Client client) throws InvalidTokenException {
        return "Bearer " + genToken(client);
    }

    public String genToken(Client client) throws InvalidTokenException {
        return provider.createAccessToken(client).orElseThrow(InvalidTokenException::new);
    }

    public String genToken() throws InvalidTokenException {
        return genToken(generateMockClient());
    }

    public String genSellerHeader() throws InvalidTokenException {
        Client cl = getSeller();
        return "Bearer " + provider.createAccessToken(cl).orElseThrow(InvalidTokenException::new);
    }

    public Seller getSellerC() {
        return Seller.builder()
            .username("coppang143-se11")
            .password("비밀번호123!")
            .description("안녕하세요!")
            .phone("010-0030-9090")
            .realName("길동홍길동")
            .role(Role.SELLER)
            .build();
    }

    public Client getSeller() {
        return Client.builder()
            .clientId(1L)
            .username("coppang143")
            .password("비밀번호123!")
            .description("안녕하세요!")
            .phone("010-0030-9090")
            .realName("길동홍길동")
            .role(Role.SELLER)
            .build();
    }

    public Client getRealSeller() {
        return Client.builder()
            .username("coppang143")
            .password("비밀번호123!")
            .description("안녕하세요!")
            .phone("010-0030-9090")
            .realName("길동홍길동")
            .role(Role.SELLER)
            .build();
    }

    public String genAuthHeader() throws InvalidTokenException {
        return "Bearer " + genToken();
    }

    public ItemDetail getItemDetail() {
        return ItemDetail.builder()
            .optionName("테스트용")
            .optionValue("빨간색")
            .price(5000)
            .stockQuantity(10)
            .mainImg("no image")
            .item(
                Item.builder()
                    .itemName("신발1")
                    .build()
            ).build();
    }

    public String getSellerAuthHeader() {
        return "Bearer " + provider.createAccessToken(
            Client.builder()
                .clientId(1L)
                .username("coppang143")
                .password("비밀번호123!")
                .description("안녕하세요!")
                .phone("010-0030-9090")
                .realName("길동홍길동")
                .role(Role.SELLER)
                .build()
        ).orElseThrow(RuntimeException::new);
    }
}
