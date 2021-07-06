package com.alconn.copang.wishlist;

import com.alconn.copang.annotations.InjectId;
import com.alconn.copang.common.ResponseMessage;
import com.alconn.copang.exceptions.NoSuchEntityExceptions;
import com.alconn.copang.wishlist.dto.WishRequest;
import com.alconn.copang.wishlist.dto.WishResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishlist")
public class WishListController {

    private final WishService wishService;

    @PostMapping("/add")
    public ResponseMessage<WishResponse> add(@Valid @RequestBody WishRequest wishRequest, @InjectId Long clientId) throws NoSuchEntityExceptions {
        return ResponseMessage.<WishResponse>builder()
                .message("찜등록 완료")
                .data(wishService.add(wishRequest,clientId))
                .build();
    }

    @GetMapping("/list")
    public ResponseMessage<List<WishResponse>> list(@InjectId Long clientId) throws NoSuchEntityExceptions {
        System.out.println("clientId = " + clientId);
        List<WishResponse> wishResponse=wishService.list(clientId);
        if(wishResponse==null) {
            return ResponseMessage.<List<WishResponse>>builder()
                    .message("등록된 찜목록이 없습니다")
                    .build();
        }
        return ResponseMessage.<List<WishResponse>>builder()
                .message("등록된 찜목록")
                .data(wishResponse)
                .build();
    }

    @DeleteMapping("/del")
    public ResponseMessage<List<WishResponse>> delList(@RequestBody WishRequest.WishRequestdel wishRequestdel, @InjectId Long clientId) throws NoSuchEntityExceptions {
        return ResponseMessage.<List<WishResponse>>builder()
                .message("삭제된 데이터입니다")
                .data(wishService.delList(wishRequestdel, clientId))
                .build();
    }



}
