package com.alconn.copang.item;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.alconn.copang.address.Address;
import com.alconn.copang.address.AddressRepository;
import com.alconn.copang.client.Client;
import com.alconn.copang.client.ClientRepo;
import com.alconn.copang.item.dto.ItemDetailForm.MainForm;
import com.alconn.copang.item.dto.ItemViewForm.MainViewForm;
import com.alconn.copang.order.OrderItem;
import com.alconn.copang.order.OrderRepository;
import com.alconn.copang.order.Orders;
import com.alconn.copang.order.dto.OrderItemForm;
import com.alconn.copang.review.Review;
import com.alconn.copang.review.ReviewRepository;
import com.alconn.copang.search.ItemSearchCondition;
import com.alconn.copang.search.OrderCondition;
import com.alconn.copang.search.SearchService;
import com.alconn.copang.seller.Seller;
import com.alconn.copang.seller.SellerRepository;
import com.alconn.copang.utils.TestUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@AutoConfigureMockMvc
@SpringBootTest
class ItemQueryRepositoryTest {

    @Autowired
    EntityManager m;

    @Autowired
    ItemQueryRepository itemQueryRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemDetailRepository itemDetailRepository;

    @Autowired
    MockMvc mvc;

    @Autowired
    TestUtils utils;

    @Autowired
    ClientRepo clientRepo;

    @Autowired
    SearchService service;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    SellerRepository sellerRepository;

    @BeforeEach
    void setUp() {

    }

    @Transactional
    @Test
    void searchTest() throws Exception {

        String[] names = new String[]{
            "감자", "고구마", "빼빼로", "삼양라면", "진라면", "불닭볶음면"
            , "열라면"
        };
        int[] prices = new int[]{
            2000, 4000, 5000, 20000, 10000, 8000, 50000
        };
        Iterator<Integer> iter = Arrays.stream(prices).iterator();

        List<Item> items =
            Arrays.stream(names).map(s ->
                Item.builder()
                    .itemName(s)
                    .build()).collect(Collectors.toList());

        List<ItemDetail> detailsToSave =
            items.stream().map(i -> ItemDetail.builder()
                .mainImg("123")
                .item(i)
                .optionValue("rm")
                .optionName("ASdf")
                .price(iter.hasNext() ? iter.next() : 200)
                .itemMainApply(ItemMainApply.APPLY)
                .stockQuantity(123)
                .build()
            ).collect(Collectors.toList());

        detailsToSave.forEach(d -> d.itemConnect(d.getItem()));

        itemRepository.saveAll(items);
        itemDetailRepository.saveAll(detailsToSave);

        List<ItemDetail> details1 = itemQueryRepository.searchByKeywords("라면+고구마", 0, 40);


        System.out.println("details1.size() = " + details1.size());
        details1.forEach(d -> System.out
            .println("d.getItem().getItemName() = " + d.getItem().getItemName()));


        assertEquals(4, details1.size());

        MultiValueMap<String, String> params= new LinkedMultiValueMap<>();
        params.add("keyword", "라면+고구마");
        this.mvc.perform(
            get(
                "/api/item/search?keyword={keyword}&priceOver={price}", "라면+고구마", 10000)
            .characterEncoding("utf-8")
            .queryParam("priceUnder", String.valueOf(40000))
//            .params(params)
//            .queryParam("keyword", "라면+고구마")
//            .queryParam("keyword", "h+123")
        )
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    void dateFormatTest() throws Exception {
        String date = "2020-08-09";
//        String endDate = "2021-08-09";
        long endDate = System.currentTimeMillis();
        this.mvc.perform(
            get(

                "/search"
            )
            .characterEncoding("utf-8")
            .queryParam("startDate", Long.toString(endDate))
            .queryParam("endDate", Long.toString(endDate))
        )
            .andDo(print());
    }

    @Test
    void nullTest() throws Exception {

        this.mvc.perform(
            get("/api/item/search")
        )
            .andDo(print());
    }

//    @Disabled
    @Transactional
    @DisplayName("평균 별점 수집")
    @Test
    void withReviewRating() {
        Client client = utils.generateRealClient();
        Seller seller = utils.getSeller();
        Address address = utils.getAddress(client);

        String[] names = new String[]{
            "감자", "고구마", "빼빼로", "삼양라면", "진라면", "불닭볶음면"
            , "열라면"
        };
        int[] prices = new int[]{
            2000, 4000, 5000, 20000, 10000, 8000, 50000
        };
        Iterator<Integer> iter = Arrays.stream(prices).iterator();

        List<Item> items =
            Arrays.stream(names).map(s ->
                Item.builder()
                    .itemName(s)
                    .build()).collect(Collectors.toList());

        List<ItemDetail> detailsToSave =
            items.stream().map(i -> ItemDetail.builder()
                .mainImg("123")
                .item(i)
                .optionValue("rm")
                .optionName("ASdf")
                .price(iter.hasNext() ? iter.next() : 200)
                .itemMainApply(ItemMainApply.APPLY)
                .stockQuantity(123)
                .build()
            ).collect(Collectors.toList());

        clientRepo.save(client);
        sellerRepository.save(seller);
        addressRepository.save(address);
        itemRepository.saveAll(items);
        itemDetailRepository.saveAll(detailsToSave);

        List<OrderItem> orderItems =
            detailsToSave.stream().map(d ->
                OrderItem.builder()
            .amount(4)
            .itemDetail(ItemDetail.builder().itemDetailId(d.getItemDetailId()).build())
                .build()
            ).collect(Collectors.toList());

        Orders orders =
            Orders.builder()
            .client(client)
            .address(address)
            .orderItemList(orderItems)
            .build();
        orders.connectOrderItems();
        orderRepository.save(orders);
        Random random = new Random();

        List<Review> reviews =
            orderItems.stream().map(o -> Review.builder()
            .content("좋아요")
            .rating(random.nextInt(5))
                .orderItem(
                    o
                )
                .writer(client)
                .build()
            ).collect(Collectors.toList());
        reviews.addAll(
            orderItems.subList(1, 3).stream().map(o -> Review.builder()
                .content("좋아요")
                .rating(random.nextInt(3))
                .orderItem(
                    o
                )
                .writer(client)
                .build()
            ).collect(Collectors.toList())
        );

        reviewRepository.saveAll(reviews);

        m.flush();
        m.clear();

//        List<ItemDetail> itemWithReview = itemQueryRepository.se();
//        itemWithReview.forEach(i -> System.out
//            .println("i.getItem().getAverageRating() = " + i.getItem().getAverageRating()));

        ItemSearchCondition condition =
            ItemSearchCondition.builder()
//            .sorted(OrderItemCondition.ranking)
                .sorted(OrderCondition.review)
            .build();

//        MainViewForm search = service.search(new ItemSearchCondition());
        MainViewForm search = service.search(condition);

        System.out.println("search.getTotalCount() = " + search.getTotalCount());
        for (MainForm m: search.getList()) {
            System.out.println("m.getAverageRating() = " + m.getAverageRating());
            System.out.println("m.getReviews() = " + m.getCountReviews());
            System.out.println("m.getCountOrderItems() = " + m.getCountOrderItems());
        }
    }
}