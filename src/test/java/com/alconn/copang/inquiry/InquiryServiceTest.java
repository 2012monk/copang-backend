package com.alconn.copang.inquiry;

import static org.junit.jupiter.api.Assertions.*;

import com.alconn.copang.client.Client;
import com.alconn.copang.client.ClientRepo;
import com.alconn.copang.exceptions.NoSuchEntityExceptions;
import com.alconn.copang.exceptions.UnauthorizedException;
import com.alconn.copang.exceptions.ValidationException;
import com.alconn.copang.inquiry.InquiryForm.Request;
import com.alconn.copang.inquiry.InquiryForm.Response;
import com.alconn.copang.item.Item;
import com.alconn.copang.item.ItemDetail;
import com.alconn.copang.item.ItemDetailService;
import com.alconn.copang.seller.Seller;
import com.alconn.copang.seller.SellerRepository;
import com.alconn.copang.utils.TestUtils;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class InquiryServiceTest {

    @Autowired
    InquiryService service;

    @Autowired
    ItemDetailService detailService;

    @Autowired
    TestUtils utils;

    ItemDetail detail;

    Client client;

    Seller seller;

    @Autowired
    ClientRepo repo;

    @Autowired
    SellerRepository sellerRepository;

    @Autowired
    InquiryMapper mapper;

    @BeforeEach
    void setUp() {
        detail = utils.getItemDetail();


        client = utils.generateRealClient();
        seller = utils.getSeller();
        repo.save(client);
        sellerRepository.save(seller);

        Item item =
            Item.builder()
                .itemName("신발1")
                .seller(seller)
                .build();

        ReflectionTestUtils.setField(detail, "item", item);

        detailService.itemDetailSave(detail);
    }

    @AfterEach
    void cleanUp() {

    }

    @Test
    void saveInquiry() {
        InquiryForm.Request request =
            InquiryForm.Request.builder()
            .content("문의사항")
            .itemDetailId(detail.getItemDetailId())
            .build();

        InquiryForm.Response response = service.registerInquiry(request, client.getClientId());

        System.out.println("response.getContent() = " + response.getContent());
        assertNotNull(response);
        assertEquals(request.getContent(), response.getContent());
    }

    @Test
    void registerReply() throws NoSuchEntityExceptions, ValidationException {
        InquiryForm.Request request =
            InquiryForm.Request.builder()
                .content("문의사항")
                .itemDetailId(detail.getItemDetailId())
                .build();

        InquiryForm.Response response = service.registerInquiry(request, client.getClientId());

        InquiryForm.Request req =
            InquiryForm.Request.builder()
            .content("hello")
            .build();

        Response response1 = service
            .registerReply(req, seller.getClientId(), response.getInquiryId());


        assertNotNull(response1.getContent());

        List<Response> res = service.getInquiresByClient(client.getClientId());

        assertEquals(1, res.size());
        assertNotNull(res.get(0).getReply());

        System.out
            .println("res.get(0).getReply().getContent() = " + res.get(0).getReply().getContent());

    }

    @Test
    void name() {
        InquiryForm.Request request =
            InquiryForm.Request.builder()
                .content("문의사항")
                .itemDetailId(detail.getItemDetailId())
                .build();

        InquiryForm.Response response = service.registerInquiry(request, client.getClientId());

        List<Response> inquiresByClient = service.getInquiresByClient(client.getClientId());

        assertEquals(1, inquiresByClient.size());

        assertEquals(request.getContent(), inquiresByClient.get(0).getContent());
    }

    private Response saveAndReturnInquiry() {
        InquiryForm.Request request =
            InquiryForm.Request.builder()
                .content("문의사항")
                .itemDetailId(detail.getItemDetailId())
                .build();

        return service.registerInquiry(request, client.getClientId());
    }

    @Test
    void getInquiryByItem() {
        Response response = saveAndReturnInquiry();

        List<Response> inquiresItem = service.getInquiresByItem(detail.getItem().getItemId());

        assertEquals(1, inquiresItem.size());
        assertNotNull(inquiresItem.get(0).getContent());
    }

    @Test
    void mappingtest() {

        InquiryForm.Request request =
            InquiryForm.Request.builder()
                .content("문의사항")
                .itemDetailId(detail.getItemDetailId())
                .build();
        Response response = mapper.toDto(
            Inquiry.builder()
            .content("123")
            .build()
        );

        System.out.println("response.getContent() = " + response.getContent());
        assertNotNull(response.getContent());
    }

    @DisplayName("문의가 업데이트된다")
    @Test
    void updateInquiryTest() throws NoSuchEntityExceptions, UnauthorizedException {
        Response response = saveAndReturnInquiry();

        Request request =
            Request.builder()
            .content("updated")
            .build();

        System.out.println("response = " + response.getInquiryId());
        Response response1 = service
            .updateInquiry(request, response.getInquiryId(), client.getClientId());

        assertEquals(request.getContent(), response1.getContent());
        assertNotEquals(response1.getContent(),response.getContent());

    }

    @Test
    void getSellerInquires() {
        InquiryForm.Request request =
            InquiryForm.Request.builder()
                .content("문의사항")
                .itemDetailId(detail.getItemDetailId())
                .build();

        InquiryForm.Response response = service.registerInquiry(request, client.getClientId());

        List<Response> in = service.getInquiresBySeller(seller.getClientId());

        assertEquals(1, in.size());
    }
}