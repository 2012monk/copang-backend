package com.alconn.copang.item;

import static com.alconn.copang.item.QItem.item;
import static com.alconn.copang.item.QItemDetail.itemDetail;
import static com.alconn.copang.order.QOrderItem.orderItem;
import static com.alconn.copang.review.QReview.review;

import com.alconn.copang.item.dto.ItemViewForm.MainViewForm;
import com.alconn.copang.item.mapper.ItemMapper;
import com.alconn.copang.search.ItemSearchCondition;
import com.alconn.copang.shipment.ShippingChargeType;
import com.alconn.copang.utils.StringUtils;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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
                eqCategory(condition.getCategoryId()),
                eqLogisticChargeType(condition.getShippingChargeType())
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

    private BooleanExpression eqLogisticChargeType(ShippingChargeType type) {
        return type == null ? null : itemDetail.item.shipmentInfo.shippingChargeType.eq(type);
    }

    public void list() {

    }


    public MainViewForm list(ItemMapper itemMapper) {
        return search(new ItemSearchCondition(), itemMapper);
    }

    public MainViewForm search(ItemSearchCondition condition, ItemMapper itemMapper) {
        BooleanBuilder builder = new BooleanBuilder();
        if (!StringUtils.isEmpty(condition.getKeyword())) {
            String[] keywords = StringUtils.filterAndSplitByEmpty(condition.getKeyword());
            Arrays.stream(keywords).forEach(s -> builder.or(itemDetail.item.itemName.contains(s)));
        }
        builder.and(itemDetail.itemMainApply.eq(ItemMainApply.APPLY));
        List<Tuple> fetch = queryFactory
            .select(itemDetail, review.rating.avg().coalesce(0d), item.itemId.count(),
                review.count())
            .from(item)
            .offset((long) condition.getPage() * condition.getSize())
            .limit(condition.getSize())
            .join(itemDetail).on(item.eq(itemDetail.item))
            .leftJoin(orderItem).on(itemDetail.eq(orderItem.itemDetail))
            .leftJoin(review).on(orderItem.eq(review.orderItem))
            .where(builder)
            .where(
                eqBrand(condition.getBrand()),
                eqPriceOver(condition.getPriceOver()),
                eqPriceUnder(condition.getPriceUnder()),
                afterDate(condition.getStartDate()),
                beforeDate(condition.getEndDate()),
                eqCategory(condition.getCategoryId()),
                eqLogisticChargeType(condition.getShippingChargeType())
            )
            .groupBy(item)
            .orderBy(review.rating.avg().desc())
            .fetchJoin()
            .fetch();
        List<ItemDetail> details = new ArrayList<>();
        for (Tuple t : fetch) {
            ItemDetail detail = t.get(itemDetail);
            if (detail != null) {
//                detail.getItem().setAvg(t.get(review.rating.avg()));
                detail.getItem().setCountReviews(t.get(review.count()));
                detail.getItem().setAvg(t.get(1, Double.class));
                details.add(detail);
            }
        }
        return MainViewForm.builder()
            .list(itemMapper.mainPage(details))
            .totalCount(Objects.requireNonNull(fetch.get(0).get(item.itemId.count())).intValue())
            .build();
    }

}
