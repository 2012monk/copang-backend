package com.alconn.copang.item;


import com.alconn.copang.ApiDocumentUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.annotation.Commit;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

//@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
@AutoConfigureRestDocs
public class ItemControllerTest {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemDetailRepository itemDetailRepository;

    @Autowired
    ItemMapper itemMapper;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ItemService itemService;

    @Autowired
    ItemDetailService itemDetailService;

    @Autowired
    EntityManager em;

    @BeforeEach
    void setUp() {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    //=================테스트 데이터==========


    private ItemForm itemFormTest() {
        List<ItemDetailForm.DetailForm> list = new ArrayList<>();

        ItemDetailForm.DetailForm detailForm = ItemDetailForm.DetailForm.builder()
                .mainImg("대표사진")
                .optionName("옵션이름")
                .optionValue("옵션값")
                .price(10000)
                .stockQuantity(100)
                .subImg("옵션사진")
                .build();
        ItemDetailForm.DetailForm detailForm2 = ItemDetailForm.DetailForm.builder()
                .mainImg("대표사진2")
                .optionName("옵션이름2")
                .optionValue("옵션값2")
                .price(20000)
                .stockQuantity(200)
                .subImg("옵션사진")
                .build();
        list.add(detailForm2);
        list.add(detailForm);

        ItemForm itemForm = ItemForm.builder()
                .itemName("상품명")
                .itemComment("상품설명")
                .itemDetailFormList(list)
                .build();

        return itemForm;
    }

    private void testData(){
        Item item=Item.builder()
                .itemComment("반팔티설명")
                .itemName("반팔티")
                .build();
        itemRepository.save(item);

        ItemDetail itemDetail= ItemDetail.builder()
                .price(100)
                .stockQuantity(20)
                .optionName("색상")
                .optionValue("검은색")
                .mainImg("양말사진")
                .build();
        itemDetail.itemConnect(item);

        itemDetailRepository.save(itemDetail);
        em.flush();
        em.clear();
    }



    //=======================================
    @DisplayName("상품 옵션 추가")
    @Test
    public void optionOneAdd() throws Exception{
        testData();
        Item item=itemRepository.findAll().get(0);
        ItemForm.ItemSingle itemSingle=ItemForm.ItemSingle.builder()
                .itemId(item.getItemId())
                .itemName("양말")
                .itemComment("양말설명")
                .detailForm(ItemDetailForm.DetailForm.builder()
                        .price(100)
                        .stockQuantity(20)
                        .optionName("색상")
                        .optionValue("검은색")
                        .mainImg("양말사진")
                        .subImg("양말추가사진")
                        .build())
                .build();

        mockMvc.perform(post("/api/item/add/detail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemSingle))
                        .characterEncoding("utf-8")
//                        .header(HttpHeaders.AUTHORIZATION)
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andDo(print())
                .andDo(document("item/post-save-detail",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        relaxedRequestFields(
                                fieldWithPath("itemName").type(JsonFieldType.STRING).description("상품명"),
                                fieldWithPath("itemComment").type(JsonFieldType.STRING).description("상품설명"),
                                fieldWithPath("detailForm.price").type(JsonFieldType.NUMBER).description("단가"),
                                fieldWithPath("detailForm.stockQuantity").type(JsonFieldType.NUMBER).description("재고"),
                                fieldWithPath("detailForm.optionName").type(JsonFieldType.STRING).description("옵션명"),
                                fieldWithPath("detailForm.optionValue").type(JsonFieldType.STRING).description("옵션값"),
                                fieldWithPath("detailForm.mainImg").type(JsonFieldType.STRING).description("대표사진"),
                                fieldWithPath("detailForm.subImg").type(JsonFieldType.STRING).description("옵션사진").optional()
                        ),
                        relaxedResponseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("data.itemName").type(JsonFieldType.STRING).description("상품명"),
                                fieldWithPath("data.itemId").type(JsonFieldType.NUMBER).description("상품등록코드"),
                                fieldWithPath("data.itemComment").type(JsonFieldType.STRING).description("상품설명"),

                                fieldWithPath("data.itemDetailViewForm").type(JsonFieldType.OBJECT).description("추가할상품옵션정보"),
                                fieldWithPath("data.itemDetailViewForm.itemDetailId").type(JsonFieldType.NUMBER).description("상품옵션등록코드"),
                                fieldWithPath("data.itemDetailViewForm.price").type(JsonFieldType.NUMBER).description("단가"),
                                fieldWithPath("data.itemDetailViewForm.stockQuantity").type(JsonFieldType.NUMBER).description("재고"),
                                fieldWithPath("data.itemDetailViewForm.optionName").type(JsonFieldType.STRING).description("옵션명"),
                                fieldWithPath("data.itemDetailViewForm.optionValue").type(JsonFieldType.STRING).description("옵션값"),
                                fieldWithPath("data.itemDetailViewForm.mainImg").type(JsonFieldType.STRING).description("대표사진"),
                                fieldWithPath("data.itemDetailViewForm.subImg").type(JsonFieldType.STRING).description("옵션사진").optional()
                        )));
    }

    //RestDocumentationRequestBuilders -> Rest Doc 작성할 때 사용한다함
    @DisplayName("상품리스트등록")
    @Test
    public void save() throws Exception {
        ItemForm itemForm = itemFormTest();

        mockMvc.perform(post("/api/item/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemForm))
                        .characterEncoding("utf-8")
//                        .header(HttpHeaders.AUTHORIZATION)
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andDo(print())
                .andDo(document("item/post-save",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        relaxedRequestFields(
                              fieldWithPath("itemName").type(JsonFieldType.STRING).description("상품명"),
                                fieldWithPath("itemComment").type(JsonFieldType.STRING).description("상품설명"),

                                fieldWithPath("itemDetailFormList.[].price").type(JsonFieldType.NUMBER).description("단가"),
                                fieldWithPath("itemDetailFormList.[].stockQuantity").type(JsonFieldType.NUMBER).description("재고"),
                                fieldWithPath("itemDetailFormList.[].optionName").type(JsonFieldType.STRING).description("옵션명"),
                                fieldWithPath("itemDetailFormList.[].optionValue").type(JsonFieldType.STRING).description("옵션값"),
                                fieldWithPath("itemDetailFormList.[].mainImg").type(JsonFieldType.STRING).description("대표사진"),
                                fieldWithPath("itemDetailFormList.[].subImg").type(JsonFieldType.STRING).description("옵션사진").optional()
                        ),
                        relaxedResponseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("data.itemName").type(JsonFieldType.STRING).description("상품명"),
                                fieldWithPath("data.itemComment").type(JsonFieldType.STRING).description("상품설명"),

                                fieldWithPath("data.itemId").type(JsonFieldType.NUMBER).description("상품등록코드"),

                                fieldWithPath("data.itemDetailFormList").type(JsonFieldType.ARRAY).description("상품옵션리스트"),

                                fieldWithPath("data.itemDetailFormList.[].price").type(JsonFieldType.NUMBER).description("단가"),
                                fieldWithPath("data.itemDetailFormList.[].stockQuantity").type(JsonFieldType.NUMBER).description("재고"),
                                fieldWithPath("data.itemDetailFormList.[].optionName").type(JsonFieldType.STRING).description("옵션명"),
                                fieldWithPath("data.itemDetailFormList.[].optionValue").type(JsonFieldType.STRING).description("옵션값"),
                                fieldWithPath("data.itemDetailFormList.[].mainImg").type(JsonFieldType.STRING).description("대표사진"),
                                fieldWithPath("data.itemDetailFormList.[].itemDetailId").type(JsonFieldType.NUMBER).description("상품옵션등록코드")
                                )));
    }

    @DisplayName("대표상품페이지")
    @Test
    public void list() throws Exception {
        save();
        save();
        save();
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/item/list")
                .characterEncoding("utf-8")
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andDo(print())
                .andDo(document("item/get-mainlist",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        relaxedResponseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("data.[].itemName").type(JsonFieldType.STRING).description("상품명"),
                                fieldWithPath("data.[].itemId").type(JsonFieldType.NUMBER).description("상품등록코드"),
                                fieldWithPath("data.[].itemDetailId").type(JsonFieldType.NUMBER).description("상품옵션등록코드"),
                                fieldWithPath("data.[].price").type(JsonFieldType.NUMBER).description("단가"),
                                fieldWithPath("data.[].mainImg").type(JsonFieldType.STRING).description("대표사진")

                        )));
    }

    @DisplayName("상품상세페이지")
    @Test
    public void listPage() throws Exception {
        save();
        List<Item> item = itemService.itemFindAll();
        Long itemId = item.get(0).getItemId();

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/item/list/itemid={itemId}", itemId)
                .characterEncoding("utf-8")
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andDo(document("item/get-itemlist",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        pathParameters(
                                parameterWithName("itemId").description("상품등록코드")
                        ),

                        relaxedResponseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("data.itemId").type(JsonFieldType.NUMBER).description("상품등록코드"),
                                fieldWithPath("data.itemName").type(JsonFieldType.STRING).description("상품명"),
                                fieldWithPath("data.itemComment").type(JsonFieldType.STRING).description("상품설명"),

                                fieldWithPath("data.itemDetailFormList").type(JsonFieldType.ARRAY).description("상품옵션정보"),
                                fieldWithPath("data.itemDetailFormList.[].itemDetailId").type(JsonFieldType.NUMBER).description("상품옵션등록코드"),
                                fieldWithPath("data.itemDetailFormList.[].price").type(JsonFieldType.NUMBER).description("단가"),
                                fieldWithPath("data.itemDetailFormList.[].stockQuantity").type(JsonFieldType.NUMBER).description("재고"),
                                fieldWithPath("data.itemDetailFormList.[].optionName").type(JsonFieldType.STRING).description("옵션명"),
                                fieldWithPath("data.itemDetailFormList.[].optionValue").type(JsonFieldType.STRING).description("옵션값"),
                                fieldWithPath("data.itemDetailFormList.[].mainImg").type(JsonFieldType.STRING).description("대표사진"),
                                fieldWithPath("data.itemDetailFormList.[].subImg").type(JsonFieldType.STRING).description("옵션사진").optional()
                        )))
                .andDo(print());
    }


    @DisplayName("상품삭제")
    @Test
    public void delItem() throws Exception {
        save();
        List<Item> item = itemService.itemFindAll();
        Long itemId = item.get(0).getItemId();

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/item/delete/{itemId}" , itemId)
                .characterEncoding("utf-8")
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andDo(document("item/delete-item",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        pathParameters(
                                parameterWithName("itemId").description("상품등록코드")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("삭제된 상품정보")
                        )))
                .andDo(print());
    }

    @DisplayName("상품옵션삭제")
    @Test
    public void delItemDetail() throws Exception {
        save();
        List<Item> item = itemService.itemFindAll();
        Long itemDetailId = item.get(0).getItemDetails().get(0).getItemDetailId();

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/item/delete/item-detail/{itemDetailId}" , itemDetailId)
                .characterEncoding("utf-8")
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andDo(document("item/delete-itemDetail",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        pathParameters(
                                parameterWithName("itemDetailId").description("옵션아이디")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("삭제된 상품옵션정보")
                        )))
                .andDo(print());
    }

    @DisplayName("상품단일수정")
    @Test
    public void upItemDetailOne() throws Exception{
        testData();
        ItemDetail itemDetail=itemDetailRepository.findAll().get(0);
        ItemForm.ItemFormUpdateSingle updateSingle=ItemForm.ItemFormUpdateSingle.builder()
                .itemId(itemDetail.getItem().getItemId())
                .itemName("신발")
                .itemComment("신발설명")
                .detailUpdateClass(ItemDetailForm.DetailUpdateClass.builder()
                        .itemDetailId(itemDetail.getItemDetailId())
                        .price(10000)
                        .stockQuantity(10)
                        .optionName("색상")
                        .optionValue("초록")
                        .mainImg("신발초록색사진")
                        .subImg("추가옵션사진")
                        .build()
                )
                .build();
        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/item/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateSingle))
                .characterEncoding("utf-8")
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andDo(document("item/put-update",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        relaxedRequestFields(
                                fieldWithPath("itemId").type(JsonFieldType.NUMBER).description("상품등록코드"),
                                fieldWithPath("itemName").type(JsonFieldType.STRING).description("상품명"),
                                fieldWithPath("itemComment").type(JsonFieldType.STRING).description("상품설명"),

                                fieldWithPath("detailUpdateClass.itemDetailId").type(JsonFieldType.NUMBER).description("상품옵션등록코드"),
                                fieldWithPath("detailUpdateClass.price").type(JsonFieldType.NUMBER).description("단가"),
                                fieldWithPath("detailUpdateClass.stockQuantity").type(JsonFieldType.NUMBER).description("재고"),
                                fieldWithPath("detailUpdateClass.optionName").type(JsonFieldType.STRING).description("옵션명"),
                                fieldWithPath("detailUpdateClass.optionValue").type(JsonFieldType.STRING).description("옵션값"),
                                fieldWithPath("detailUpdateClass.mainImg").type(JsonFieldType.STRING).description("대표사진"),
                                fieldWithPath("detailUpdate Class.subImg").type(JsonFieldType.STRING).description("옵션사진").optional()
                        ),
                        relaxedResponseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("data.itemId").type(JsonFieldType.NUMBER).description("상품등록코드"),
                                fieldWithPath("data.itemName").type(JsonFieldType.STRING).description("상품명"),
                                fieldWithPath("data.itemComment").type(JsonFieldType.STRING).description("상품설명"),

                                fieldWithPath("data.itemDetailViewForm").type(JsonFieldType.OBJECT).description("상품옵션리스트"),
                                fieldWithPath("data.itemDetailViewForm.itemDetailId").type(JsonFieldType.NUMBER).description("상품옵션등록코드"),
                                fieldWithPath("data.itemDetailViewForm.price").type(JsonFieldType.NUMBER).description("단가"),
                                fieldWithPath("data.itemDetailViewForm.stockQuantity").type(JsonFieldType.NUMBER).description("재고"),
                                fieldWithPath("data.itemDetailViewForm.optionName").type(JsonFieldType.STRING).description("옵션명"),
                                fieldWithPath("data.itemDetailViewForm.optionValue").type(JsonFieldType.STRING).description("옵션값"),
                                fieldWithPath("data.itemDetailViewForm.mainImg").type(JsonFieldType.STRING).description("대표사진"),
                                fieldWithPath("data.itemDetailViewForm.subImg").type(JsonFieldType.STRING).description("옵션사진").optional()
                        )))
                .andDo(print());
    }

    @DisplayName("상품전체수정")
    @Test
    public void upItemDetail() throws Exception {
        save();

        //DB에서 상품id하나 가져와서
        //아이템리스트를 전부 가져온다 ( count..)
        List<ItemDetail> testList = itemDetailRepository.findItemDetailPage(itemRepository.findAll().get(0).getItemId());

        //위의 리스트 수만큼 UpdateForm 생성 후
        List<ItemDetailForm.DetailUpdateClass> testUpdateList = new ArrayList<>();

            for (ItemDetail itemDetail : testList) {
                testUpdateList.add(
                        ItemDetailForm.DetailUpdateClass.builder()
                                .itemDetailId(itemDetail.getItemDetailId())
                                .price(20000)
                                .stockQuantity(30)
                                .optionName("수정")
                                .optionValue("수정테스트")
                                .mainImg("수정사진")
                                .subImg("수정이미지")
                                .build()
                );
            }

        //ItemFormUpdate으로 포장하여 테스트
        ItemForm.ItemFormUpdate itemFormUpdate = ItemForm.ItemFormUpdate.builder()
                        .itemId(testList.get(0).getItem().getItemId())
                        .itemName(testList.get(0).getItem().getItemName())
                        .itemComment(testList.get(0).getItem().getItemComment())
                        .itemDetailUpdateClassList(testUpdateList)
                        .build();
            mockMvc.perform(RestDocumentationRequestBuilders.put("/api/item/update/list")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(itemFormUpdate))
                    .characterEncoding("utf-8")
            ).andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").exists())
                    .andDo(document("item/put-update-list",
                            ApiDocumentUtils.getDocumentRequest(),
                            ApiDocumentUtils.getDocumentResponse(),
                            relaxedRequestFields(
                                    fieldWithPath("itemId").type(JsonFieldType.NUMBER).description("상품등록코드"),
                                    fieldWithPath("itemName").type(JsonFieldType.STRING).description("상품명"),
                                    fieldWithPath("itemComment").type(JsonFieldType.STRING).description("상품설명"),

                                    fieldWithPath("itemDetailUpdateClassList.[].itemDetailId").type(JsonFieldType.NUMBER).description("상품옵션등록코드"),
                                    fieldWithPath("itemDetailUpdateClassList.[].price").type(JsonFieldType.NUMBER).description("단가"),
                                    fieldWithPath("itemDetailUpdateClassList.[].stockQuantity").type(JsonFieldType.NUMBER).description("재고"),
                                    fieldWithPath("itemDetailUpdateClassList.[].optionName").type(JsonFieldType.STRING).description("옵션명"),
                                    fieldWithPath("itemDetailUpdateClassList.[].optionValue").type(JsonFieldType.STRING).description("옵션값"),
                                    fieldWithPath("itemDetailUpdateClassList.[].mainImg").type(JsonFieldType.STRING).description("대표사진"),
                                    fieldWithPath("itemDetailUpdateClassList.[].subImg").type(JsonFieldType.STRING).description("옵션사진").optional()
                            ),
                            relaxedResponseFields(
                                    fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                                    fieldWithPath("data.itemId").type(JsonFieldType.NUMBER).description("상품등록코드"),
                                    fieldWithPath("data.itemName").type(JsonFieldType.STRING).description("상품명"),
                                    fieldWithPath("data.itemComment").type(JsonFieldType.STRING).description("상품설명"),

                                    fieldWithPath("data.itemDetailUpdateClassList").type(JsonFieldType.ARRAY).description("상품옵션리스트"),
                                    fieldWithPath("data.itemDetailUpdateClassList.[].itemDetailId").type(JsonFieldType.NUMBER).description("상품옵션등록코드"),
                                    fieldWithPath("data.itemDetailUpdateClassList.[].price").type(JsonFieldType.NUMBER).description("단가"),
                                    fieldWithPath("data.itemDetailUpdateClassList.[].stockQuantity").type(JsonFieldType.NUMBER).description("재고"),
                                    fieldWithPath("data.itemDetailUpdateClassList.[].optionName").type(JsonFieldType.STRING).description("옵션명"),
                                    fieldWithPath("data.itemDetailUpdateClassList.[].optionValue").type(JsonFieldType.STRING).description("옵션값"),
                                    fieldWithPath("data.itemDetailUpdateClassList.[].mainImg").type(JsonFieldType.STRING).description("대표사진"),
                                    fieldWithPath("data.itemDetailUpdateClassList.[].subImg").type(JsonFieldType.STRING).description("옵션사진").optional()
                            )))
                    .andDo(print());
            em.flush();
            em.clear();
    }
}