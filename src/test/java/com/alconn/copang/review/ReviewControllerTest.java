package com.alconn.copang.review;

import static com.alconn.copang.ApiDocumentUtils.getAuthHeaderField;
import static com.alconn.copang.ApiDocumentUtils.getDocumentRequest;
import static com.alconn.copang.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.beneathPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedRequestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.alconn.copang.client.UserForm;
import com.alconn.copang.exceptions.InvalidTokenException;
import com.alconn.copang.exceptions.NoSuchEntityExceptions;
import com.alconn.copang.exceptions.UnauthorizedException;
import com.alconn.copang.order.dto.OrderItemForm;
import com.alconn.copang.utils.TestUtils;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class ReviewControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    ReviewService service;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    TestUtils utils;

    @Test
    void getReviews() {
    }

    @BeforeEach
    void setUp() {
        mapper.setSerializationInclusion(Include.NON_NULL);
    }


    @Test
    void postReview() throws Exception {
        OrderItemForm form =
            OrderItemForm.builder()
                .amount(1)
                .itemDetailId(2L)
                .itemId(3L)
                .itemName("돈까스")
                .optionName("중량")
                .optionValue("500G")
                .build();

        UserForm.Response u =
            UserForm.Response.builder()
                .realName("홍*동")
                .clientId(1L)
                .build();
        ReviewForm.Response response =
            ReviewForm.Response.builder()
                .content("맛있어요")
                .reviewId(1L)
                .image("")
                .registerDate(LocalDateTime.now())
                .satisfied(true)
                .amount(1)
                .orderItemId(4L)
                .itemDetailId(2L)
                .itemId(3L)
                .itemName("돈까스")
                .title("한줄평")
                .optionName("중량")
                .optionValue("500G")
                .writerName(u.getRealName())
                .rating(5)
                .itemName("돈까")
                .build();

        ReviewForm.Request request =
            ReviewForm.Request.builder()
                .content("맛있어요")
                .image("imageUrl")
                .title("한줄평")
                .orderItemId(4L)
                .satisfied(true)
                .rating(5)
                .itemDetailId(2L)
                .itemId(3L)
                .build();

        given(service.postReview(any(ReviewForm.Request.class), eq(1L))).willReturn(response);

        ResultActions result =
            this.mvc.perform(
                post("/api/review/register")
                    .content(mapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, utils.genAuthHeader())
            );

        result.andExpect(status().isCreated())
            .andDo(print())
            .andDo(
                document(
                    "review/post",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    getAuthHeaderField(),
                    relaxedRequestFields(
                        fieldWithPath("content").type(JsonFieldType.STRING).description("리뷰 내용"),
                        fieldWithPath("title").type(JsonFieldType.STRING).description("한줄평"),
                        fieldWithPath("image").type(JsonFieldType.STRING).description("리뷰사진"),
                        fieldWithPath("orderItemId").type(JsonFieldType.NUMBER)
                            .description("주문 상품 아이디"),
                        fieldWithPath("itemDetailId").type(JsonFieldType.NUMBER)
                            .description("옵션상품 아이디"),
                        fieldWithPath("itemId").type(JsonFieldType.NUMBER).description("상품 아이디"),
                        fieldWithPath("rating").type(JsonFieldType.NUMBER).description("별점"),
                        fieldWithPath("satisfied").type(JsonFieldType.BOOLEAN)
                            .description("만족 TRUE FALSE")
                    ),
                    relaxedResponseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("결과코드"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("등록한 리뷰")
                    ),
                    relaxedResponseFields(
                        beneathPath("data").withSubsectionId("data"),
                        fieldWithPath("writerName").type(JsonFieldType.STRING)
                            .description("작성자 이름"),
                        fieldWithPath("content").type(JsonFieldType.STRING).description("리뷰 내용"),
                        fieldWithPath("title").type(JsonFieldType.STRING).description("한줄평"),
                        fieldWithPath("image").type(JsonFieldType.STRING).description("리뷰사진"),
                        fieldWithPath("registerDate").type(JsonFieldType.STRING)
                            .description("리뷰 등록 날짜"),
                        fieldWithPath("reviewId").type(JsonFieldType.NUMBER).description("리뷰 아이디"),
                        fieldWithPath("orderItemId").type(JsonFieldType.NUMBER)
                            .description("주문 상품 아이디"),
                        fieldWithPath("itemDetailId").type(JsonFieldType.NUMBER)
                            .description("옵션상품 아이디"),
                        fieldWithPath("itemId").type(JsonFieldType.NUMBER).description("상품 아이디"),
                        fieldWithPath("rating").type(JsonFieldType.NUMBER).description("별점"),
                        fieldWithPath("satisfied").type(JsonFieldType.BOOLEAN)
                            .description("만족 TRUE FALSE"),
                        fieldWithPath("itemName").type(JsonFieldType.STRING).description("상품명"),
                        fieldWithPath("optionName").type(JsonFieldType.STRING).description("옵션명"),
                        fieldWithPath("optionValue").type(JsonFieldType.STRING).description("옵션값"),
                        fieldWithPath("amount").type(JsonFieldType.NUMBER).description("개별 상품수량")
                    )
                )
            );

        List<ReviewForm.Response> responses = Collections.singletonList(response);
        given(service.getUserReview(eq(1L))).willReturn(responses);

        this.mvc.perform(
            get("/api/review/user")
                .header(HttpHeaders.AUTHORIZATION, utils.genAuthHeader())
        )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(
                document(
                    "review/user",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    getAuthHeaderField()
                ));
    }

    @Test
    void updateReview()
        throws Exception {
        ReviewForm.Response response =
            ReviewForm.Response.builder()
                .content("맛있어요")
                .reviewId(1L)
                .image("")
                .registerDate(LocalDateTime.now())
                .satisfied(true)
                .amount(1)
                .orderItemId(4L)
                .itemDetailId(2L)
                .itemId(3L)
                .itemName("돈까스")
                .title("한줄평")
                .optionName("중량")
                .optionValue("500G")
                .writerName("홍*동")
                .rating(5)
                .itemName("돈까")
                .build();

        given(service.updateReview(any(ReviewForm.Update.class), eq(1L), eq(2L))).willReturn(response);

        ReviewForm.Update update =
            ReviewForm.Update.builder()
                .content("다시생각해보니 별로에요")
                .rating(0)
                .satisfied(false)
                .image("no image")
                .rating(0)
                .build();

            this.mvc.perform(
                put("/api/review/{reviewId}", 2L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(update))
                    .header(HttpHeaders.AUTHORIZATION, utils.genAuthHeader())
            )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(
                    document(
                        "review/{method-name}",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        getAuthHeaderField(),
                        relaxedRequestFields(
                            fieldWithPath("content").type(JsonFieldType.STRING).description("리뷰 내용").optional(),
                            fieldWithPath("title").type(JsonFieldType.STRING).description("한줄평").optional(),
                            fieldWithPath("image").type(JsonFieldType.STRING).description("리뷰사진").optional(),
                            fieldWithPath("rating").type(JsonFieldType.NUMBER).description("별점").optional(),
                            fieldWithPath("satisfied").type(JsonFieldType.BOOLEAN)
                                .description("만족 TRUE FALSE").optional()
                        ),
                        relaxedResponseFields(
                            fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                            fieldWithPath("code").type(JsonFieldType.NUMBER).description("결과코드"),
                            fieldWithPath("data").type(JsonFieldType.OBJECT).description("업데이트 된 리뷰")
                        )
                ));


    }
}