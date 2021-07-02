package com.alconn.copang.question;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.alconn.copang.client.Client;
import com.alconn.copang.client.ClientRepo;
import com.alconn.copang.item.Item;
import com.alconn.copang.item.ItemDetail;
import com.alconn.copang.item.ItemDetailRepository;
import com.alconn.copang.item.ItemRepository;
import com.alconn.copang.seller.Seller;
import com.alconn.copang.seller.SellerRepository;
import com.alconn.copang.utils.TestUtils;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class QuestionRepositoryTest {

    @Autowired
    InquiryRepository repository;

    @Autowired
    ItemDetailRepository detailRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ClientRepo repo;

    @Autowired
    TestUtils utils;

    @Autowired
    EntityManager m;

    @Autowired
    SellerRepository sellerRepository;

    @Transactional
    @Test
    void save() {

        Client client = utils.generateRealClient();
        repo.save(client);

        Item item = Item.builder()
            .itemName("name")
            .build();

        itemRepository.save(item);
        ItemDetail detail =
            ItemDetail.builder()
                .item(item)
                .optionName("adsf")
                .optionValue("asdf")
                .price(123)
                .stockQuantity(123)
                .mainImg("1234")
                .build();

        detailRepository.save(detail);
        Inquiry inquiry =
            Inquiry.builder()
                .content("hello")
                .client(client)
                .itemDetail(detail)
                .build();

        repository.save(inquiry);

        m.flush();
        m.clear();
        System.out.println("inquiry.getInquiryId() = " + inquiry.getInquiryId());

        Seller seller = utils.getSeller();
        sellerRepository.save(seller);

        Reply reply1 =
            Reply.builder()
            .answer("answer")
            .seller(seller)
            .build();

        Inquiry origin =
            repository.getById(inquiry.getInquiryId());

//        origin.reply(reply);

        origin.reply(reply1);
        m.flush();
        m.clear();

        ItemDetail detail1 = detailRepository.getById(detail.getItemDetailId());
        System.out.println("detail1.getItem().getItemName() = " + detail1.getItem().getItemName());
        List<Inquiry> res =
            repository.findInquiriesByItemDetail_Item_ItemId(detail1.getItem().getItemId());

        assertEquals(res.size(), 1);

        System.out.println("res.get(0).getContent() = " + res.get(0).getContent());
        System.out.println("res = " + res.get(0).getReply().getAnswer());



    }
}