package com.alconn.copang.item;

import static com.alconn.copang.item.QItemDetail.itemDetail;

import com.alconn.copang.item.dto.ItemDetailForm;
import com.alconn.copang.search.ItemSearchCondition;
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
     * 키워드로 검색 특수문자 소거후 공백문자로 분리해서 모두 검색 공백문자는 + 기호로 치환해서 받음
     *
     * @param request 검색어
     * @param page    페이지번호
     * @param size    페이지당 entity
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

    public List<ItemDetail> filterBy(String brand, Integer price, LocalDate startDate,
        LocalDate endDate) {
        return queryFactory
                .selectFrom(itemDetail)
            .where(eqBrand(brand))
            .where(itemDetail.item.itemCreate.after(startDate))
            .where(itemDetail.item.itemCreate.before(endDate))
            .fetch();
    }

    public List<ItemDetail> searchByKeywords(String request) {
        return searchByKeywords(request, 0, 60);
    }

    public List<ItemDetail> searchAndFilter(ItemSearchCondition condition) {
        BooleanBuilder builder = new BooleanBuilder();
        if (!StringUtils.isEmpty(condition.getKeyword())) {
            String[] keywords = StringUtils.filterAndSplitByEmpty(condition.getKeyword());
            Arrays.stream(keywords).forEach(s -> builder.or(itemDetail.item.itemName.contains(s)));
        }
        builder.and(itemDetail.itemMainApply.eq(ItemMainApply.APPLY));
        return queryFactory
            .selectFrom(itemDetail)
            .where(builder)
            .where(
                eqBrand(condition.getBrand()),
                eqPriceOver(condition.getPriceOver()),
                eqPriceUnder(condition.getPriceUnder()),
                afterDate(condition.getStartDate()),
                beforeDate(condition.getEndDate()),
                eqCategory(condition.getCategoryId())
            )
            .offset((long) condition.getPage() * condition.getSize())
            .limit(condition.getSize())
            .fetch();
    }

    private BooleanExpression eqBrand(String brand) {
        if (StringUtils.isEmpty(brand)) {
            return null;
        }
        return itemDetail.item.brand.eq(brand);
    }


    private BooleanExpression eqPriceOver(Integer price) {
        return price == null ? null : itemDetail.price.gt(price);
    }

    private BooleanExpression eqPriceUnder(Integer price) {
        return price == null ? null : itemDetail.price.lt(price);
    }

    private BooleanExpression afterDate(LocalDate startDate) {
        return startDate == null ? null : itemDetail.item.itemCreate.after(startDate);
    }

    private BooleanExpression beforeDate(LocalDate beforeDate) {
        return beforeDate == null ? null : itemDetail.item.itemCreate.before(beforeDate);
    }

    private BooleanExpression eqCategory(Long categoryId) {
        return categoryId == null ? null : itemDetail.item.category.categoryId.eq(categoryId);
    }


    // Review join 방법찾을필요 있음
    public List<ItemDetailForm.MainForm> getItemWithReview() {

//        return queryFactory
//            .select(Projections.fields(ItemDetail.class,
//                itemDetail.item,
//                ExpressionUtils.as(
//                    JPAExpressions.select(review.rating.avg()),
//
//                    "averageRating")))
//            .from(itemDetail)
//            .join(itemDetail.item)
////            .fetchJoin()
////            .leftJoin(review).on(itemDetail.eq(review.orderItem.itemDetail))
//            .join(orderItem).on(itemDetail.eq(orderItem.itemDetail))
//            .join(review).on(orderItem.itemDetail.eq(itemDetail))
//            .fetch();
        return null;

    }

}
