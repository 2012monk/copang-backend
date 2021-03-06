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

        ReflectionTestUtils.setField(res.getReply(), "content", "????????? ??????");

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
                        parameterWithName("inquiryId").description("????????? ?????????")
                    ),
                    relaxedRequestFields(
                        fieldWithPath("content").type(JsonFieldType.STRING).description("???????????? ????????????")
                    ),
                    relaxedResponseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("?????? ?????????"),
                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("?????? ??????"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("?????? ?????????")
                    ),
                    relaxedResponseFields(
                        beneathPath("data").withSubsectionId("data"),
                        fieldWithPath("content").description(JsonFieldType.STRING).description("????????????"),
                        fieldWithPath("clientName").description(JsonFieldType.STRING).description("????????? ??????"),
                        fieldWithPath("itemName").description(JsonFieldType.STRING).description("????????? ?????? ??????"),
                        fieldWithPath("optionName").description(JsonFieldType.STRING).description("????????? ????????????"),
                        fieldWithPath("optionValue").description(JsonFieldType.STRING).description("????????? ?????????"),
                        fieldWithPath("registerDate").description(JsonFieldType.STRING).description("?????????"),
                        fieldWithPath("itemDetailId").description(JsonFieldType.NUMBER).description("???????????????"),
                        fieldWithPath("inquiryId").description(JsonFieldType.NUMBER).description("????????? ?????????"),
                        fieldWithPath("reply.content").type(JsonFieldType.STRING).description("?????? ??????"),
                        fieldWithPath("reply.sellerName").type(JsonFieldType.STRING).description("????????? ???"),
                        fieldWithPath("reply.registerDate").type(JsonFieldType.STRING).description("?????????"),
                        fieldWithPath("reply.sellerCode").type(JsonFieldType.NUMBER).description("???????????????"),
                        fieldWithPath("reply.replyId").type(JsonFieldType.NUMBER).description("???????????????")
                    )
                )
            );
    }

    @Test
    void updateInquiry() throws Exception {
        Response res = getMockResponse();

        ReflectionTestUtils.setField(res, "content", "????????? ??????");

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
                        parameterWithName("inquiryId").description("????????? ?????????")
                    ),
                    relaxedRequestFields(
                        fieldWithPath("content").type(JsonFieldType.STRING).description("???????????? ??????")
                    ),
                    relaxedResponseFields(),
                    relaxedResponseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("?????? ?????????"),
                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("?????? ??????"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("?????? ?????????")
                    ),
                    relaxedResponseFields(
                        beneathPath("data").withSubsectionId("data"),
                        fieldWithPath("content").description(JsonFieldType.STRING).description("????????????"),
                        fieldWithPath("clientName").description(JsonFieldType.STRING).description("????????? ??????"),
                        fieldWithPath("itemName").description(JsonFieldType.STRING).description("????????? ?????? ??????"),
                        fieldWithPath("optionName").description(JsonFieldType.STRING).description("????????? ????????????"),
                        fieldWithPath("optionValue").description(JsonFieldType.STRING).description("????????? ?????????"),
                        fieldWithPath("registerDate").description(JsonFieldType.STRING).description("?????????"),
                        fieldWithPath("itemDetailId").description(JsonFieldType.NUMBER).description("???????????????"),
                        fieldWithPath("inquiryId").description(JsonFieldType.NUMBER).description("????????? ?????????"),
                        fieldWithPath("reply.content").type(JsonFieldType.STRING).description("?????? ??????"),
                        fieldWithPath("reply.sellerName").type(JsonFieldType.STRING).description("????????? ???"),
                        fieldWithPath("reply.registerDate").type(JsonFieldType.STRING).description("?????????"),
                        fieldWithPath("reply.sellerCode").type(JsonFieldType.NUMBER).description("???????????????"),
                        fieldWithPath("reply.replyId").type(JsonFieldType.NUMBER).description("???????????????")
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
                        parameterWithName("itemId").description("???????????? ?????? ?????????")
                    ),
                    relaxedResponseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("?????? ?????????"),
                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("?????? ??????"),
                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("?????? ?????????")
                    ),
                    relaxedResponseFields(
                        beneathPath("data[]").withSubsectionId("data"),
                        fieldWithPath("content").description(JsonFieldType.STRING).description("????????????"),
                        fieldWithPath("clientName").description(JsonFieldType.STRING).description("????????? ??????"),
                        fieldWithPath("itemName").description(JsonFieldType.STRING).description("????????? ?????? ??????"),
                        fieldWithPath("optionName").description(JsonFieldType.STRING).description("????????? ????????????"),
                        fieldWithPath("optionValue").description(JsonFieldType.STRING).description("????????? ?????????"),
                        fieldWithPath("registerDate").description(JsonFieldType.STRING).description("?????????"),
                        fieldWithPath("itemDetailId").description(JsonFieldType.NUMBER).description("???????????????"),
                        fieldWithPath("inquiryId").description(JsonFieldType.NUMBER).description("????????? ?????????"),
                        fieldWithPath("reply.content").type(JsonFieldType.STRING).description("?????? ??????"),
                        fieldWithPath("reply.sellerName").type(JsonFieldType.STRING).description("????????? ???"),
                        fieldWithPath("reply.registerDate").type(JsonFieldType.STRING).description("?????????"),
                        fieldWithPath("reply.sellerCode").type(JsonFieldType.NUMBER).description("???????????????"),
                        fieldWithPath("reply.replyId").type(JsonFieldType.NUMBER).description("???????????????")
                    )
                )
            );
    }

    private Response getMockResponse() {
        InquiryForm.ReplyForm reply =
            InquiryForm.ReplyForm.builder()
                .content("?????? ?????? ??????????????? ???????????????")
                .sellerId(3L)
                .sellerName("????????????")
                .sellerCode(51L)
                .registerDate(LocalDateTime.now())
                .replyId(6L)
                .build();
        Response res =
            Response.builder()
                .content("?????? ?????? ?????????")
                .inquiryId(5L)
                .clientId(1L)
                .clientName("????????? ?????? ???????????????")
                .itemId(6L)
                .itemDetailId(3L)
                .registerDate(LocalDateTime.now())
                .itemName("??????")
                .optionName("??????")
                .optionValue("1KG")
                .reply(reply)
                .build();
        return res;
    }

    @Test
    void registerReply() throws Exception {
        InquiryForm.ReplyForm reply =
            InquiryForm.ReplyForm.builder()
            .content("?????? ?????? ??????????????? ???????????????")
            .sellerId(3L)
            .sellerName("????????????")
            .sellerCode(51L)
            .registerDate(LocalDateTime.now())
            .replyId(6L)
            .build();
        InquiryForm.Response res =
            InquiryForm.Response.builder()
                .content("?????? ?????? ?????????")
                .inquiryId(5L)
                .clientId(1L)
                .clientName("????????? ?????? ???????????????")
                .itemDetailId(3L)
                .registerDate(LocalDateTime.now())
                .itemName("??????")
                .optionName("??????")
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
                        parameterWithName("inquiryId").description("????????? ?????????")
                    ),
                    relaxedRequestFields(
                        fieldWithPath("content").type(JsonFieldType.STRING).description("?????????")
                    ),
                    relaxedResponseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("?????? ?????????"),
                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("?????? ??????"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("?????? ?????????")
                    ),


                    relaxedResponseFields(
                        beneathPath("data").withSubsectionId("data"),
                        fieldWithPath("content").description(JsonFieldType.STRING).description("????????????"),
                        fieldWithPath("clientName").description(JsonFieldType.STRING).description("????????? ??????"),
                        fieldWithPath("itemName").description(JsonFieldType.STRING).description("????????? ?????? ??????"),
                        fieldWithPath("optionName").description(JsonFieldType.STRING).description("????????? ????????????"),
                        fieldWithPath("optionValue").description(JsonFieldType.STRING).description("????????? ?????????"),
                        fieldWithPath("registerDate").description(JsonFieldType.STRING).description("?????????"),
                        fieldWithPath("itemDetailId").description(JsonFieldType.NUMBER).description("???????????????"),
                        fieldWithPath("inquiryId").description(JsonFieldType.NUMBER).description("????????? ?????????"),
                        fieldWithPath("reply.content").type(JsonFieldType.STRING).description("?????? ??????"),
                        fieldWithPath("reply.sellerName").type(JsonFieldType.STRING).description("????????? ???"),
                        fieldWithPath("reply.registerDate").type(JsonFieldType.STRING).description("?????????"),
                        fieldWithPath("reply.sellerCode").type(JsonFieldType.NUMBER).description("???????????????"),
                        fieldWithPath("reply.replyId").type(JsonFieldType.NUMBER).description("???????????????")
                    )
                )
            );
    }

    @Test
    void registerInquiry() throws Exception {

        InquiryForm.Response res =
            InquiryForm.Response.builder()
            .content("?????? ?????? ?????????")
            .inquiryId(5L)
            .clientId(1L)
            .clientName("????????? ?????? ???????????????")
            .itemDetailId(3L)
            .registerDate(LocalDateTime.now())
            .itemName("??????")
            .optionName("??????")
            .optionValue("1KG")
            .build();



        given(this.service.registerInquiry(any(InquiryForm.Request.class), eq(1L))).willReturn(res);


        InquiryForm.Request req =
            InquiryForm.Request.builder()
            .content("?????? ?????? ?????????")
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
                        fieldWithPath("content").type(JsonFieldType.STRING).description("????????????"),
                        fieldWithPath("itemDetailId").type(JsonFieldType.NUMBER).description("????????? ???????????????")
                    ),
                    relaxedResponseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("?????? ?????????"),
                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("?????? ??????"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("?????? ?????????")
                    ),


                    relaxedResponseFields(
                        beneathPath("data").withSubsectionId("data"),
                        fieldWithPath("content").description(JsonFieldType.STRING).description("????????????"),
                        fieldWithPath("clientName").description(JsonFieldType.STRING).description("????????? ??????"),
                        fieldWithPath("itemName").description(JsonFieldType.STRING).description("????????? ?????? ??????"),
                        fieldWithPath("optionName").description(JsonFieldType.STRING).description("????????? ????????????"),
                        fieldWithPath("optionValue").description(JsonFieldType.STRING).description("????????? ?????????"),
                        fieldWithPath("registerDate").description(JsonFieldType.STRING).description("?????????"),
                        fieldWithPath("itemDetailId").description(JsonFieldType.NUMBER).description("???????????????")
                    )
                )
            );
    }
}