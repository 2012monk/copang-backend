package com.alconn.copang.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PagingFilterService {
    //페이지 출력만 담당함

    private final ItemRepository itemRepository;

    private final ItemDetailRepository itemDetailRepository;


    public void list(){




    }
}
