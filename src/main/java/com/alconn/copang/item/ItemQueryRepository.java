package com.alconn.copang.item;

import static com.alconn.copang.item.QItemDetail.itemDetail;

import com.alconn.copang.utils.StringUtils;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ItemQueryRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * 키워드로 검색 특수문자 소거후 공백문자로 분리해서 모두 검색
     * 공백문자 처리에대해서 논의 필요함
     * @param request 검색어
     * @param page 페이지번호
     * @param size 페이지당 entity
     * @return 옵션상품
     */
    public List<ItemDetail> searchByKeywords(String request, Integer page, Integer size) {
        String[] keywords = StringUtils.filterAndSplitByEmpty(request);

        BooleanBuilder builder = new BooleanBuilder();

//        BooleanExpression
        Arrays.stream(keywords).forEach(s -> builder.or(itemDetail.item.itemName.contains(s)));
        builder.and(itemDetail.itemMainApply.eq(ItemMainApply.APPLY));
        return queryFactory
            .selectFrom(itemDetail)
            .where(builder)
            .offset((long) page * size)
            .limit(size)
            .fetch();
    }

    public List<ItemDetail> filterBy(String brand, Integer price, LocalDate startDate, LocalDate endDate) {
        return queryFactory
            .selectFrom(itemDetail)
            .where(eqBrand(brand))
            .where(itemDetail.item.itemCreate.after(startDate))
            .where(itemDetail.item.itemCreate.before(endDate))
            .fetch();


    }
    public BooleanExpression eqBrand(String brand) {
        if (brand == null) return null;
        return itemDetail.item.itemComment.eq(brand);
    }

    public List<ItemDetail> searchByKeywords(String request) {
        return searchByKeywords(request, 0, 60);
    }

    // Review join 방법찾을필요 있음
//    public List<ItemDetail> getItemWithReview() {
//
//        List<Integer> fetch = queryFactory
//            .select(Projections.fields(Integer.class,
//                itemDetail.item.averageRating.as("averageRating"),
//                ExpressionUtils.as(
//                    JPAExpressions.select(review.rating.avg())
//                        .from(review)
//                        .where(review.orderItem.itemDetail.item.itemId.eq(itemDetail.item.itemId)),
//                    "averageRating")
//            ))
//            .from(itemDetail)
//            .fetch();
//
//        return null;
//
//    }

}
