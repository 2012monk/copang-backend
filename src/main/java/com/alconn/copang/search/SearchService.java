package com.alconn.copang.search;

import com.alconn.copang.item.ItemDetail;
import com.alconn.copang.item.ItemQueryRepository;
import com.alconn.copang.item.dto.ItemDetailForm;
import com.alconn.copang.item.dto.ItemDetailForm.MainForm;
import com.alconn.copang.item.dto.ItemForm;
import com.alconn.copang.item.mapper.ItemMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class SearchService {

    private final ItemQueryRepository itemQueryRepository;

    private final ItemMapper itemMapper;

    public List<ItemForm.ItemSingle> searchByKeyword(String keyword) {
        return null;
    }

    public List<MainForm> search(ItemSearchCondition condition) {

        List<ItemDetail> details = itemQueryRepository.searchAndFilter(condition);
        return itemMapper.mainPage(details);
    }
}
