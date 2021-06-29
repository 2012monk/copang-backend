package com.alconn.copang.item;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.annotation.Commit;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest
@Transactional
public class ItemControllerTest {

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


    //=================테스트 데이터==========


    public ItemForm itemFormTest() {
        List<ItemDetailForm.DetailForm> list = new ArrayList<>();

        ItemDetailForm.DetailForm detailForm = ItemDetailForm.DetailForm.builder()
                .mainImg("사진")
                .optionName("옵션이름")
                .optionValue("옵션값")
                .price(10000)
                .stockQuantity(100)
                .build();
        ItemDetailForm.DetailForm detailForm2 = ItemDetailForm.DetailForm.builder()
                .mainImg("사진2")
                .optionName("옵션이름2")
                .optionValue("옵션값2")
                .price(20000)
                .stockQuantity(200)
                .build();
        list.add(detailForm2);
        list.add(detailForm);

        ItemForm itemForm = ItemForm.builder()
                .itemName("상품명")
                .itemDetailFormList(list)
                .build();

        return itemForm;
    }
    //=======================================

    //RestDocumentationRequestBuilders -> Rest Doc 작성할 때 사용한다함
    @DisplayName("상품저장")
    @Test
    public void save() throws Exception {
        ItemForm itemForm = itemFormTest();

        mockMvc.perform(post("/item/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemForm))
                        .characterEncoding("utf-8")
//                        .header(HttpHeaders.AUTHORIZATION)
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists());
    }

    @DisplayName("대표상품페이지")
    @Test
    public void list() throws Exception {
        save();

        mockMvc.perform(RestDocumentationRequestBuilders.get("/item/list")
                .characterEncoding("utf-8")
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists()).andDo(print());
    }

    @DisplayName("상품상세페이지")
    @Test
    public void listPage() throws Exception {
        save();
        List<Item> item=itemService.itemFindAll();
        Long itemId=item.get(0).getItemId();

        mockMvc.perform(RestDocumentationRequestBuilders.get("/item/list/itemDetail/" + itemId)
                .characterEncoding("utf-8")
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists()).andDo(print());
    }


    @DisplayName("상품삭제")
    @Test
    public void delItem() throws Exception {
        save();
        List<Item> item=itemService.itemFindAll();
        Long itemId=item.get(0).getItemId();
        
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/item/delete/itemId/" + itemId)
                .characterEncoding("utf-8")
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists()).andDo(print());
    }

    @DisplayName("상품옵션삭제")
    @Test
    public void delItemDetail() throws Exception {
        save();
        List<Item> item=itemService.itemFindAll();
        Long itemDetailId=item.get(0).getItemDetails().get(0).getItemDetailId();

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/item/delete/itemDetail/" + itemDetailId)
                .characterEncoding("utf-8")
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists()).andDo(print());

    }

}