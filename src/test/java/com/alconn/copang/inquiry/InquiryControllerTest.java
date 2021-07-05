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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.alconn.copang.client.Client;
import com.alconn.copang.client.ClientRepo;
import com.alconn.copang.exceptions.InvalidTokenException;
import com.alconn.copang.utils.TestUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
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

    @Test
    void registerInquiry() throws Exception {

        InquiryForm.Response res =
            InquiryForm.Response.builder()
            .content("상품 문의 입니다")
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
                        fieldWithPath("data").type(JsonFieldType.STRING).description("결과 데이터")
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