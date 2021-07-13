package com.alconn.copang.search;

import com.alconn.copang.annotations.QueryStringBody;
import com.alconn.copang.common.ResponseMessage;
import com.alconn.copang.item.dto.ItemDetailForm;
import com.alconn.copang.item.dto.ItemViewForm;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SearchController {

    private final SearchService service;

//    @Cacheable(value = "item", keyGenerator = "searchKeyGenerator", cacheManager = "cacheManager")
    @GetMapping("/api/item/search")
    public ResponseMessage<ItemViewForm.MainViewForm> searchItem(@QueryStringBody ItemSearchCondition condition) {
        return ResponseMessage.success(
            service.search(condition)
        );
    }

    @GetMapping("/search")
    public ItemSearchCondition s(@QueryStringBody ItemSearchCondition condition) {
        return condition;
    }

}
