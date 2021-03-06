package com.alconn.copang.item;


import com.alconn.copang.ApiDocumentUtils;
import com.alconn.copang.category.Category;
import com.alconn.copang.category.CategoryRepository;
import com.alconn.copang.client.Client;
import com.alconn.copang.client.ClientRepo;
import com.alconn.copang.item.dto.ItemDetailForm;
import com.alconn.copang.item.dto.ItemForm;
import com.alconn.copang.item.mapper.ItemMapper;
import com.alconn.copang.shipment.LogisticCode;
import com.alconn.copang.shipment.ShipmentInfo;
import com.alconn.copang.shipment.ShippingChargeType;
import com.alconn.copang.shipment.dto.ShipmentInfoForm;
import com.alconn.copang.utils.TestUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

@Disabled
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
    //=====
    @Autowired
    CategoryRepository categoryRepository;
    //=====
    @Autowired
    EntityManager em;

    @Autowired
    TestUtils utils;

    @Autowired
    ClientRepo repo;

    Client client;

    @BeforeEach
    void setUp() {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        client = repo.save(utils.getSeller());
    }

    @AfterEach
    void clean() {
        repo.deleteAll();
    }

    //=================????????? ?????????==========
    //=====
    private Category categorySave() {
        Category category = Category.builder()
                .parentId(0l)
                .categoryName("?????????")
                .layer(1)
                .build();
        return category;
    }
//=====

    private ItemForm itemFormTest() {
        categoryRepository.save(categorySave());

        List<ItemDetailForm.DetailForm> list = new ArrayList<>();

        ItemDetailForm.DetailForm detailForm = ItemDetailForm.DetailForm.builder()
                .mainImg("????????????")
                .optionName("????????????")
                .optionValue("?????????")
                .price(10000)
                .stockQuantity(100)
                .subImg("????????????")
                .build();
        ItemDetailForm.DetailForm detailForm2 = ItemDetailForm.DetailForm.builder()
                .mainImg("????????????2")
                .optionName("????????????2")
                .optionValue("?????????2")
                .price(20000)
                .stockQuantity(200)
                .subImg("????????????")
                .build();

        list.add(detailForm2);
        list.add(detailForm);

        ShipmentInfoForm shipmentInfo = ShipmentInfoForm.builder()
                .logisticCompany(LogisticCode.EPOST)
                .freeShipOverPrice(19000)
                .shippingPrice(2500)
                .shippingChargeType(ShippingChargeType.CONDITIONAL_FREE)
                .releaseDate(1111)
                .build();

        ItemForm itemForm = ItemForm.builder()
                .itemName("?????????")
                .itemComment("????????????")
                .categoryId(categoryRepository.findAll().get(0).getCategoryId())
                .brand("??????")
                .shipmentInfoForm(shipmentInfo)
                .itemDetailFormList(list)
                .build();


        return itemForm;
    }

    private void testData() {
        //=====
        Category category = Category.builder()
                .parentId(0l)
                .categoryName("?????????")
                .layer(1)
                .build();

        categoryRepository.save(category);
        //=====

        Item item = Item.builder()
                .itemComment("???????????????")
                .itemName("?????????")
                .brand("??????")
                .category(categoryRepository.findAll().get(0))
                .build();
        item.changeCategory(category);
        itemRepository.save(item);

        ItemDetail itemDetail = ItemDetail.builder()
                .price(100)
                .stockQuantity(20)
                .optionName("??????")
                .optionValue("?????????")
                .mainImg("????????????")
                .build();
        itemDetail.itemConnect(item);

        itemDetailRepository.save(itemDetail);
        em.flush();
        em.clear();
    }


    //=======================================


    @DisplayName("?????? ?????? ??????")
    @Test
    public void optionOneAdd() throws Exception {
        testData();
        Item item = itemRepository.findAll().get(0);
        ItemForm.ItemSingle itemSingle = ItemForm.ItemSingle.builder()
                .itemId(item.getItemId())
                .detailForm(ItemDetailForm.DetailForm.builder()
                        .price(100)
                        .stockQuantity(20)
                        .optionName("??????")
                        .optionValue("?????????")
                        .mainImg("????????????")
                        .subImg("??????????????????")
                        .build())
                .build();

        mockMvc.perform(post("/api/item/add/detail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemSingle))
                        .characterEncoding("utf-8")
                        .header(HttpHeaders.AUTHORIZATION, utils.genHeader(client))
//                        .header(HttpHeaders.AUTHORIZATION)
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andDo(print())
                .andDo(document("item/post-save-detail",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        relaxedRequestFields(
                                fieldWithPath("detailForm.price").type(JsonFieldType.NUMBER).description("??????"),
                                fieldWithPath("detailForm.stockQuantity").type(JsonFieldType.NUMBER).description("??????"),
                                fieldWithPath("detailForm.optionName").type(JsonFieldType.STRING).description("?????????"),
                                fieldWithPath("detailForm.optionValue").type(JsonFieldType.STRING).description("?????????"),
                                fieldWithPath("detailForm.mainImg").type(JsonFieldType.STRING).description("????????????"),
                                fieldWithPath("detailForm.subImg").type(JsonFieldType.STRING).description("????????????").optional()
                        ),
                        relaxedResponseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("???????????????"),
                                fieldWithPath("data.itemId").type(JsonFieldType.NUMBER).description("??????????????????"),

                                fieldWithPath("data.itemDetailViewForm").type(JsonFieldType.OBJECT).description("???????????????????????????"),
                                fieldWithPath("data.itemDetailViewForm.itemDetailId").type(JsonFieldType.NUMBER).description("????????????????????????"),
                                fieldWithPath("data.itemDetailViewForm.price").type(JsonFieldType.NUMBER).description("??????"),
                                fieldWithPath("data.itemDetailViewForm.stockQuantity").type(JsonFieldType.NUMBER).description("??????"),
                                fieldWithPath("data.itemDetailViewForm.optionName").type(JsonFieldType.STRING).description("?????????"),
                                fieldWithPath("data.itemDetailViewForm.optionValue").type(JsonFieldType.STRING).description("?????????"),
                                fieldWithPath("data.itemDetailViewForm.mainImg").type(JsonFieldType.STRING).description("????????????"),
                                fieldWithPath("data.itemDetailViewForm.subImg").type(JsonFieldType.STRING).description("????????????").optional()
                        )));
    }

    //RestDocumentationRequestBuilders -> Rest Doc ????????? ??? ???????????????
    @DisplayName("?????? ??????")
    @Test
    public void save() throws Exception {
        ItemForm itemForm = itemFormTest();
        mockMvc.perform(post("/api/item/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itemForm))
                .characterEncoding("utf-8")
                .header(HttpHeaders.AUTHORIZATION, utils.genHeader(client))
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andDo(print())
                .andDo(document("item/post-save",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        relaxedRequestFields(
                                fieldWithPath("itemName").type(JsonFieldType.STRING).description("?????????"),
                                fieldWithPath("itemComment").type(JsonFieldType.STRING).description("????????????"),
                                fieldWithPath("categoryId").type(JsonFieldType.NUMBER).description("??????????????????"),
                                fieldWithPath("brand").type(JsonFieldType.STRING).description("????????????"),

                                fieldWithPath("itemDetailFormList.[].price").type(JsonFieldType.NUMBER).description("??????"),
                                fieldWithPath("itemDetailFormList.[].stockQuantity").type(JsonFieldType.NUMBER).description("??????"),
                                fieldWithPath("itemDetailFormList.[].optionName").type(JsonFieldType.STRING).description("?????????"),
                                fieldWithPath("itemDetailFormList.[].optionValue").type(JsonFieldType.STRING).description("?????????"),
                                fieldWithPath("itemDetailFormList.[].mainImg").type(JsonFieldType.STRING).description("????????????"),
                                fieldWithPath("itemDetailFormList.[].subImg").type(JsonFieldType.STRING).description("????????????").optional(),

                                fieldWithPath("shipmentInfoForm.logisticCompany").type(JsonFieldType.STRING).description("?????????"),
                                fieldWithPath("shipmentInfoForm.shippingChargeType").type(JsonFieldType.STRING).description("????????????????????????"),
                                fieldWithPath("shipmentInfoForm.freeShipOverPrice").type(JsonFieldType.NUMBER).description("?????????????????????"),
                                fieldWithPath("shipmentInfoForm.releaseDate").type(JsonFieldType.NUMBER).description("?????????"),
                                fieldWithPath("shipmentInfoForm.shippingPrice").type(JsonFieldType.NUMBER).description("?????????")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("???????????????"),
                                fieldWithPath("data.itemName").type(JsonFieldType.STRING).description("?????????"),
                                fieldWithPath("data.itemComment").type(JsonFieldType.STRING).description("????????????"),
                                fieldWithPath("data.itemId").type(JsonFieldType.NUMBER).description("??????????????????"),
                                fieldWithPath("data.categoryId").type(JsonFieldType.NUMBER).description("??????????????????"),
                                fieldWithPath("data.brand").type(JsonFieldType.STRING).description("????????????"),

                                fieldWithPath("data.itemDetailFormList").type(JsonFieldType.ARRAY).description("?????????????????????"),

                                fieldWithPath("data.itemDetailFormList.[].price").type(JsonFieldType.NUMBER).description("??????"),
                                fieldWithPath("data.itemDetailFormList.[].stockQuantity").type(JsonFieldType.NUMBER).description("??????"),
                                fieldWithPath("data.itemDetailFormList.[].optionName").type(JsonFieldType.STRING).description("?????????"),
                                fieldWithPath("data.itemDetailFormList.[].optionValue").type(JsonFieldType.STRING).description("?????????"),
                                fieldWithPath("data.itemDetailFormList.[].mainImg").type(JsonFieldType.STRING).description("????????????"),
                                fieldWithPath("data.itemDetailFormList.[].itemDetailId").type(JsonFieldType.NUMBER).description("????????????????????????"),

                                fieldWithPath("data.shipmentInfoForm.id").type(JsonFieldType.NUMBER).description("???????????????????????????"),
                                fieldWithPath("data.shipmentInfoForm.logisticCompany").type(JsonFieldType.STRING).description("?????????"),
                                fieldWithPath("data.shipmentInfoForm.shippingChargeType").type(JsonFieldType.STRING).description("????????????????????????"),
                                fieldWithPath("data.shipmentInfoForm.freeShipOverPrice").type(JsonFieldType.NUMBER).description("?????????????????????"),
                                fieldWithPath("data.shipmentInfoForm.releaseDate").type(JsonFieldType.NUMBER).description("?????????"),
                                fieldWithPath("data.shipmentInfoForm.shippingPrice").type(JsonFieldType.NUMBER).description("?????????")
                        )));
    }

    @DisplayName("????????????")
    @Test
    public void list() throws Exception {
        save();
        save();
        save();
        int pageNumber = 0;
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/item/list/{pageNumber}", pageNumber)
                .characterEncoding("utf-8")

                .header(HttpHeaders.AUTHORIZATION, utils.genHeader(client))
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andDo(print())
                .andDo(document("item/get-mainlist",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        pathParameters(
                                parameterWithName("pageNumber").description("???????????????")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("???????????????"),
                                fieldWithPath("data.list.[].itemName").type(JsonFieldType.STRING).description("?????????"),
                                fieldWithPath("data.list.[].itemId").type(JsonFieldType.NUMBER).description("??????????????????"),
                                fieldWithPath("data.list.[].itemDetailId").type(JsonFieldType.NUMBER).description("????????????????????????"),
                                fieldWithPath("data.list.[].price").type(JsonFieldType.NUMBER).description("??????"),
                                fieldWithPath("data.list.[].mainImg").type(JsonFieldType.STRING).description("????????????"),
                                fieldWithPath("data.list.[].shipmentInfoForm.id").type(JsonFieldType.NUMBER).description("???????????????????????????"),
                                fieldWithPath("data.list.[].shipmentInfoForm.logisticCompany").type(JsonFieldType.STRING).description("?????????"),
                                fieldWithPath("data.list.[].shipmentInfoForm.shippingChargeType").type(JsonFieldType.STRING).description("????????????????????????"),
                                fieldWithPath("data.list.[].shipmentInfoForm.freeShipOverPrice").type(JsonFieldType.NUMBER).description("?????????????????????"),
                                fieldWithPath("data.list.[].shipmentInfoForm.releaseDate").type(JsonFieldType.NUMBER).description("?????????"),
                                fieldWithPath("data.list.[].shipmentInfoForm.shippingPrice").type(JsonFieldType.NUMBER).description("?????????")

                        )));
    }

    @DisplayName("?????????????????????")
    @Test
    public void listPage() throws Exception {
        save();
        List<Item> item = itemService.itemFindAll();
        Long itemId = item.get(0).getItemId();

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/item/list/itemid={itemId}", itemId)
                .characterEncoding("utf-8")
                .header(HttpHeaders.AUTHORIZATION, utils.genHeader(client))
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andDo(document("item/get-itemlist",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        pathParameters(
                                parameterWithName("itemId").description("??????????????????")
                        ),

                        relaxedResponseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("???????????????"),
                                fieldWithPath("data.itemId").type(JsonFieldType.NUMBER).description("??????????????????"),
                                fieldWithPath("data.itemName").type(JsonFieldType.STRING).description("?????????"),
                                fieldWithPath("data.itemComment").type(JsonFieldType.STRING).description("????????????"),

                                fieldWithPath("data.itemDetailFormList").type(JsonFieldType.ARRAY).description("??????????????????"),
                                fieldWithPath("data.itemDetailFormList.[].itemDetailId").type(JsonFieldType.NUMBER).description("????????????????????????"),
                                fieldWithPath("data.itemDetailFormList.[].price").type(JsonFieldType.NUMBER).description("??????"),
                                fieldWithPath("data.itemDetailFormList.[].stockQuantity").type(JsonFieldType.NUMBER).description("??????"),
                                fieldWithPath("data.itemDetailFormList.[].optionName").type(JsonFieldType.STRING).description("?????????"),
                                fieldWithPath("data.itemDetailFormList.[].optionValue").type(JsonFieldType.STRING).description("?????????"),
                                fieldWithPath("data.itemDetailFormList.[].mainImg").type(JsonFieldType.STRING).description("????????????"),
                                fieldWithPath("data.itemDetailFormList.[].subImg").type(JsonFieldType.STRING).description("????????????").optional(),

                                fieldWithPath("data.shipmentInfoForm.id").type(JsonFieldType.NUMBER).description("???????????????????????????"),
                                fieldWithPath("data.shipmentInfoForm.logisticCompany").type(JsonFieldType.STRING).description("?????????"),
                                fieldWithPath("data.shipmentInfoForm.shippingChargeType").type(JsonFieldType.STRING).description("????????????????????????"),
                                fieldWithPath("data.shipmentInfoForm.freeShipOverPrice").type(JsonFieldType.NUMBER).description("?????????????????????"),
                                fieldWithPath("data.shipmentInfoForm.releaseDate").type(JsonFieldType.NUMBER).description("?????????"),
                                fieldWithPath("data.shipmentInfoForm.shippingPrice").type(JsonFieldType.NUMBER).description("?????????")

                        )))
                .andDo(print());
    }


    @DisplayName("????????????")
    @Test
    public void delItem() throws Exception {
        save();
        List<Item> item = itemService.itemFindAll();
        Long itemId = item.get(0).getItemId();

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/item/delete/{itemId}", itemId)
                .characterEncoding("utf-8")
                .header(HttpHeaders.AUTHORIZATION, utils.genHeader(client))
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andDo(document("item/delete-item",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        pathParameters(
                                parameterWithName("itemId").description("??????????????????")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("???????????????"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("????????? ????????????")
                        )))
                .andDo(print());
    }

    @DisplayName("??????????????????")
    @Test
    public void delItemDetail() throws Exception {
        save();
        List<Item> item = itemService.itemFindAll();
        Long itemDetailId = item.get(0).getItemDetails().get(0).getItemDetailId();

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/item/delete/item-detail/{itemDetailId}", itemDetailId)
                .characterEncoding("utf-8")
                .header(HttpHeaders.AUTHORIZATION, utils.genHeader(client))
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andDo(document("item/delete-itemDetail",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        pathParameters(
                                parameterWithName("itemDetailId").description("???????????????")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("???????????????"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("????????? ??????????????????")
                        )))
                .andDo(print());
    }

    @DisplayName("??????????????????")
    @Test
    public void upItemDetailOne() throws Exception {
        testData();
        ItemDetail itemDetail = itemDetailRepository.findAll().get(0);
        ItemForm.ItemFormUpdateSingle updateSingle = ItemForm.ItemFormUpdateSingle.builder()
                .itemId(itemDetail.getItem().getItemId())
                .itemName("??????")
                .itemComment("????????????")
                //=====
                .categoryId(categoryRepository.findAll().get(0).getCategoryId())
                //=====
                .detailUpdateClass(ItemDetailForm.DetailForm.builder()
                        .itemDetailId(itemDetail.getItemDetailId())
                        .price(10000)
                        .stockQuantity(10)
                        .optionName("??????")
                        .optionValue("??????")
                        .mainImg("?????????????????????")
                        .subImg("??????????????????")
                        .build()
                )
                .build();
        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/item/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateSingle))
                .characterEncoding("utf-8")
                .header(HttpHeaders.AUTHORIZATION, utils.genHeader(client))
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andDo(document("item/put-update",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        relaxedRequestFields(
                                fieldWithPath("itemId").type(JsonFieldType.NUMBER).description("??????????????????"),
                                fieldWithPath("itemName").type(JsonFieldType.STRING).description("?????????"),
                                fieldWithPath("itemComment").type(JsonFieldType.STRING).description("????????????"),
                                fieldWithPath("categoryId").type(JsonFieldType.NUMBER).description("??????????????????"),

                                fieldWithPath("detailUpdateClass.itemDetailId").type(JsonFieldType.NUMBER).description("????????????????????????"),
                                fieldWithPath("detailUpdateClass.price").type(JsonFieldType.NUMBER).description("??????"),
                                fieldWithPath("detailUpdateClass.stockQuantity").type(JsonFieldType.NUMBER).description("??????"),
                                fieldWithPath("detailUpdateClass.optionName").type(JsonFieldType.STRING).description("?????????"),
                                fieldWithPath("detailUpdateClass.optionValue").type(JsonFieldType.STRING).description("?????????"),
                                fieldWithPath("detailUpdateClass.mainImg").type(JsonFieldType.STRING).description("????????????"),
                                fieldWithPath("detailUpdate Class.subImg").type(JsonFieldType.STRING).description("????????????").optional()
                        ),
                        relaxedResponseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("???????????????"),
                                fieldWithPath("data.itemId").type(JsonFieldType.NUMBER).description("??????????????????"),
                                fieldWithPath("data.itemName").type(JsonFieldType.STRING).description("?????????"),
                                fieldWithPath("data.itemComment").type(JsonFieldType.STRING).description("????????????"),
                                fieldWithPath("data.categoryId").type(JsonFieldType.NUMBER).description("??????????????????"),

                                fieldWithPath("data.itemDetailViewForm").type(JsonFieldType.OBJECT).description("?????????????????????"),
                                fieldWithPath("data.itemDetailViewForm.itemDetailId").type(JsonFieldType.NUMBER).description("????????????????????????"),
                                fieldWithPath("data.itemDetailViewForm.price").type(JsonFieldType.NUMBER).description("??????"),
                                fieldWithPath("data.itemDetailViewForm.stockQuantity").type(JsonFieldType.NUMBER).description("??????"),
                                fieldWithPath("data.itemDetailViewForm.optionName").type(JsonFieldType.STRING).description("?????????"),
                                fieldWithPath("data.itemDetailViewForm.optionValue").type(JsonFieldType.STRING).description("?????????"),
                                fieldWithPath("data.itemDetailViewForm.mainImg").type(JsonFieldType.STRING).description("????????????"),
                                fieldWithPath("data.itemDetailViewForm.subImg").type(JsonFieldType.STRING).description("????????????").optional()
                        )))
                .andDo(print());
    }

    @DisplayName("??????????????????")
    @Test
    public void upItemDetail() throws Exception {
        save();

        //DB?????? ??????id?????? ????????????
        //????????????????????? ?????? ???????????? ( count..)
        List<ItemDetail> testList = itemDetailRepository.findItemDetailPage(itemRepository.findAll().get(0).getItemId());

        //?????? ????????? ????????? UpdateForm ?????? ???
        List<ItemDetailForm.DetailForm> testUpdateList = new ArrayList<>();

        for (ItemDetail itemDetail : testList) {
            testUpdateList.add(
                    ItemDetailForm.DetailForm.builder()
                            .itemDetailId(itemDetail.getItemDetailId())
                            .price(20000)
                            .stockQuantity(30)
                            .optionName("??????")
                            .optionValue("???????????????")
                            .mainImg("????????????")
                            .subImg("???????????????")
                            .build()
            );
        }

        ShipmentInfoForm shipmentInfoForm = ShipmentInfoForm.builder()
                .id(testList.get(0).getItem().getShipmentInfo().getId())
                .freeShipOverPrice(2600)
                .logisticCompany(LogisticCode.CJGLS)
                .releaseDate(351)
                .shippingChargeType(ShippingChargeType.FREE)
                .shippingPrice(10000)
                .build();


        //ItemFormUpdate?????? ???????????? ?????????
        ItemForm itemFormUpdate = ItemForm.builder()
                .itemId(testList.get(0).getItem().getItemId())
                .itemName(testList.get(0).getItem().getItemName())
                .itemComment(testList.get(0).getItem().getItemComment())
                .shipmentInfoForm(shipmentInfoForm)
                //====
                .categoryId(categoryRepository.findAll().get(0).getCategoryId())
                //====

                .itemDetailFormList(testUpdateList)
                .build();
        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/item/update/list")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itemFormUpdate))
                .characterEncoding("utf-8")
                .header(HttpHeaders.AUTHORIZATION, utils.genHeader(client))
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andDo(document("item/put-update-list",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        relaxedRequestFields(
                                fieldWithPath("itemId").type(JsonFieldType.NUMBER).description("??????????????????"),
                                fieldWithPath("itemName").type(JsonFieldType.STRING).description("?????????"),
                                fieldWithPath("itemComment").type(JsonFieldType.STRING).description("????????????"),

                                //====
                                fieldWithPath("categoryId").type(JsonFieldType.NUMBER).description("??????????????????"),
                                //====

                                fieldWithPath("itemDetailFormList.[].itemDetailId").type(JsonFieldType.NUMBER).description("????????????????????????"),
                                fieldWithPath("itemDetailFormList.[].price").type(JsonFieldType.NUMBER).description("??????"),
                                fieldWithPath("itemDetailFormList.[].stockQuantity").type(JsonFieldType.NUMBER).description("??????"),
                                fieldWithPath("itemDetailFormList.[].optionName").type(JsonFieldType.STRING).description("?????????"),
                                fieldWithPath("itemDetailFormList.[].optionValue").type(JsonFieldType.STRING).description("?????????"),
                                fieldWithPath("itemDetailFormList.[].mainImg").type(JsonFieldType.STRING).description("????????????"),
                                fieldWithPath("itemDetailFormList.[].subImg").type(JsonFieldType.STRING).description("????????????").optional(),

                                fieldWithPath("shipmentInfoForm.logisticCompany").type(JsonFieldType.STRING).description("?????????"),
                                fieldWithPath("shipmentInfoForm.shippingChargeType").type(JsonFieldType.STRING).description("????????????????????????"),
                                fieldWithPath("shipmentInfoForm.freeShipOverPrice").type(JsonFieldType.NUMBER).description("?????????????????????"),
                                fieldWithPath("shipmentInfoForm.releaseDate").type(JsonFieldType.NUMBER).description("?????????"),
                                fieldWithPath("shipmentInfoForm.shippingPrice").type(JsonFieldType.NUMBER).description("?????????")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("???????????????"),
                                fieldWithPath("data.itemId").type(JsonFieldType.NUMBER).description("??????????????????"),
                                fieldWithPath("data.itemName").type(JsonFieldType.STRING).description("?????????"),
                                fieldWithPath("data.itemComment").type(JsonFieldType.STRING).description("????????????"),

                                //====
                                fieldWithPath("data.categoryId").type(JsonFieldType.NUMBER).description("??????????????????"),
                                //====

                                fieldWithPath("data.itemDetailFormList").type(JsonFieldType.ARRAY).description("?????????????????????"),
                                fieldWithPath("data.itemDetailFormList.[].itemDetailId").type(JsonFieldType.NUMBER).description("????????????????????????"),
                                fieldWithPath("data.itemDetailFormList.[].price").type(JsonFieldType.NUMBER).description("??????"),
                                fieldWithPath("data.itemDetailFormList.[].stockQuantity").type(JsonFieldType.NUMBER).description("??????"),
                                fieldWithPath("data.itemDetailFormList.[].optionName").type(JsonFieldType.STRING).description("?????????"),
                                fieldWithPath("data.itemDetailFormList.[].optionValue").type(JsonFieldType.STRING).description("?????????"),
                                fieldWithPath("data.itemDetailFormList.[].mainImg").type(JsonFieldType.STRING).description("????????????"),
                                fieldWithPath("data.itemDetailFormList.[].subImg").type(JsonFieldType.STRING).description("????????????").optional(),
                                fieldWithPath("data.shipmentInfoForm.id").type(JsonFieldType.NUMBER).description("???????????????????????????"),
                                fieldWithPath("data.shipmentInfoForm.logisticCompany").type(JsonFieldType.STRING).description("?????????"),
                                fieldWithPath("data.shipmentInfoForm.shippingChargeType").type(JsonFieldType.STRING).description("????????????????????????"),
                                fieldWithPath("data.shipmentInfoForm.freeShipOverPrice").type(JsonFieldType.NUMBER).description("?????????????????????"),
                                fieldWithPath("data.shipmentInfoForm.releaseDate").type(JsonFieldType.NUMBER).description("?????????"),
                                fieldWithPath("data.shipmentInfoForm.shippingPrice").type(JsonFieldType.NUMBER).description("?????????")
                        )))
                .andDo(print());
        em.flush();
        em.clear();
    }
}