package com.alconn.copang.item;


import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class ItemQueryRepositoryTest {


    @Autowired
    ItemQueryRepository itemQueryRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemDetailRepository itemDetailRepository;

    @Transactional
    @Test
    void searchTest() {

        String[] names = new String[]{
            "감자", "고구마", "빼빼로", "삼양라면", "진라면", "불닭볶음면"
            , "열라면"
        };

        List<Item> items =
            Arrays.stream(names).map(s ->
                Item.builder()
                    .itemName(s)
                    .build()).collect(Collectors.toList());

        List<ItemDetail> detailsToSave =
            items.stream().map(i -> ItemDetail.builder()
                .mainImg("123")
                .item(i)
                .optionValue("rm")
                .optionName("ASdf")
                .price(123)
                .itemMainApply(ItemMainApply.APPLY)
                .stockQuantity(123)
                .build()
            ).collect(Collectors.toList());

        detailsToSave.forEach(d -> d.itemConnect(d.getItem()));

        itemRepository.saveAll(items);
        itemDetailRepository.saveAll(detailsToSave);

        List<ItemDetail> details1 = itemQueryRepository.searchByKeywords("라면+고구마", 0, 40);


        System.out.println("details1.size() = " + details1.size());
        details1.forEach(d -> System.out
            .println("d.getItem().getItemName() = " + d.getItem().getItemName()));


        assertEquals(4, details1.size());

    }
}