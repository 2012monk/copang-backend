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
                        fieldWithPath("message").type(JsonFieldType.STRING).description("?????? ?????????"),
                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("????????????"),
                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("?????? ?????????"),
                        fieldWithPath("data.[].addressId").type(JsonFieldType.NUMBER)
                            .description("?????? ?????? ??????"),
                        fieldWithPath("data.[].clientId").type(JsonFieldType.NUMBER)
                            .description("?????? ?????????"),
                        fieldWithPath("data.[].address").type(JsonFieldType.STRING)
                            .description("??????1"),
                        fieldWithPath("data.[].detail").type(JsonFieldType.STRING)
                            .description("????????????"),
                        fieldWithPath("data.[].receiverName").type(JsonFieldType.STRING)
                            .description("???????????? ??????"),
                        fieldWithPath("data.[].receiverPhone").type(JsonFieldType.STRING)
                            .description("???????????? ????????????"),
                        fieldWithPath("data.[].preRequest").type(JsonFieldType.STRING)
                            .description("????????????"),
                        fieldWithPath("data.[].priority").type(JsonFieldType.STRING)
                            .description("???????????? ????????? ??????????????? PRIMARY ????????? SECONDARY")
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
                        fieldWithPath("address").type(JsonFieldType.STRING).description("??????1"),
                        fieldWithPath("detail").type(JsonFieldType.STRING).description("????????????"),
                        fieldWithPath("receiverName").type(JsonFieldType.STRING)
                            .description("???????????? ??????"),
                        fieldWithPath("receiverPhone").type(JsonFieldType.STRING)
                            .description("???????????? ????????????"),
                        fieldWithPath("preRequest").type(JsonFieldType.STRING).description("????????????")
                            .optional()
                    ),
                    getSingleResponseSnippet()
                )
            );

    }

    private ResponseFieldsSnippet getSingleResponseSnippet() {
        return relaxedResponseFields(
            fieldWithPath("message").type(JsonFieldType.STRING).description("?????? ?????????"),
            fieldWithPath("code").type(JsonFieldType.NUMBER).description("????????????"),
            fieldWithPath("data").type(JsonFieldType.OBJECT).description("?????? ?????????"),
            fieldWithPath("data.addressId").type(JsonFieldType.NUMBER)
                .description("?????? ?????? ??????"),
            fieldWithPath("data.clientId").type(JsonFieldType.NUMBER)
                .description("?????? ?????????"),
            fieldWithPath("data.address").type(JsonFieldType.STRING).description("??????1"),
            fieldWithPath("data.detail").type(JsonFieldType.STRING).description("????????????"),
            fieldWithPath("data.receiverName").type(JsonFieldType.STRING)
                .description("???????????? ??????"),
            fieldWithPath("data.receiverPhone").type(JsonFieldType.STRING)
                .description("???????????? ????????????"),
            fieldWithPath("data.preRequest").type(JsonFieldType.STRING)
                .description("????????????"),
            fieldWithPath("data.priority").type(JsonFieldType.STRING)
                .description("???????????? ????????? ??????????????? PRIMARY ????????? SECONDARY")
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
                        fieldWithPath("address").type(JsonFieldType.STRING).description("??????1"),
                        fieldWithPath("detail").type(JsonFieldType.STRING).description("????????????"),
                        fieldWithPath("receiverName").type(JsonFieldType.STRING)
                            .description("???????????? ??????"),
                        fieldWithPath("receiverPhone").type(JsonFieldType.STRING)
                            .description("???????????? ????????????"),
                        fieldWithPath("preRequest").type(JsonFieldType.STRING).description("????????????")
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
                        parameterWithName("addressId").description("?????? ?????????")
                    ),
                    relaxedResponseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("???????????????"),
                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("?????? ??????")
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
                        parameterWithName("addressId").description("?????? ?????????")
                    ),
                    relaxedResponseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("?????? ?????????"),
                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("????????????"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("?????????"),
                        fieldWithPath("data.addressId").type(JsonFieldType.NUMBER).description("????????? ?????? ?????????"),
                        fieldWithPath("data.clientId").type(JsonFieldType.NUMBER).description("?????? ?????????")
                   )
                )
            );
    }

    @Test
    void updateAddress()
        throws Exception {
        AddressForm origin = getAddress();

        ReflectionTestUtils.setField(origin, "address", "????????? ???????????????");
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
                        fieldWithPath("address").type(JsonFieldType.STRING).description("??????").optional(),
                        fieldWithPath("detail").type(JsonFieldType.STRING).description("????????????").optional(),
                        fieldWithPath("receiverName").type(JsonFieldType.STRING).description("???????????? ??????").optional(),
                        fieldWithPath("receiverPhone").type(JsonFieldType.STRING).description("???????????? ?????????").optional(),
                        fieldWithPath("preRequest").type(JsonFieldType.STRING).description("????????????").optional()
                    ),
                    getSingleResponseSnippet()
                )
            );
    }


    private AddressForm getAddress() {
        return AddressForm.builder()
            .addressId(1L)
            .address("?????????")
            .detail("306?????????")
            .priority(EntityPriority.PRIMARY)
            .clientId(43L)
            .receiverName("?????????")
            .receiverPhone("080-800-9898")
            .preRequest("??????")
            .build();
    }

    private AddressForm getAddress(Long id, EntityPriority priority) {

        return AddressForm.builder().addressId(id)
            .address("?????????")
            .detail("306?????????")
            .priority(priority)
            .clientId(43L)
            .receiverName("?????????")
            .receiverPhone("080-800-9898")
            .preRequest("??????")
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