//package com.alconn.copang.categoryitem;
//
//import com.alconn.copang.ApiDocumentUtils;
//import com.alconn.copang.category.Category;
//import com.alconn.copang.category.CategoryForm;
//import com.alconn.copang.category.CategoryRepository;
//import com.alconn.copang.category.CategoryService;
//import com.alconn.copang.common.ResponseMessage;
//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
//import org.springframework.restdocs.payload.JsonFieldType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//
//import javax.persistence.EntityManager;
//
//import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.restdocs.payload.PayloadDocumentation.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
//import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
//import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
//
//
//@AutoConfigureMockMvc
//@SpringBootTest
//@AutoConfigureRestDocs
//public class CategortControllerTest {
//
//    @Autowired
//    MockMvc mockMvc;
//
//    @Autowired
//    CategoryRepository categoryRepository;
//
//    @Autowired
//    ObjectMapper objectMapper;
//
//    @Autowired
//    EntityManager em;
//
//    //======== 테스트 데이터 ================
//    private void createCategory(){
//        Category category=Category.builder()
//                .parentId(0L)
//                .categoryName("의류")
//                .build();
//        categoryRepository.save(category);
////        em.flush();
////        em.clear();
//    }
//
//    private Long aLong(){
//        createCategory();
//        return categoryRepository.findAll().get(0).getParentId();
//    }
//
//    private CategoryForm.CategorySaveTop categorySaveTop(){
//        return CategoryForm.CategorySaveTop.builder()
//                .categoryName("의류")
//                .build();
//    }
//
//    private CategoryForm.CategorySaveForm categorySaveForm(){
//
//        return CategoryForm.CategorySaveForm.builder()
//                .categoryName("티셔츠")
//                .parentId(aLong())
//                .build();
//    }
//
//
//
//    //========================
//
//
//    @BeforeEach
//    void setUp() {
//        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//    }
//
//    @Test
//    public void saveTopTest() throws Exception{
//        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/category/top")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(categorySaveTop()))
//                        .characterEncoding("utf-8")
////                        .header(HttpHeaders.AUTHORIZATION)
//        ).andExpect(status().isOk()).andDo(print()).andDo(document("category/post-save-top",
//                ApiDocumentUtils.getDocumentRequest(),
//                ApiDocumentUtils.getDocumentResponse(),
//                relaxedRequestFields(
//                        fieldWithPath("categoryName").type(JsonFieldType.STRING).description("카테고리명")
//                ),
//                relaxedResponseFields(
//                        fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
//                        fieldWithPath("data.categoryId").type(JsonFieldType.NUMBER).description("카테고리번호"),
//                        fieldWithPath("data.categoryName").type(JsonFieldType.STRING).description("카테고리명"),
//                        fieldWithPath("data.parentId").type(JsonFieldType.NUMBER).description("부모카테고리번호")
//
//                )));
//    }@Test
//    public void categorySaveTest() throws Exception{
//        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/category/bottom")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(categorySaveForm()))
//                        .characterEncoding("utf-8")
////                        .header(HttpHeaders.AUTHORIZATION)
//        ).andExpect(status().isOk()).andDo(print()).andDo(document("category/post-save-bottom",
//                ApiDocumentUtils.getDocumentRequest(),
//                ApiDocumentUtils.getDocumentResponse(),
//                relaxedRequestFields(
//                        fieldWithPath("categoryName").type(JsonFieldType.STRING).description("카테고리명"),
//                        fieldWithPath("data.parentId").type(JsonFieldType.NUMBER).description("부모카테고리번호")
//                ),
//                relaxedResponseFields(
//                        fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
//                        fieldWithPath("data.categoryId").type(JsonFieldType.NUMBER).description("카테고리번호"),
//                        fieldWithPath("data.categoryName").type(JsonFieldType.STRING).description("카테고리명"),
//                        fieldWithPath("data.parentId").type(JsonFieldType.NUMBER).description("부모카테고리번호")
//
//                )));
//    }
//
////    @PostMapping("/top")
////    public ResponseMessage<CategoryForm.CategorySaveTop> parentSave(@RequestBody CategoryForm.CategorySaveTop categorySaveTop){
////        categorySaveTop=categoryService.saveTop(categorySaveTop);
////
////        return ResponseMessage.<CategoryForm.CategorySaveTop>builder()
////                .message("메인카테고리저장")
////                .data(categorySaveTop)
////                .build();
////    }
//
//
//}
