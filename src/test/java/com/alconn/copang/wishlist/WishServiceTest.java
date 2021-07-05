//package com.alconn.copang.wishlist;
//
//import com.alconn.copang.client.Client;
//import com.alconn.copang.client.ClientRepo;
//import com.alconn.copang.exceptions.NoSuchEntityExceptions;
//import com.alconn.copang.item.Item;
//import com.alconn.copang.item.ItemDetail;
//import com.alconn.copang.item.ItemDetailRepository;
//import com.alconn.copang.item.ItemRepository;
//import com.alconn.copang.wishlist.dto.WishRequest;
//import com.alconn.copang.wishlist.dto.WishResponse;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.persistence.EntityManager;
//import java.util.List;
//import java.util.NoSuchElementException;
//
//@SpringBootTest
//@Transactional
//public class WishServiceTest {
//
//    @Autowired
//    private WishService wishService;
//
//    @Autowired
//    private ItemRepository itemRepo;
//
//    @Autowired
//    private ItemDetailRepository itemDetailRepository;
//
//    @Autowired
//    private ClientRepo clientRepo;
//
//    @Autowired
//    private WishRepository wishRepository;
//
//    @Autowired
//    private EntityManager em;
//
//
//    private void testData() {
//        Item item = Item.builder()
//                .itemName("테스트")
//                .build();
//
//        itemRepo.save(item);
//
//        ItemDetail itemDetail = ItemDetail.builder()
//                .mainImg("메인사진")
//                .item(item)
//                .stockQuantity(10)
//                .optionName("색상")
//                .optionValue("검은색")
//                .mainImg("메인사진")
//                .price(10000)
//                .build();
//        itemDetail.itemConnect(item);
//        itemDetailRepository.save(itemDetail);
//
//        Client client = Client.builder()
//                .username("테스트유저")
//                .password("123")
//                .build();
//        clientRepo.save(client);
//
//    }
//
//    private WishRequest testWishDate(){
//        testData();
//        Long client=clientRepo.findAll().get(0).getClientId();
//        WishRequest wishRequest= WishRequest.builder()
//                .clientId(client)
//                .itemDetailId(itemDetailRepository.findAll().get(0).getItemDetailId())
//                .build();
//        return wishRequest;
//    }
//
//    @Test
//    public void addTest() throws NoSuchEntityExceptions {
//        WishRequest withRequest=testWishDate();
//        wishService.add(withRequest);
//
//    }
//
//    @Test
//    public void listTest() throws NoSuchEntityExceptions {
//        //추가
//        addTest();
//        Long client=clientRepo.findAll().get(0).getClientId();
//
//        //출력
//        WishRequest.WishRequestlist wishRequestlist= WishRequest.WishRequestlist.builder()
//                .clientId(client)
//                .build();
//        List<WishResponse> responses= wishService.list(wishRequestlist);
//        System.out.println("responses.get(0) = " + responses.get(0).toString());
//    }
//
//    @Test
//    public void delTest() throws NoSuchEntityExceptions{
//        addTest();
//        Long client=clientRepo.findAll().get(0).getClientId();
//
//        WishRequest wishRequest=WishRequest.builder()
//                .wishId(wishRepository.findAll().get(0).getWishId())
//                .clientId(client)
//                .build();
//        WishResponse wishResponse=wishService.del(wishRequest);
//        System.out.println("삭제된 데이터"+wishResponse.toString());
//    }
//
//    @Test
//    public void delListTest() throws NoSuchEntityExceptions{
//        addTest();
//        Long client=clientRepo.findAll().get(0).getClientId();
//
//
//
//
//
//        WishRequest.WishRequestdel wishRequestdel= WishRequest.WishRequestdel.builder()
//                .clientId(client)
//                .wishId()
//
//                .build();
//
//    }
//
//
//
//}
