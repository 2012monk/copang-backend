package com.alconn.copang.inquiry;

import static com.alconn.copang.ApiDocumentUtils.getAuthHeaderField;
import static com.alconn.copang.ApiDocumentUtils.getDocumentRequest;
import static com.alconn.copang.ApiDocumentUtils.getDocumentResponse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.beneathPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedRequestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.alconn.copang.client.Client;
import com.alconn.copang.client.ClientRepo;
import com.alconn.copang.exceptions.InvalidTokenException;
import com.alconn.copang.exceptions.NoSuchEntityExceptions;
import com.alconn.copang.exceptions.UnauthorizedException;
import com.alconn.copang.exceptions.ValidationException;
import com.alconn.copang.inquiry.InquiryForm.Request;
import com.alconn.copang.inquiry.InquiryForm.Response;
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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class InquiryControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    InquiryService service;

    @Autowired
    TestUtils utils;

    @Autowired
    ObjectMapper mapper;

    final String PATH = "inquiry/{method-name}";

    @BeforeEach
    void setUp() {
        mapper.setSerializationInclusion(Include.NON_NULL);
    }


    @Test
    void updateReply() throws Exception {
        Response res = getMockResponse();

        ReflectionTestUtils.setField(res.getReply(), "content", "수정된 문의");

        Request request =
            Request.builder()
                .content(res.getReply().getContent())
                .build();

        given(this.service.updateReply(any(Request.class), eq(res.getInquiryId()), eq(1L) )).willReturn(res);

        this.mvc.perform(
            RestDocumentationRequestBuilders.
            put("/api/inquiry/{inquiryId}/reply", res.getInquiryId())
            .content(mapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, utils.getSellerAuthHeader())
        )
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(
                document(
                    PATH,
                    getDocumentRequest(),
                    getDocumentResponse(),
                    getAuthHeaderField(),
                    pathParameters(
                        parameterWithName("inquiryId").description("문의글 아이디")
                    ),
                    relaxedRequestFields(
                        fieldWithPath("content").type(JsonFieldType.STRING).description("수정하실 답변내용")
                    ),
                    relaxedResponseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메세지"),
                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("결과 코드"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터")
                    ),
                    relaxedResponseFields(
                        beneathPath("data").withSubsectionId("data"),
                        fieldWithPath("content").description(JsonFieldType.STRING).description("문의사항"),
                        fieldWithPath("clientName").description(JsonFieldType.STRING).description("등록자 이름"),
                        fieldWithPath("itemName").description(JsonFieldType.STRING).description("문의한 상품 이름"),
                        fieldWithPath("optionName").description(JsonFieldType.STRING).description("문의한 옵션이름"),
                        fieldWithPath("optionValue").description(JsonFieldType.STRING).description("문의한 옵션값"),
                        fieldWithPath("registerDate").description(JsonFieldType.STRING).description("등록일"),
                        fieldWithPath("itemDetailId").description(JsonFieldType.NUMBER).description("옵션아이디"),
                        fieldWithPath("inquiryId").description(JsonFieldType.NUMBER).description("문의글 아이디"),
                        fieldWithPath("reply.content").type(JsonFieldType.STRING).description("답변 내용"),
                        fieldWithPath("reply.sellerName").type(JsonFieldType.STRING).description("판매자 명"),
                        fieldWithPath("reply.registerDate").type(JsonFieldType.STRING).description("등록일"),
                        fieldWithPath("reply.sellerCode").type(JsonFieldType.NUMBER).description("판매자코드"),
                        fieldWithPath("reply.replyId").type(JsonFieldType.NUMBER).description("답변아이디")
                    )
                )
            );
    }

    @Test
    void updateInquiry() throws Exception {
        Response res = getMockResponse();

        ReflectionTestUtils.setField(res, "content", "수정된 문의");

        Request request =
            Request.builder()
            .content(res.getContent())
            .build();

        given(this.service.updateInquiry(any(InquiryForm.Request.class), eq(res.getInquiryId()), eq(1L))).willReturn(res);

        this.mvc.perform(
            RestDocumentationRequestBuilders.
            put("/api/inquiry/{inquiryId}", res.getInquiryId())
            .header(HttpHeaders.AUTHORIZATION, utils.genAuthHeader())
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(request))
        ).andExpect(status().isOk())
            .andDo(print())
            .andDo(
                document(
                    PATH,
                    getDocumentRequest(),
                    getDocumentResponse(),
                    getAuthHeaderField(),
                    pathParameters(
                        parameterWithName("inquiryId").description("문의글 아이디")
                    ),
                    relaxedRequestFields(
                        fieldWithPath("content").type(JsonFieldType.STRING).description("수정하실 내용")
                    ),
                    relaxedResponseFields(),
                    relaxedResponseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메세지"),
                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("결과 코드"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터")
                    ),
                    relaxedResponseFields(
                        beneathPath("data").withSubsectionId("data"),
                        fieldWithPath("content").description(JsonFieldType.STRING).description("문의사항"),
                        fieldWithPath("clientName").description(JsonFieldType.STRING).description("등록자 이름"),
                        fieldWithPath("itemName").description(JsonFieldType.STRING).description("문의한 상품 이름"),
                        fieldWithPath("optionName").description(JsonFieldType.STRING).description("문의한 옵션이름"),
                        fieldWithPath("optionValue").description(JsonFieldType.STRING).description("문의한 옵션값"),
                        fieldWithPath("registerDate").description(JsonFieldType.STRING).description("등록일"),
                        fieldWithPath("itemDetailId").description(JsonFieldType.NUMBER).description("옵션아이디"),
                        fieldWithPath("inquiryId").description(JsonFieldType.NUMBER).description("문의글 아이디"),
                        fieldWithPath("reply.content").type(JsonFieldType.STRING).description("답변 내용"),
                        fieldWithPath("reply.sellerName").type(JsonFieldType.STRING).description("판매자 명"),
                        fieldWithPath("reply.registerDate").type(JsonFieldType.STRING).description("등록일"),
                        fieldWithPath("reply.sellerCode").type(JsonFieldType.NUMBER).description("판매자코드"),
                        fieldWithPath("reply.replyId").type(JsonFieldType.NUMBER).description("답변아이디")
                    )
                )
            );
    }

    @Test
    void getInquiryBySeller() throws Exception {
        Response res = getMockResponse();

        List<Response> ress = Collections.singletonList(res);
        given(this.service.getInquiresBySeller(eq(1L))).willReturn(ress);

        this.mvc.perform(
            get("/api/inquiry/seller")
                .header(HttpHeaders.AUTHORIZATION, utils.getSellerAuthHeader())
        )
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(
                document(
                    PATH,
                    getDocumentRequest(),
                    getDocumentResponse(),
                    getAuthHeaderField()
                )
            );
    }

    @Test
    void getInquiryByClient() throws Exception {
        Response res = getMockResponse();

        List<Response> ress = Collections.singletonList(res);
        given(this.service.getInquiresByClient(eq(1L))).willReturn(ress);

        this.mvc.perform(
            get("/api/inquiry/client")
            .header(HttpHeaders.AUTHORIZATION, utils.genAuthHeader())
        )
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(
                document(
                    PATH,
                    getDocumentRequest(),
                    getDocumentResponse(),
                    getAuthHeaderField()
                )
            );
    }

    @Test
    void getInquiryById() throws Exception {
        Response res = getMockResponse();

        given(this.service.getInquiresByItem(eq(res.getItemId()))).willReturn(Collections.singletonList(res));
        this.mvc.perform(
            RestDocumentationRequestBuilders.
            get("/api/inquiry/{itemId}/item", res.getItemId())
        ).andExpect(status().isOk())
            .andDo(print())
            .andDo(
                document(
                    PATH,
                    getDocumentRequest(),
                    getDocumentResponse(),
                    pathParameters(
                        parameterWithName("itemId").description("조회하실 상품 아이디")
                    ),
                    relaxedResponseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메세지"),
                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("결과 코드"),
                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("결과 데이터")
                    ),
                    relaxedResponseFields(
                        beneathPath("data[]").withSubsectionId("data"),
                        fieldWithPath("content").description(JsonFieldType.STRING).description("문의사항"),
                        fieldWithPath("clientName").description(JsonFieldType.STRING).description("등록자 이름"),
                        fieldWithPath("itemName").description(JsonFieldType.STRING).description("문의한 상품 이름"),
                        fieldWithPath("optionName").description(JsonFieldType.STRING).description("문의한 옵션이름"),
                        fieldWithPath("optionValue").description(JsonFieldType.STRING).description("문의한 옵션값"),
                        fieldWithPath("registerDate").description(JsonFieldType.STRING).description("등록일"),
                        fieldWithPath("itemDetailId").description(JsonFieldType.NUMBER).description("옵션아이디"),
                        fieldWithPath("inquiryId").description(JsonFieldType.NUMBER).description("문의글 아이디"),
                        fieldWithPath("reply.content").type(JsonFieldType.STRING).description("답변 내용"),
                        fieldWithPath("reply.sellerName").type(JsonFieldType.STRING).description("판매자 명"),
                        fieldWithPath("reply.registerDate").type(JsonFieldType.STRING).description("등록일"),
                        fieldWithPath("reply.sellerCode").type(JsonFieldType.NUMBER).description("판매자코드"),
                        fieldWithPath("reply.replyId").type(JsonFieldType.NUMBER).description("답변아이디")
                    )
                )
            );
    }

    private Response getMockResponse() {
        InquiryForm.ReplyForm reply =
            InquiryForm.ReplyForm.builder()
                .content("문의 하신 사항에대한 답변입니다")
                .sellerId(3L)
                .sellerName("나이스샵")
                .sellerCode(51L)
                .registerDate(LocalDateTime.now())
                .replyId(6L)
                .build();
        Response res =
            Response.builder()
                .content("상품 문의 입니다")
                .inquiryId(5L)
                .clientId(1L)
                .clientName("아이디 또는 실명입니다")
                .itemId(6L)
                .itemDetailId(3L)
                .registerDate(LocalDateTime.now())
                .itemName("감자")
                .optionName("중량")
                .optionValue("1KG")
                .reply(reply)
                .build();
        return res;
    }

    @Test
    void registerReply() throws Exception {
        InquiryForm.ReplyForm reply =
            InquiryForm.ReplyForm.builder()
            .content("문의 하신 사항에대한 답변입니다")
            .sellerId(3L)
            .sellerName("나이스샵")
            .sellerCode(51L)
            .registerDate(LocalDateTime.now())
            .replyId(6L)
            .build();
        InquiryForm.Response res =
            InquiryForm.Response.builder()
                .content("상품 문의 입니다")
                .inquiryId(5L)
                .clientId(1L)
                .clientName("아이디 또는 실명입니다")
                .itemDetailId(3L)
                .registerDate(LocalDateTime.now())
                .itemName("감자")
                .optionName("중량")
                .optionValue("1KG")
                .reply(reply)
                .build();

        InquiryForm.Request req =
            InquiryForm.Request.builder()
            .content(reply.getContent())
            .build();

        given(this.service.registerReply(any(InquiryForm.Request.class), eq(1L), eq(5L))).willReturn(res);

        ResultActions actions =
            this.mvc.perform(
                RestDocumentationRequestBuilders.
                post("/api/inquiry/{inquiryId}/reply", res.getInquiryId())
                .content(mapper.writeValueAsString(req))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, utils.getSellerAuthHeader())
            );

        actions.andExpect(status().isCreated())
            .andDo(print())
            .andDo(
                document(
                    PATH,
                    getDocumentRequest(),
                    getDocumentResponse(),
                    getAuthHeaderField(),
                    pathParameters(
                        parameterWithName("inquiryId").description("문의글 아이디")
                    ),
                    relaxedRequestFields(
                        fieldWithPath("content").type(JsonFieldType.STRING).description("답변글")
                    ),
                    relaxedResponseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메세지"),
                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("결과 코드"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터")
                    ),


                    relaxedResponseFields(
                        beneathPath("data").withSubsectionId("data"),
                        fieldWithPath("content").description(JsonFieldType.STRING).description("문의사항"),
                        fieldWithPath("clientName").description(JsonFieldType.STRING).description("등록자 이름"),
                        fieldWithPath("itemName").description(JsonFieldType.STRING).description("문의한 상품 이름"),
                        fieldWithPath("optionName").description(JsonFieldType.STRING).description("문의한 옵션이름"),
                        fieldWithPath("optionValue").description(JsonFieldType.STRING).description("문의한 옵션값"),
                        fieldWithPath("registerDate").description(JsonFieldType.STRING).description("등록일"),
                        fieldWithPath("itemDetailId").description(JsonFieldType.NUMBER).description("옵션아이디"),
                        fieldWithPath("inquiryId").description(JsonFieldType.NUMBER).description("문의글 아이디"),
                        fieldWithPath("reply.content").type(JsonFieldType.STRING).description("답변 내용"),
                        fieldWithPath("reply.sellerName").type(JsonFieldType.STRING).description("판매자 명"),
                        fieldWithPath("reply.registerDate").type(JsonFieldType.STRING).description("등록일"),
                        fieldWithPath("reply.sellerCode").type(JsonFieldType.NUMBER).description("판매자코드"),
                        fieldWithPath("reply.replyId").type(JsonFieldType.NUMBER).description("답변아이디")
                    )
                )
            );
    }

    @Test
    void registerInquiry() throws Exception {

        InquiryForm.Response res =
            InquiryForm.Response.builder()
            .content("상품 문의 입니다")
            .inquiryId(5L)
            .clientId(1L)
            .clientName("아이디 또는 실명입니다")
            .itemDetailId(3L)
            .registerDate(LocalDateTime.now())
            .itemName("감자")
            .optionName("중량")
            .optionValue("1KG")
            .build();



        given(this.service.registerInquiry(any(InquiryForm.Request.class), eq(1L))).willReturn(res);


        InquiryForm.Request req =
            InquiryForm.Request.builder()
            .content("상품 문의 입니다")
            .itemDetailId(3L)
            .build();

        ResultActions actions =
            this.mvc.perform(
                post("/api/inquiry")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req))
                .header(HttpHeaders.AUTHORIZATION, utils.genAuthHeader())
            );

        actions.andExpect(status().isCreated())
            .andDo(print())
            .andDo(
                document(
                    PATH,
                    getDocumentRequest(),
                    getDocumentResponse(),
                    getAuthHeaderField(),
                    relaxedRequestFields(
                        fieldWithPath("content").type(JsonFieldType.STRING).description("문의사항"),
                        fieldWithPath("itemDetailId").type(JsonFieldType.NUMBER).description("문의할 옵션아이디")
                    ),
                    relaxedResponseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메세지"),
                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("결과 코드"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터")
                    ),


                    relaxedResponseFields(
                        beneathPath("data").withSubsectionId("data"),
                        fieldWithPath("content").description(JsonFieldType.STRING).description("문의사항"),
                        fieldWithPath("clientName").description(JsonFieldType.STRING).description("등록자 이름"),
                        fieldWithPath("itemName").description(JsonFieldType.STRING).description("문의한 상품 이름"),
                        fieldWithPath("optionName").description(JsonFieldType.STRING).description("문의한 옵션이름"),
                        fieldWithPath("optionValue").description(JsonFieldType.STRING).description("문의한 옵션값"),
                        fieldWithPath("registerDate").description(JsonFieldType.STRING).description("등록일"),
                        fieldWithPath("itemDetailId").description(JsonFieldType.NUMBER).description("옵션아이디")
                    )
                )
            );
    }
}