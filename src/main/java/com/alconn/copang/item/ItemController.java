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
//    public ResponseMessage<List<ItemDetailForm>> add( @RequestBody List<ItemDetailForm> itemDetailFormList) {
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
    public ResponseMessage<ItemDetailForm> itemDetailDel(@PathVariable(name = "itemDetailId")Long id){
        ItemDetailForm itemDetailForm=itemDetailService.delItemDetail(id);
        return ResponseMessage.<ItemDetailForm>builder()
                .message("상품삭제된목록")
                .data(itemDetailForm)
                .build();
    }




//    @GetMapping("/list")
//    public ResponseMessage<List<ItemDetailForm>> itemDetailList(){
////
////            List<ItemDetail> itemDetails=itemDetailService.listItemDetailsALLFind();
////            System.out.println(itemDetails.toString());
////
////            List<ItemDetailForm> itemDetailForms= itemMapper.listDomainToDto(itemDetails);
////            System.out.println("itemDetailForm.toString() = " + itemDetailForms.toString());
//        List<ItemDetailForm> list=itemDetailService.itemDetailFormList();
//        System.out.println("list.toString() = " + list.toString());
//        return ResponseMessage.<List<ItemDetailForm>>builder()
//                .message("전체조회")
//                .data(list)
//                .build();
//    }
//
//    @GetMapping("/listOne")
//    public ResponseMessage<ItemDetailForm> itemDetailfindOne(@PathVariable Long id){
//        log.info("id:"+id);
//        ItemDetail itemDetail=itemDetailService.itemDetailFind(id);
//
//        ItemDetailForm itemDetailForm=itemMapper.domainToDto(itemDetail);
//
//        return ResponseMessage.<ItemDetailForm>builder()
//                .message("상품하나조회")
//                .data(itemDetailForm)
//                .build();
//    }

//
//
//    @DeleteMapping("/delete/{id}")
//    public ResponseMessage itemDetailDelete(@PathVariable Long id){
//        boolean result=itemDetailService.itemDetailDelete(id);
//        if(result==true)
//            return ResponseMessage.builder()
//                    .message( "삭제가 완료되었습니다")
//                    .build();
//
//        else
//            return ResponseMessage.builder()
//                    .message("잘못된 요청입니다")
//                    .build();
//    }

}
