package com.alconn.copang.search;

import com.alconn.copang.annotations.QueryStringBody;
import com.alconn.copang.common.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SearchController {

    private final SearchService service;

    @GetMapping("/api/item/search")
    public ResponseMessage<?> searchItem(@QueryStringBody ItemSearchCondition condition) {
        return ResponseMessage.success(
            service.search(condition)
        );
    }

    @GetMapping("/search")
    public ItemSearchCondition s(@QueryStringBody ItemSearchCondition condition) {
        return condition;
    }

}
