package com.alconn.copang.wishlist;

import com.alconn.copang.auth.AccessTokenContainer;
import com.alconn.copang.auth.LoginToken;
import com.alconn.copang.client.Client;
import com.alconn.copang.client.ClientRepo;
import com.alconn.copang.client.ClientService;
import com.alconn.copang.client.Role;
import com.alconn.copang.exceptions.InvalidTokenException;
import com.alconn.copang.exceptions.LoginFailedException;
import com.alconn.copang.exceptions.NoSuchEntityExceptions;
import com.alconn.copang.item.Item;
import com.alconn.copang.item.ItemDetail;
import com.alconn.copang.item.ItemDetailRepository;
import com.alconn.copang.item.ItemRepository;
import com.alconn.copang.utils.TestUtils;
import com.alconn.copang.wishlist.dto.WishRequest;
import com.alconn.copang.wishlist.dto.WishResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.NoSuchElementException;

@SpringBootTest
public class WishServiceTest {

    @Autowired
    private WishService wishService;

    @Autowired
    private ItemRepository itemRepo;

    @Autowired
    private ItemDetailRepository itemDetailRepository;

    @Autowired
    private ClientRepo clientRepo;

    @Autowired
    private WishRepository wishRepository;


    @Autowired
    ClientService clientService;



    private void testData() {
        Item item = Item.builder()
                .itemName("테스트")
                .build();

        itemRepo.save(item);

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
    }

    @Transactional
    @Test
    public void addTest() throws NoSuchEntityExceptions {
        testData();
        Client client=Client.builder()
                .password("3371")
                .role(Role.CLIENT)
                .username("아아아아아아앙앙ㄱ")
                .build();
        clientRepo.save(client);

        WishRequest wishRequest= WishRequest.builder()
                .itemDetailId(itemDetailRepository.findAll().get(0).getItemDetailId())
                .build();
        wishService.add(wishRequest,client.getClientId());

    }

    @Transactional
    @Test
    public void listTest() throws NoSuchEntityExceptions {
        //추가
        addTest();
        Long client=clientRepo.findAll().get(0).getClientId();

        //출력
        List<WishResponse> responses= wishService.list(client);
    }
    @Transactional
    @Test
    public void delTest() throws NoSuchEntityExceptions{
        testData();
        Client client=Client.builder()
                .password("11111")
                .role(Role.CLIENT)
                .username("이클라이언트는제것입니다")
                .build();
        clientRepo.save(client);

        WishRequest wishRequest2= WishRequest.builder()
                .itemDetailId(itemDetailRepository.findAll().get(0).getItemDetailId())
                .build();
        wishService.add(wishRequest2,client.getClientId());


        WishRequest wishRequest=WishRequest.builder()
                .wishId(wishRepository.findAll().get(0).getWishId())
                .build();
        WishResponse wishResponse=wishService.del(wishRequest,client.getClientId());
        System.out.println("삭제된 데이터"+wishResponse.toString());
    }
}
