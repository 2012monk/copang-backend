package com.alconn.copang.address;

import static com.alconn.copang.ApiDocumentUtils.getAuthHeaderField;
import static com.alconn.copang.ApiDocumentUtils.getDocumentRequest;
import static com.alconn.copang.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedRequestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.alconn.copang.common.EntityPriority;
import com.alconn.copang.exceptions.InvalidTokenException;
import com.alconn.copang.exceptions.NoSuchEntityExceptions;
import com.alconn.copang.exceptions.ValidationException;
import com.alconn.copang.utils.TestUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
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
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
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
                        fieldWithPath("data.[].addressId").type(JsonFieldType.NUMBER)
                            .description("주소 식별 번호"),
                        fieldWithPath("data.[].clientId").type(JsonFieldType.NUMBER)
                            .description("고객 식별자"),
                        fieldWithPath("data.[].address").type(JsonFieldType.STRING)
                            .description("주소1"),
                        fieldWithPath("data.[].detail").type(JsonFieldType.STRING)
                            .description("상세주소"),
                        fieldWithPath("data.[].receiverName").type(JsonFieldType.STRING)
                            .description("받는사람 이름"),
                        fieldWithPath("data.[].receiverPhone").type(JsonFieldType.STRING)
                            .description("받는사람 전화번호"),
                        fieldWithPath("data.[].preRequest").type(JsonFieldType.STRING)
                            .description("요청사항"),
                        fieldWithPath("data.[].priority").type(JsonFieldType.STRING)
                            .description("기본주소 식별자 기본주소는 PRIMARY 나머지 SECONDARY")
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
                        fieldWithPath("receiverName").type(JsonFieldType.STRING)
                            .description("받는사람 이름"),
                        fieldWithPath("receiverPhone").type(JsonFieldType.STRING)
                            .description("받는사람 전화번호"),
                        fieldWithPath("preRequest").type(JsonFieldType.STRING).description("요청사항")
                            .optional()
                    ),
                    getSingleResponseSnippet()
                )
            );

    }

    private ResponseFieldsSnippet getSingleResponseSnippet() {
        return relaxedResponseFields(
            fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메세지"),
            fieldWithPath("code").type(JsonFieldType.NUMBER).description("결과코드"),
            fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
            fieldWithPath("data.addressId").type(JsonFieldType.NUMBER)
                .description("주소 식별 번호"),
            fieldWithPath("data.clientId").type(JsonFieldType.NUMBER)
                .description("고객 식별자"),
            fieldWithPath("data.address").type(JsonFieldType.STRING).description("주소1"),
            fieldWithPath("data.detail").type(JsonFieldType.STRING).description("상세주소"),
            fieldWithPath("data.receiverName").type(JsonFieldType.STRING)
                .description("받는사람 이름"),
            fieldWithPath("data.receiverPhone").type(JsonFieldType.STRING)
                .description("받는사람 전화번호"),
            fieldWithPath("data.preRequest").type(JsonFieldType.STRING)
                .description("요청사항"),
            fieldWithPath("data.priority").type(JsonFieldType.STRING)
                .description("기본주소 식별자 기본주소는 PRIMARY 나머지 SECONDARY")
        );
    }

    @Test
    void saveDefault() throws Exception {
        AddressForm res = getAddress();

        given(this.service.registerPrimaryAddress(any(AddressForm.class), eq(1L))).willReturn(res);

        this.mvc.perform(
            post("/api/address/default")
                .header(HttpHeaders.AUTHORIZATION, utils.genAuthHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(res))
        ).andExpect(status().isOk())
            .andDo(print())
            .andDo(
                document(
                    "address/{method-name}",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    getAuthHeaderField(),
                    relaxedRequestFields(
                        fieldWithPath("address").type(JsonFieldType.STRING).description("주소1"),
                        fieldWithPath("detail").type(JsonFieldType.STRING).description("상세주소"),
                        fieldWithPath("receiverName").type(JsonFieldType.STRING)
                            .description("받는사람 이름"),
                        fieldWithPath("receiverPhone").type(JsonFieldType.STRING)
                            .description("받는사람 전화번호"),
                        fieldWithPath("preRequest").type(JsonFieldType.STRING).description("요청사항")
                            .optional()
                    ),
                    getSingleResponseSnippet()

                )
            );
    }

    @Test
    void setDefault() throws Exception {
        Long addrId = 2L;
        Long clientId = 1L;

        given(this.service.setPrimaryAddress(eq(addrId), eq(clientId))).willReturn(true);

        this.mvc.perform(
            RestDocumentationRequestBuilders.
                patch("/api/address/{addressId}", addrId)
                .header(HttpHeaders.AUTHORIZATION, utils.genAuthHeader())
        )
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(
                document(
                    "address/{method-name}",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    getAuthHeaderField(),
                    pathParameters(
                        parameterWithName("addressId").description("주소 식별자")
                    ),
                    relaxedResponseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("결과 코드")
                    )
                )
            );
    }

    @Test
    void deleteAddress() throws Exception {
        AddressForm address = AddressForm.builder()
            .addressId(2L)
            .clientId(1L)
            .build();
        given(this.service.deleteAddress(eq(address.getAddressId()), eq(1L))).willReturn(address);

        this.mvc.perform(
            RestDocumentationRequestBuilders.
            delete("/api/address/{addressId}", address.getAddressId())
            .header(HttpHeaders.AUTHORIZATION, utils.genAuthHeader())
        )
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(
                document(
                    "address/{method-name}",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    getAuthHeaderField(),
                    pathParameters(
                        parameterWithName("addressId").description("주소 식별값")
                    ),
                    relaxedResponseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메세지"),
                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("결과코드"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과값"),
                        fieldWithPath("data.addressId").type(JsonFieldType.NUMBER).description("삭제된 주소 아이디"),
                        fieldWithPath("data.clientId").type(JsonFieldType.NUMBER).description("유저 식별값")
                   )
                )
            );
    }

    @Test
    void updateAddress()
        throws Exception {
        AddressForm origin = getAddress();

        ReflectionTestUtils.setField(origin, "address", "변경된 주소입니다");
        given(this.service.updateAddress(any(AddressForm.class), eq(origin.getAddressId()), eq(1L)))
            .willReturn(origin);

        AddressForm req = AddressForm.builder().addressName(origin.getAddress()).build();
        this.mvc.perform(
            put("/api/address/{addressId}", origin.getAddressId())
                .content(mapper.writeValueAsString(req))
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, utils.genAuthHeader())
        )
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(
                document(
                    "address/{method-name}",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    getAuthHeaderField(),
                    relaxedRequestFields(
                        fieldWithPath("address").type(JsonFieldType.STRING).description("주소").optional(),
                        fieldWithPath("detail").type(JsonFieldType.STRING).description("상세주소").optional(),
                        fieldWithPath("receiverName").type(JsonFieldType.STRING).description("받는사람 이름").optional(),
                        fieldWithPath("receiverPhone").type(JsonFieldType.STRING).description("받는사람 연락처").optional(),
                        fieldWithPath("preRequest").type(JsonFieldType.STRING).description("요청사항").optional()
                    ),
                    getSingleResponseSnippet()
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

    private AddressForm getAddress(Long id, EntityPriority priority) {

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
        for (long i = 1L; i < 4L; i++) {
            if (i == 1L) {
                list.add(getAddress(i, EntityPriority.PRIMARY));
            } else {
                list.add(getAddress(i, EntityPriority.SECONDARY));
            }
        }
        return list;
    }


}