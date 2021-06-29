package com.alconn.copang.address;

import static com.alconn.copang.ApiDocumentUtils.getAuthHeaderField;
import static com.alconn.copang.ApiDocumentUtils.getDocumentRequest;
import static com.alconn.copang.ApiDocumentUtils.getDocumentResponse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedRequestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.alconn.copang.common.EntityPriority;
import com.alconn.copang.exceptions.InvalidTokenException;
import com.alconn.copang.exceptions.ValidationException;
import com.alconn.copang.utils.TestUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.impl.crypto.MacProvider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.text.html.parser.Entity;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class AddressControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    AddressService service;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    TestUtils utils;

    @Test
    void getAllAddress() throws Exception {
        List<AddressForm> res = listAddress();

        given(service.getAllAddresses(eq(1L))).willReturn(res);

        this.mvc.perform(
            get("/api/address")
            .header(HttpHeaders.AUTHORIZATION, utils.genAuthHeader())
        ).andExpect(status().isOk())
            .andDo(
                document(
                    "address/{method-name}",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    getAuthHeaderField(),
                    relaxedResponseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메세지"),
                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("결과코드"),
                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("결과 데이터"),
                        fieldWithPath("data.[].addressId").type(JsonFieldType.NUMBER).description("주소 식별 번호"),
                        fieldWithPath("data.[].clientId").type(JsonFieldType.NUMBER).description("고객 식별자"),
                        fieldWithPath("data.[].address").type(JsonFieldType.STRING).description("주소1"),
                        fieldWithPath("data.[].detail").type(JsonFieldType.STRING).description("상세주소"),
                        fieldWithPath("data.[].receiverName").type(JsonFieldType.STRING).description("받는사람 이름"),
                        fieldWithPath("data.[].receiverPhone").type(JsonFieldType.STRING).description("받는사람 전화번호"),
                        fieldWithPath("data.[].preRequest").type(JsonFieldType.STRING).description("요청사항"),
                        fieldWithPath("data.[].priority").type(JsonFieldType.STRING).description("기본주소 식별자 기본주소는 PRIMARY 나머지 SECONDARY")
                    )
                )
            );
    }

    @Test
    void saveAddress() throws Exception {
        AddressForm res =
            getAddress();

        given(service.registerAddress(any(AddressForm.class), eq(1L))).willReturn(res);

        String token = utils.genToken();
        this.mvc.perform(
            post("/api/address")
            .content(mapper.writeValueAsString(res))
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        ).andExpect(status().isOk())
            .andDo(print())
            .andDo(
                document(
                    "address/create",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    getAuthHeaderField(),
                    relaxedRequestFields(
                        fieldWithPath("address").type(JsonFieldType.STRING).description("주소1"),
                        fieldWithPath("detail").type(JsonFieldType.STRING).description("상세주소"),
                        fieldWithPath("receiverName").type(JsonFieldType.STRING).description("받는사람 이름"),
                        fieldWithPath("receiverPhone").type(JsonFieldType.STRING).description("받는사람 전화번호"),
                        fieldWithPath("preRequest").type(JsonFieldType.STRING).description("요청사항").optional()
                    ),
                    relaxedResponseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메세지"),
                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("결과코드"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
                        fieldWithPath("data.addressId").type(JsonFieldType.NUMBER).description("주소 식별 번호"),
                        fieldWithPath("data.clientId").type(JsonFieldType.NUMBER).description("고객 식별자"),
                        fieldWithPath("data.address").type(JsonFieldType.STRING).description("주소1"),
                        fieldWithPath("data.detail").type(JsonFieldType.STRING).description("상세주소"),
                        fieldWithPath("data.receiverName").type(JsonFieldType.STRING).description("받는사람 이름"),
                        fieldWithPath("data.receiverPhone").type(JsonFieldType.STRING).description("받는사람 전화번호"),
                        fieldWithPath("data.preRequest").type(JsonFieldType.STRING).description("요청사항"),
                        fieldWithPath("data.priority").type(JsonFieldType.STRING).description("기본주소 식별자 기본주소는 PRIMARY 나머지 SECONDARY")
                    )
                )
            );

    }

    private AddressForm getAddress() {
        return AddressForm.builder()
            .addressId(1L)
            .address("의정부")
            .detail("306보충대")
            .priority(EntityPriority.PRIMARY)
            .clientId(43L)
            .receiverName("길동홍")
            .receiverPhone("080-800-9898")
            .preRequest("문앞")
            .build();
    }

    private AddressForm getAddress(Long id, EntityPriority priority){

        return AddressForm.builder().addressId(id)
                .address("의정부")
                .detail("306보충대")
                .priority(priority)
                .clientId(43L)
                .receiverName("길동홍")
                .receiverPhone("080-800-9898")
                .preRequest("문앞")
                .build();
    }

    private List<AddressForm> listAddress() {
        Long id = 1L;
        List<AddressForm> list = new ArrayList<>(4);
        for (long i=1L;i<4L;i++){
            if (i == 1L) {
                list.add(getAddress(i, EntityPriority.PRIMARY));
            }else{
                list.add(getAddress(i, EntityPriority.SECONDARY));
            }
        }
        return list;
    }
}