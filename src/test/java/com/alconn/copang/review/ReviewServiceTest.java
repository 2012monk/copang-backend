package com.alconn.copang.review;

import static org.junit.jupiter.api.Assertions.*;

import com.alconn.copang.address.Address;
import com.alconn.copang.address.AddressRepository;
import com.alconn.copang.client.Client;
import com.alconn.copang.client.ClientRepo;
import com.alconn.copang.item.Item;
import com.alconn.copang.item.ItemDetail;
import com.alconn.copang.item.ItemDetailRepository;
import com.alconn.copang.item.ItemRepository;
import com.alconn.copang.order.OrderItem;
import com.alconn.copang.order.OrderRepository;
import com.alconn.copang.order.Orders;
import com.alconn.copang.utils.TestUtils;
import java.util.Collections;
import javax.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Disabled
@Slf4j
@SpringBootTest
class ReviewServiceTest {

    @Autowired
    ReviewService service;

    @Autowired
    ReviewRepository repository;

    @Autowired
    ClientRepo repo;

    @Autowired
    ItemDetailRepository detailRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    TestUtils utils;

    @Autowired
    EntityManager m;

    private Item getItem(String itemName) {
        return Item.builder()
            .itemName(itemName)
            .build();
    }

    @Transactional
    @DisplayName("리뷰를 쓴다!")
    @Test
    void writeReview() {
        Item item = getItem("빨강양말");
        ItemDetail detail =
            buildDetail(item);

        Client client = utils.generateMockClient();

        Address address =
            Address.builder()
            .address("124444")
            .detail("1451")
            .receiverName("dnknk")
            .receiverPhone("141--14010402")
            .client(client)
            .build();
        repo.save(client);

        addressRepository.save(address);
        itemRepository.save(item);

        detailRepository.save(detail);

        Orders orders =
            Orders.builder()
            .client(client)
            .address(address)
            .build();

        orders.addOrderItem(
            OrderItem.builder()
                .amount(14)
                .itemDetail(detail)
                .shippingPrice(4000000)
                .build()
        );
        orderRepository.save(orders);

        log.info("itemId {}, detailId {}",detail.getItem().getItemId(), detail.getItemDetailId());
        m.flush();
        m.clear();

        OrderItem item1 = orderRepository.getById(orders.getOrderId()).getOrderItemList().get(0);

        Review review =
            Review.builder()
            .content("fdnfkndfkndkfnkans")
            .rating(5)
            .image("no")
            .writer(client)
            .satisfied(true)
            .orderItem(item1)
            .build();

        repository.save(review);

        m.flush();
        m.clear();
        Review review1 = repository.getById(review.getReviewId());

        log.info("review :{} {}", review1.getReviewId(), review1.getContent());



    }

    private ItemDetail buildDetail(Item item) {
        return ItemDetail.builder()
            .price(4000)
            .mainImg("123")
            .stockQuantity(414)
            .optionName("색상")
            .optionValue("빨깡")
            .item(item)
            .build();
    }
}