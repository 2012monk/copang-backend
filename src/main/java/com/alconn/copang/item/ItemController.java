package com.alconn.copang.item;

import com.alconn.copang.aop.GlobalExceptionHandler;
import com.alconn.copang.common.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/item")
public class ItemController {

    private final ItemDetailService itemDetailService;

    private final ItemMapper itemMapper;

    private final GlobalExceptionHandler globalExceptionHandler;

    @GetMapping("/list")
    public ResponseMessage<List<ItemDetailForm>> itemDetailList(){

        List<ItemDetail> itemDetails=itemDetailService.listItemDetailsALLFind();

        List<ItemDetailForm> itemDetailForms= itemMapper.listDomainToDto(itemDetails);

        return ResponseMessage.<List<ItemDetailForm>>builder()
                .message("전체조회")
                .data(itemDetailForms)
                .build();
    }

    @GetMapping("/listOne")
    public ResponseMessage<ItemDetailForm> itemDetailfindOne(@PathVariable Long id){
        ItemDetail itemDetail=itemDetailService.itemDetailFind(id);

        ItemDetailForm itemDetailForm=itemMapper.domainToDto(itemDetail);

        return ResponseMessage.<ItemDetailForm>builder()
                .message("상품하나조회")
                .data(itemDetailForm)
                .build();
    }

    @PostMapping("/add")
    public ResponseMessage<List<ItemDetailForm>> add(@Valid @RequestBody List<ItemDetailForm> itemDetailFormList) {
        List<ItemDetail> itemDetailList =itemMapper.listDtoToDomain(itemDetailFormList);
        itemDetailList=itemDetailService.itemDetailSaveList(itemDetailList);

        List<ItemDetailForm> itemDetailForms=itemMapper.listDomainToDto(itemDetailList);

        return ResponseMessage.<List<ItemDetailForm>>builder()
                .message("상품저장")
                .data(itemDetailForms)
                .build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseMessage itemDetailDelete(@PathVariable Long id){
        boolean result=itemDetailService.itemDetailDelete(id);
        if(result==true)
            return ResponseMessage.builder()
                    .message( "삭제가 완료되었습니다")
                    .build();

        else
            return ResponseMessage.builder()
                    .message("잘못된 요청입니다")
                    .build();
    }


//    @PutMapping("/update/{id}")
//    public ResponseMessage<List<ItemDetailForm>> itemDetailUpdate(@RequestBody List<ItemDetailForm> itemDetailFormListList){
//        List<ItemDetail> itemDetailList=itemMapper.listDtoToDomain(itemDetailFormListList);
////
////        for(ItemDetail itemDetail : itemDetailList){
////            itemDetailService.itemDetailUpdate()
////        }
////
//        return  ResponseMessage<List<ItemDetailForm>>
//
//    }
}
