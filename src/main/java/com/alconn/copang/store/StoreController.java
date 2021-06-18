package com.alconn.copang.store;

import com.alconn.copang.common.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class StoreController {

    private final StoreRepo repo;


    @PostMapping("/stores")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseMessage<Store> createStore(@RequestBody Store store) {
        return ResponseMessage.<Store>builder()
                .data(repo.save(store))
                .message("success")
                .build();
    }

    @GetMapping("/stores/list")
    public ResponseMessage<List<Store>> getAllStores() {
        return ResponseMessage.<List<Store>>builder()
                .data(repo.findAll())
                .message("success")
                .build();
    }

    @GetMapping("/stores/{id}")
    public ResponseMessage<Store> getStoreById(@PathVariable Long id) {
        return ResponseMessage.<Store>builder()
                .data(repo.getById(id))
                .message("success")
                .build();
    }


}
