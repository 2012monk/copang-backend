package com.alconn.copang.item;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest
@Transactional
public class ItemControllerTest {

//    @Autowired
//    ItemController itemController;

    @Autowired
    ItemMapper itemMapper;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ItemService itemService;


    public ItemDetailForm itemDetailFormCreate(){
        ItemDetailForm itemDetailForm=ItemDetailForm.builder()
                .itemName("상품")
                .mainImg("메인이미지")
                .price(10000)
                .optionName("옵션이름")
                .optionValue("옵션값")
                .stockQuantity(10000)
                .build();
        return itemDetailForm;
    }

    @Test
    public void save() throws Exception {
        ItemDetailForm itemDetailForm=itemDetailFormCreate();
        System.out.println("itemDetailForm.toString() = " + itemDetailForm.toString());
        List<ItemDetailForm> itemDetailFormList=new ArrayList<>();
        itemDetailFormList.add(itemDetailForm);

        mockMvc.perform(post("/item/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDetailFormList))
                        .characterEncoding("utf-8")
//                        .header(HttpHeaders.AUTHORIZATION)
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists());
//
//                RestDocumentationRequestBuilders.

    }












//
//    @Autowired
//    private ItemController itemController;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ItemDetailService itemDetailService;
//
//    @Autowired
//    private ItemService itemService;
//
//    @Autowired
//    private ItemMapper itemMapper;
//
//    @Autowired
//    EntityManager em;
//
//    public Item itemCreate() {
//        Item item = Item.builder()
//                .itemName("test")
//                .mainImg("test2")
//                .itemComment("test")
//                .build();
//        return item;
//    }
//
//    public ItemDetail itemDetailCreate2(Item itemInput){
//        ItemDetail itemDetail=ItemDetail.builder()
//                .item(itemInput)
//                .price(10000)
//                .stockQuantity(111)
//                .option("옷")
//                .detailImg("사진")
//                .build();
//        return itemDetail;
//    }
//
//
//
//    @BeforeEach
//    public void setUp() {
//        this.mockMvc = MockMvcBuilders.standaloneSetup(itemController)
////                .addFilter(new CharacterEncodingFilter("UTF-8",true))
//                .build();
//    }
//
//    @DisplayName("상품 전체조회")
//    @Test
//    public void mainView() throws Exception{
//
//       for(int i=0;i<5;i++) {
//           Item item = itemCreate();
//            itemService.saveItem(item);
//
//           ItemDetail itemDetail = itemDetailCreate2(item);
//           itemDetailService.itemDetailSave(itemDetail);
//       }
//
//       List<ItemDetail> itemDetailList = itemDetailService.listItemDetailsALLFind();
//       List<ItemDetailForm> itemDetailFormitemMapperList=itemMapper.listDomainToDto(itemDetailList);
//
////       this.mockMvc.perform(get("/item/list")).andExpect(status().isOk()).andDo(print());
//
//   }
//
//   @Test
//   public void itemDetailFindOne() throws Exception {
//       Item item = itemCreate();
//       itemService.saveItem(item);
//
//       ItemDetail itemDetail = itemDetailCreate2(item);
//       itemDetailService.itemDetailSave(itemDetail);
//
//       ItemDetailForm itemDetailForm=itemMapper.domainToDto(itemDetailService.itemDetailFind(itemDetail.getItemDetailId()));
//
////       System.out.println("itemDetailForm = " + itemDetailForm.getId().toString());
////       this.mockMvc.perform(get("/listOne")
////               .param("itme_id",itemDetailForm.getId().toString()))
////               .andExpect(status().isOk())
////               .andDo(print());
//
//       System.out.println("itemController.itemDetailFindOne= " + itemController.itemDetailfindOne(itemDetailForm.getItemDetailId()).message);
//
//   }
//
//   @Test
//    public void deleteTest(){
//        Item item=itemCreate();
//        itemService.saveItem(item);
//
//        ItemDetail itemDetail=itemDetailService.itemDetailSave(itemDetailCreate2(item));
//
//        System.out.println(" = " + itemController.itemDetailDelete(2L).message);
//
//   }
//
//    @Test
//    public void test(){
//
//    }
}
