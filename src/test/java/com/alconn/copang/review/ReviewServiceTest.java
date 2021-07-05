package com.alconn.copang.review;

import static org.junit.jupiter.api.Assertions.*;

import com.alconn.copang.address.Address;
import com.alconn.copang.address.AddressRepository;
import com.alconn.copang.client.Client;
import com.alconn.copang.client.ClientRepo;
import com.alconn.copang.exceptions.NoSuchEntityExceptions;
import com.alconn.copang.exceptions.UnauthorizedException;
import com.alconn.copang.item.ItemDetail;
import com.alconn.copang.item.ItemDetailService;
import com.alconn.copang.order.OrderItem;
import com.alconn.copang.order.OrderRepository;
import com.alconn.copang.order.OrderService;
import com.alconn.copang.order.Orders;
import com.alconn.copang.utils.TestUtils;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class ReviewServiceTest {

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    ReviewService service;

    @Autowired
    ClientRepo repo;

    @Autowired
    OrderService orderService;

    @Autowired
    ReviewRepository repository;

    @Autowired
    ItemDetailService itemDetailService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    TestUtils utils;

    @Autowired
    AddressRepository addressRepository;

    Orders orders;

    Client client;

    ItemDetail detail;

    Address address;
    @Autowired
    EntityManager m;

    @Transactional
    @BeforeEach
    void setUp() {
        client = utils.generateRealClient();
        repo.save(client);
        detail = utils.getItemDetail();
        itemDetailService.itemDetailSave(detail);
        address =
            Address.builder()
            .address("123")
            .client(client)
            .build();
        addressRepository.save(address);

        orders =
            Orders.builder()
                .client(client)
                .address(address)
                .build();

        orders.addOrderItem(OrderItem.builder()
            .amount(1)
            .shippingPrice(14111)
            .itemDetail(detail)
            .build());

        orders = orderRepository.save(orders);
    }

    @AfterEach
    void clean() {
//        repo.deleteAll();
    }


    @Transactional
    @DisplayName("유저 기준으로 리뷰를 가져온다")
    @Test
    void getByUser() {
        saveReview();
        List<ReviewForm.Response> response =
            service.getUserReview(client.getClientId());

        assertEquals(response.size(), 1);

    }


    @DisplayName("아이템 기준으로 리뷰를 가져온다")
    @Test
    void reviewRegister() {
        Review review =
            Review.builder()
            .orderItem(orders.getOrderItemList().get(0))
            .writer(client)
            .satisfied(true)
            .rating(3)
            .content("no")
            .build();
        reviewRepository.save(review);

        assertNotNull(review.getReviewId());
        List<ReviewForm.Response> responses = service.getReviewByItem(detail.getItem().getItemId());

        assertNotNull(responses);
        assertEquals(responses.size(), 1);
    }

    @Disabled
    @Transactional
    @DisplayName("리뷰가 등록이 된다")
    @Test
    void reviewServiceRegister() {


        Orders o = orderRepository.getById(orders.getOrderId());

        assertNotNull(o.getOrderItemList().get(0));
        assertEquals(o.getOrderItemList().size(), 1);
        ReviewForm.Request request =
            ReviewForm.Request.builder()
                .content("없음")
                .orderItemId(orders.getOrderItemList().get(0).getOrderItemId())
//                .itemDetailId(detail.getItemDetailId())
//                .itemId(detail.getItem().getItemId())
                .title("title")
                .rating(4)
                .satisfied(true)
                .build();

        ReviewForm.Response res = service.postReview(request, client.getClientId());

        assertNotNull(res);

        List<ReviewForm.Response> responses = service.getReviewByItem(detail.getItem().getItemId());

        List<ReviewForm.Response> r = service.getUserReview(client.getClientId());
        assertEquals(r.size(), 1);
        assertEquals(responses.size(), 1);
        System.out.println("responses.get(0).getContent() = " + responses.get(0).getContent());
    }

    @Transactional
    @DisplayName("주문 아이템 없이 리뷰저장")
    @Test
    void saveReview() {

        ReviewForm.Request request =
            ReviewForm.Request.builder()
                .content("없음")
                .orderItemId(orders.getOrderItemList().get(0).getOrderItemId())
                .title("title")
                .rating(4)
                .satisfied(true)
                .build();

        service.postReview(request, client.getClientId());

        List<ReviewForm.Response> response =
            service.getUserReview(client.getClientId());

        assertEquals(response.size(), 1);



    }


    @Transactional
    @DisplayName("리뷰가 수정된다")
    @Test
    void updateReview() throws NoSuchEntityExceptions, UnauthorizedException {
        ReviewForm.Request request =
            ReviewForm.Request.builder()
                .content("없음")
                .orderItemId(orders.getOrderItemList().get(0).getOrderItemId())
                .title("title")
                .rating(4)
                .satisfied(true)
                .build();

        ReviewForm.Response response =service.postReview(request, client.getClientId());

        ReviewForm.Update update =
            ReviewForm.Update.builder()
            .rating(0)
            .build();

        service.updateReview(update, client.getClientId(), response.getReviewId());

        ReviewForm.Response result = service.getUserReview(client.getClientId()).get(0);

        assertEquals(result.getRating(), 0);
        assertNotNull(response.getContent());
        assert response.isSatisfied();

    }
}