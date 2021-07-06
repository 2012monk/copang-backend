package com.alconn.copang.item;

import com.alconn.copang.ApiDocumentUtils;
import com.alconn.copang.category.Category;
import com.alconn.copang.category.CategoryRepository;
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
    private  ItemDetailRepository itemDetailRepository;
    @Autowired
    private  ItemDetailService itemDetailService;
    @Autowired
    private  CategoryRepository categoryRepository;
    @Autowired
    private  ItemRepository  itemRepository;

    @Autowired
    private MockMvc mockMvc;

    //통합 테스트
    private void testData(){
        //카테고리 생성
    Category category=Category.builder()
            .childCheck("Y")
            .parentId(0l)
            .categoryName("의류")
            .build();
    categoryRepository.save(category);

    Category category2=Category.builder()
            .childCheck("Y")
            .parentId(category.getCategoryId())
            .categoryName("남자패션")
            .build();
    categoryRepository.save(category2);

    Category category3=Category.builder()
            .childCheck("N")
            .parentId(category2.getCategoryId())
            .categoryName("티셔츠")
            .build();
    categoryRepository.save(category3);

    Category category4=Category.builder()
            .childCheck("N")
            .parentId(category2.getCategoryId())
            .categoryName("바지")
            .build();
    categoryRepository.save(category4);

        //상품 생성
    Item item=Item.builder()
            .itemName("반팔티")
            .category(category3)
            .itemComment("반팔티입니다")
            .build();
    item.changeCategory(category3);
    itemRepository.save(item);

    ItemDetail itemDetail=ItemDetail.builder()
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

    Item item33=Item.builder()
            .itemName("반팔티")
            .category(category2)
            .itemComment("설명입니다")
            .build();
    item33.changeCategory(category2);
    itemRepository.save(item33);

    ItemDetail itemDetai33=ItemDetail.builder()
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

    ItemDetail itemDetail2=ItemDetail.builder()
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

        Item item2=Item.builder()
                .itemName("검은티")
                .category(category4)
                .itemComment("반팔티입니다")
                .build();
        item2.changeCategory(category4);
        itemRepository.save(item2);

        ItemDetail itemDetail12=ItemDetail.builder()
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
    public void findCategpryMainListTest() throws Exception{
        testData();
        System.out.println("categoryRepository.findAll().get(0) = " + categoryRepository.findAll().get(0).getCategoryId());
        Long id=categoryRepository.findAll().get(0).getCategoryId();
        System.out.println(" = " + itemDetailService.findCategpryMainList(id).toString());
    }

    @DisplayName("카테고리 상품목록")
    @Test
    public void categoryItem() throws Exception{
        testData();
        Long categoryId=categoryRepository.findAll().get(0).getCategoryId();
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
                                fieldWithPath("data.[].itemDetailId").type(JsonFieldType.NUMBER).description("카테고리번호"),
                                fieldWithPath("data.[].categoryId").type(JsonFieldType.NUMBER).description("카테고리번호"),
                                fieldWithPath("data.[].price").type(JsonFieldType.NUMBER).description("카테고리번호"),
                                fieldWithPath("data.[].mainImg").type(JsonFieldType.STRING).description("카테고리번호")

                        )))
                .andDo(print());
    }

}
