package com.alconn.copang.categoryitem;

import com.alconn.copang.ApiDocumentUtils;
import com.alconn.copang.category.Category;
import com.alconn.copang.category.CategoryRepository;
import com.alconn.copang.category.CategoryService;
import com.alconn.copang.category.dto.CategoryRequest;
import com.alconn.copang.common.ResponseMessage;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.persistence.EntityManager;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

@Disabled
@AutoConfigureMockMvc
@SpringBootTest
@AutoConfigureRestDocs
public class CategoryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    EntityManager em;

    //======== 테스트 데이터 ================
    private Category createCategory(){
        Category category=Category.builder()
                .parentId(0L)
                .categoryName("의류")
                .build();
        return categoryRepository.save(category);
    }

    //저장용 목데이터
    private void testData(){
        Category category=Category.builder()
                .categoryName("의류")
                .parentId(0l)
                .layer(1)
                .build();

        categoryRepository.save(category);

        Category category2= Category.builder()
                .parentId(category.getCategoryId())
                .categoryName("티셔츠")
                .layer(2)
                .build();
        categoryRepository.save(category2);


        Category category3= Category.builder()
                .parentId(category2.getCategoryId())
                .categoryName("반팔티")
                .layer(3)
                .build();
        categoryRepository.save(category3);

        Category category4= Category.builder()
                .parentId(category3.getCategoryId())
                .categoryName("검은색")
                .layer(4)
                .build();
        categoryRepository.save(category4);

    }

    @BeforeEach
    void setUp() {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }


    @Test
    public void layerListTest() throws Exception{
        testData();

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/category/main")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
        ).andExpect(status().isOk()).andDo(print()).andDo(document("category/get-list-main",
                ApiDocumentUtils.getDocumentRequest(),
                ApiDocumentUtils.getDocumentResponse(),
                relaxedResponseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                        fieldWithPath("data.categoryId").type(JsonFieldType.NUMBER).description("카테고리번호"),
                        fieldWithPath("data.categoryName").type(JsonFieldType.STRING).description("카테고리명"),
                        fieldWithPath("data.parentId").type(JsonFieldType.NUMBER).description("부모카테고리번호").optional(),
                        fieldWithPath("data.cildCategory").type(JsonFieldType.ARRAY).description("자식카테고리번호").optional()
        )));
    }


    @Test
    public void listTest() throws Exception{
        createCategory();

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/category/list")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
        ).andExpect(status().isOk()).andDo(print()).andDo(document("category/get-list",
                ApiDocumentUtils.getDocumentRequest(),
                ApiDocumentUtils.getDocumentResponse(),
                relaxedResponseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                        fieldWithPath("data.categoryId").type(JsonFieldType.NUMBER).description("카테고리번호"),
                        fieldWithPath("data.categoryName").type(JsonFieldType.STRING).description("카테고리명"),
                        fieldWithPath("data.parentId").type(JsonFieldType.NUMBER).description("부모카테고리번호").optional(),
                        fieldWithPath("data.cildCategory").type(JsonFieldType.ARRAY).description("자식카테고리번호").optional()
                )));
    }


    @Test
    public void updateTest() throws Exception{
        Category category=createCategory();

        CategoryRequest.CategoryUpdate categoryUpdate= CategoryRequest.CategoryUpdate.builder()
                .categoryId(category.getCategoryId())
                .categoryName("가구")
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/category/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryUpdate))
                .characterEncoding("utf-8")
        ).andExpect(status().isOk()).andDo(print()).andDo(document("category/update",
                ApiDocumentUtils.getDocumentRequest(),
                ApiDocumentUtils.getDocumentResponse(),
                relaxedRequestFields(
                        fieldWithPath("categoryId").type(JsonFieldType.NUMBER).description("카테고리번호"),
                        fieldWithPath("categoryName").type(JsonFieldType.STRING).description("카테고리명"),
                        fieldWithPath("parentId").type(JsonFieldType.NUMBER).description("부모카테고리번호").optional()
                        ),

                relaxedResponseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                        fieldWithPath("data.categoryId").type(JsonFieldType.NUMBER).description("카테고리번호"),
                        fieldWithPath("data.categoryName").type(JsonFieldType.STRING).description("카테고리명"),
                        fieldWithPath("data.parentId").type(JsonFieldType.NUMBER).description("부모카테고리번호")
                )));
    }

    @Test
    public void delTest() throws Exception{
        createCategory();
        Long categoryId=categoryRepository.findAll().get(0).getCategoryId();

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/category/delete/{categoryId}",categoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
        ).andExpect(status().isOk()).andDo(print()).andDo(document("category/delete",
                ApiDocumentUtils.getDocumentRequest(),
                ApiDocumentUtils.getDocumentResponse(),
                pathParameters(
                  parameterWithName("categoryId").description("카테고리번호")
                ),
                relaxedResponseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                        fieldWithPath("data.categoryId").type(JsonFieldType.NUMBER).description("카테고리번호"),
                        fieldWithPath("data.categoryName").type(JsonFieldType.STRING).description("카테고리명"),
                        fieldWithPath("data.parentId").type(JsonFieldType.NUMBER).description("부모카테고리번호")
                )));

    }



    @Test
    public void saveTest() throws Exception{
        CategoryRequest.CategorySave categorySave= CategoryRequest.CategorySave.builder()
                .categoryName("의류")
                .parentId(0l)
                .build();

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/category/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categorySave))
                        .characterEncoding("utf-8")
        ).andExpect(status().isOk()).andDo(print()).andDo(document("category/post-save",
                ApiDocumentUtils.getDocumentRequest(),
                ApiDocumentUtils.getDocumentResponse(),
                relaxedRequestFields(
                        fieldWithPath("categoryName").type(JsonFieldType.STRING).description("카테고리명"),
                        fieldWithPath("parentId").type(JsonFieldType.NUMBER).description("상위 카테고리 번호")
                ),
                relaxedResponseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                        fieldWithPath("data.categoryId").type(JsonFieldType.NUMBER).description("카테고리번호"),
                        fieldWithPath("data.categoryName").type(JsonFieldType.STRING).description("카테고리명"),
                        fieldWithPath("data.parentId").type(JsonFieldType.NUMBER).description("부모카테고리번호")
                )));
    }


}
