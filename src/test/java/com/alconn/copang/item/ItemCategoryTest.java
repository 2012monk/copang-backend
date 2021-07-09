package com.alconn.copang.item;

import com.alconn.copang.ApiDocumentUtils;
import com.alconn.copang.category.Category;
import com.alconn.copang.category.CategoryRepository;
import com.alconn.copang.shipment.LogisticCode;
import com.alconn.copang.shipment.ShipmentInfo;
import com.alconn.copang.shipment.ShipmentInfoRepository;
import com.alconn.copang.shipment.ShippingChargeType;
import com.alconn.copang.shipment.dto.ShipmentInfoForm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@AutoConfigureRestDocs
public class ItemCategoryTest {

    @Autowired
    private ItemDetailRepository itemDetailRepository;
    @Autowired
    private ItemDetailService itemDetailService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ShipmentInfoRepository shipmentInfoRepository;

    @Autowired
    private MockMvc mockMvc;

    //통합 테스트
    private void testData() {
        //카테고리 생성
        Category category = Category.builder()
                .childCheck("Y")
                .parentId(0l)
                .categoryName("의류")
                .build();
        categoryRepository.save(category);

        Category category2 = Category.builder()
                .childCheck("Y")
                .parentId(category.getCategoryId())
                .categoryName("남자패션")
                .build();
        categoryRepository.save(category2);

        Category category3 = Category.builder()
                .childCheck("N")
                .parentId(category2.getCategoryId())
                .categoryName("티셔츠")
                .build();
        categoryRepository.save(category3);

        Category category4 = Category.builder()
                .childCheck("N")
                .parentId(category2.getCategoryId())
                .categoryName("바지")
                .build();
        categoryRepository.save(category4);

        ShipmentInfo shipmentInfoForm = ShipmentInfo.builder()
                .freeShipOverPrice(2600)
                .logisticCompany(LogisticCode.CJGLS)
                .releaseDate(351)
                .shippingChargeType(ShippingChargeType.FREE)
                .shippingPrice(10000)
                .build();
        shipmentInfoRepository.save(shipmentInfoForm);


        //상품 생성
        Item item = Item.builder()
                .itemName("반팔티")
                .category(category3)
                .itemComment("반팔티입니다")
                .shipmentInfo(shipmentInfoForm)
                .build();
        item.changeCategory(category3);
        itemRepository.save(item);

        ItemDetail itemDetail = ItemDetail.builder()
                .optionName("색상")
                .optionValue("하얀색")
                .price(2000)
                .itemMainApply(ItemMainApply.APPLY)
                .stockQuantity(20)
                .mainImg("메인사진")
                .subImg("옵션사진")
                .build();
        itemDetail.itemConnect(item);
        itemDetailRepository.save(itemDetail);

        ShipmentInfo shipmentInfoForm2 = ShipmentInfo.builder()
                .freeShipOverPrice(2600)
                .logisticCompany(LogisticCode.CJGLS)
                .releaseDate(351)
                .shippingChargeType(ShippingChargeType.FREE)
                .shippingPrice(10000)
                .build();
        shipmentInfoRepository.save(shipmentInfoForm2);


        Item item33 = Item.builder()
                .itemName("반팔티")
                .category(category2)
                .itemComment("설명입니다")
                .shipmentInfo(shipmentInfoForm2)
                .build();
        item33.changeCategory(category2);
        itemRepository.save(item33);

        ItemDetail itemDetai33 = ItemDetail.builder()
                .optionName("색상")
                .optionValue("하얀색")
                .price(300000)
                .itemMainApply(ItemMainApply.APPLY)
                .stockQuantity(20)
                .mainImg("메인사진")
                .subImg("옵션사진")
                .build();
        itemDetai33.itemConnect(item33);
        itemDetailRepository.save(itemDetai33);

        System.out.println("category2 = " + category2.getCategoryId());
        System.out.println("category2.getParentId() = " + category2.getParentId());

        ItemDetail itemDetail2 = ItemDetail.builder()
                .optionName("색상")
                .optionValue("검은색")
                .price(10000)
                .itemMainApply(ItemMainApply.NON)
                .stockQuantity(10)
                .mainImg("메인사진")
                .subImg("옵션사진")
                .build();
        itemDetail2.itemConnect(item);
        itemDetailRepository.save(itemDetail);

        ShipmentInfo shipmentInfoForm3 = ShipmentInfo.builder()
                .freeShipOverPrice(2600)
                .logisticCompany(LogisticCode.CJGLS)
                .releaseDate(351)
                .shippingChargeType(ShippingChargeType.FREE)
                .shippingPrice(10000)
                .build();
        shipmentInfoRepository.save(shipmentInfoForm3);

        Item item2 = Item.builder()
                .itemName("검은티")
                .category(category4)
                .itemComment("반팔티입니다")
                .shipmentInfo(shipmentInfoForm3)
                .build();
        item2.changeCategory(category4);
        itemRepository.save(item2);

        ItemDetail itemDetail12 = ItemDetail.builder()
                .optionName("색상")
                .optionValue("검은색")
                .price(10000)
                .itemMainApply(ItemMainApply.APPLY)
                .stockQuantity(10)
                .mainImg("메인사진")
                .subImg("옵션사진")
                .build();
        itemDetail12.itemConnect(item2);
        itemDetailRepository.save(itemDetail12);

    }


    //테스트
    @Test
    public void findCategpryMainListTest() throws Exception {
        testData();
        Long id = categoryRepository.findAll().get(0).getCategoryId();
    }

    @DisplayName("카테고리 상품목록")
    @Test
    public void categoryItem() throws Exception {
        testData();
        Long categoryId = categoryRepository.findAll().get(0).getCategoryId();
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/item/list/categoryid={categoryId}", 0l)
                .characterEncoding("utf-8")
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andDo(document("item/get-categorymainlist",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        pathParameters(
                                parameterWithName("categoryId").description("카테고리번호")
                        ),

                        relaxedResponseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("data.[].itemId").type(JsonFieldType.NUMBER).description("상품등록코드"),
                                fieldWithPath("data.[].itemName").type(JsonFieldType.STRING).description("상품명"),
                                fieldWithPath("data.[].itemDetailId").type(JsonFieldType.NUMBER).description("상품옵션등록코드"),
                                fieldWithPath("data.[].categoryId").type(JsonFieldType.NUMBER).description("카테고리번호"),
                                fieldWithPath("data.[].price").type(JsonFieldType.NUMBER).description("상품가격"),
                                fieldWithPath("data.[].mainImg").type(JsonFieldType.STRING).description("메인사진"),
                                fieldWithPath("data.[].shipmentInfoForm.id").type(JsonFieldType.NUMBER).description("판매자배송등록번호"),
                                fieldWithPath("data.[].shipmentInfoForm.logisticCompany").type(JsonFieldType.STRING).description("택배사"),
                                fieldWithPath("data.[].shipmentInfoForm.shippingChargeType").type(JsonFieldType.STRING).description("배송요금변동사항"),
                                fieldWithPath("data.[].shipmentInfoForm.freeShipOverPrice").type(JsonFieldType.NUMBER).description("초과시무료금액"),
                                fieldWithPath("data.[].shipmentInfoForm.releaseDate").type(JsonFieldType.NUMBER).description("출고일"),
                                fieldWithPath("data.[].shipmentInfoForm.shippingPrice").type(JsonFieldType.NUMBER).description("배송비")

                        )))
                .andDo(print());
    }

}
