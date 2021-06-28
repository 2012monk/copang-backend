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


    @PostMapping("/add")
    public ResponseMessage<List<ItemDetailForm>> add( @RequestBody List<ItemDetailForm> itemDetailFormList) {
        List<ItemDetailForm> itemDetailForms=itemDetailService.itemDetailListSave(itemDetailFormList);
        System.out.println("itemDetailForms.size() = " + itemDetailForms.size());
        return ResponseMessage.<List<ItemDetailForm>>builder()
                .message("저장완료")
                .data(itemDetailFormList)
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
