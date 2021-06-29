package com.alconn.copang.item;

import com.alconn.copang.aop.GlobalExceptionHandler;
import com.alconn.copang.common.ResponseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/item")
@Slf4j
public class ItemController {

    private final ItemDetailService itemDetailService;

    private final GlobalExceptionHandler globalExceptionHandler;

    //저장
    @PostMapping("/add")
    public ResponseMessage<ItemForm> add(@RequestBody ItemForm itemForm) {
        System.out.println("" );
        System.out.println("itemForm.toString() = " + itemForm.toString());
        ItemForm itemFormReturn=itemDetailService.itemDetailListSave(itemForm);
        return ResponseMessage.<ItemForm>builder()
                .message("저장완료")
                .data(itemFormReturn)
                .build();
    }

    //메인 대표이미지만 출력
    @GetMapping("/list")
    public ResponseMessage<List<ItemDetailForm.MainForm>> list(){
        List<ItemDetailForm.MainForm> itemDetailFormList=itemDetailService.findMainList();
        return ResponseMessage.<List<ItemDetailForm.MainForm>>builder()
                .message("대표리스트")
                .data(itemDetailFormList)
                .build();
    }

    //상세페이지
    @GetMapping("/list/itemDetail/{itemId}")
    public ResponseMessage<ItemForm> itemDetailPageResponse(@PathVariable(name = "itemId")Long id){
        ItemForm itemForm=itemDetailService.findItemDetailPage(id);
        return ResponseMessage.<ItemForm>builder()
                .message("상품상세페이지")
                .data(itemForm)
                .build();

    }

    //1. 상품 삭제
    @DeleteMapping("/delete/itemId/{itemId}")
    public ResponseMessage<ItemForm> itemDel(@PathVariable(name = "itemId")Long id){
        ItemForm itemForm=itemDetailService.delItem(id);
        return ResponseMessage.<ItemForm>builder()
                .message("상품삭제된목록")
                .data(itemForm)
                .build();
    }

    //2. 상품옵션 하나 삭제
    @DeleteMapping("/delete/itemDetail/{itemDetailId}")
    public ResponseMessage<ItemForm> itemDetailDel(@PathVariable(name = "itemDetailId")Long id){
        ItemForm itemForm
                =itemDetailService.delItemDetail(id);
        return ResponseMessage.<ItemForm>builder()
                .message("상품삭제된목록")
                .data(itemForm)
                .build();
    }

//    //상품 전체 수정
//    @PutMapping("/update/itemId/update")
//    public ResponseMessage<ItemForm> itemUpdate(@RequestBody ItemForm.ItemFormUpdate itemForm,
//                                                @PathVariable(name = "itemId")Long id){
//        ItemForm itemForm1=itemDetailService.delItem(id);
//
//        return ResponseMessage.<ItemForm>builder()
//                .message("상품삭제된목록")
//                .data(itemForm1)
//                .build();
//    }

    //옵션하나 수정
//    @PutMapping("/update/itemDetailId/update")
//    public ResponseMessage<ItemForm> itemDetailUpdate(@RequestBody ItemForm.ItemFormUpdate itemForm,
//                                                      @PathVariable(name = "itemDetailId")Long id){
//
//        return ResponseMessage.<ItemDetailForm>builder()
//                .message("상품삭제된목록")
//                .data(itemDetailForm)
//                .build();
//    }

}
