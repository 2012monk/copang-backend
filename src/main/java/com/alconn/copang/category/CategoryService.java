package com.alconn.copang.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

    final CategoryRepository categoryRepository;

    final CategoryItemRepository categoryItemRepository;

    public void categorySave(){


    }
}
