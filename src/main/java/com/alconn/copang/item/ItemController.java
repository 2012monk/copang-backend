package com.alconn.copang.item;

import com.alconn.copang.client.Client;
import com.alconn.copang.common.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/item")
public class ItemController {

    private final ItemDetailService itemDetailService;

    @GetMapping("/list")
    public ResponseMessage<List> itemDetailList(){

        List<ItemDetail> itemDetails=itemDetailService.listItemDetailsALLFind();
//        itemDetails.forEach(System.out::println);
        return  ResponseMessage.<List>builder()
                .data(itemDetails)
                .message("itemList")
                .build();
    }

    @DeleteMapping("litem/delete/{id}")
    public String itemDetailDelete(@PathVariable Long id){


        return "delete";
    }
}
