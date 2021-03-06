package com.alconn.copang.item;

import static com.alconn.copang.category.QCategory.category;
import static com.alconn.copang.item.QItem.item;
import static com.alconn.copang.item.QItemDetail.itemDetail;
import static com.alconn.copang.order.QOrderItem.orderItem;
import static com.alconn.copang.review.QReview.review;
import static com.alconn.copang.seller.QSeller.seller;

import com.alconn.copang.category.Category;
import com.alconn.copang.category.QCategory;
import com.alconn.copang.item.dto.ItemViewForm.MainViewForm;
import com.alconn.copang.item.mapper.ItemMapper;
import com.alconn.copang.search.ItemSearchCondition;
import com.alconn.copang.search.OrderCondition;
import com.alconn.copang.shipment.ShippingChargeType;
import com.alconn.copang.utils.StringUtils;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
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
        return price == null ? null : itemDetail.price.goe(price);
    }

    private BooleanExpression eqPriceUnder(Integer price) {
        return price == null ? null : itemDetail.price.loe(price);
    }

    private BooleanExpression afterDate(LocalDate startDate) {
        return startDate == null ? null : itemDetail.item.itemCreate.after(startDate);
    }

    private BooleanExpression beforeDate(LocalDate beforeDate) {
        return beforeDate == null ? null : itemDetail.item.itemCreate.before(beforeDate);
    }

    private BooleanExpression eqCategory(Long categoryId) {
        if (categoryId == null) return null;
        List<Long> ids = getChildCategory(categoryId);
        return item.category.categoryId.in(ids);
//        return categoryId == null ? null : itemDetail.item.category.categoryId.eq(categoryId);
    }

    private List<Long> getChildCategory(Long categoryId) {
        List<Category> fetch = queryFactory
            .selectFrom(category)
            .fetch();

        System.out.println("fetch.size() = " + fetch.size());
        System.out.println("fetch.get(0).getParentId() = " + fetch.get(0).getParentId());
        List<Long> ids = new ArrayList<>();
        ids.add(categoryId);
        for (Category c: fetch) {
            if (ids.contains(c.getParentId())) {
                ids.add(c.getCategoryId());
            }
//            fetch.remove(c);
        }

        return ids;
    }

    private BooleanExpression eqLogisticChargeType(ShippingChargeType type) {
        return type == null ? null : itemDetail.item.shipmentInfo.shippingChargeType.eq(type);
    }

    public void list() {

    }

    public Double getAverageRating(Long itemId) {
        return queryFactory
            .select(review.rating.avg().coalesce(0D))
            .from(review)
            .join(orderItem).on(review.orderItem.eq(orderItem))
            .join(itemDetail).on(orderItem.itemDetail.eq(itemDetail))
            .join(item).on(itemDetail.item.eq(item))
            .where(item.itemId.eq(itemId))
            .fetchFirst();
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
            .select(itemDetail,
                review.rating.avg().coalesce(0d),
                item.itemId.count(),
                review.count(),
                orderItem.count()
            )
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
//            .orderBy(review.rating.avg().desc())
            .orderBy(order(condition.getSorted()))
            .fetchJoin()
            .fetch();
        return getMainViewForm(condition, itemMapper, fetch);
    }

    private MainViewForm getMainViewForm(ItemSearchCondition condition, ItemMapper itemMapper,
        List<Tuple> fetch) {
        List<ItemDetail> details = new ArrayList<>();
        for (Tuple t : fetch) {
            ItemDetail detail = t.get(itemDetail);
            if (detail != null) {
                detail.getItem().setCountOrderItems(t.get(orderItem.count()));
                detail.getItem().setCountReviews(t.get(review.count()));
                detail.getItem().setAvg(t.get(1, Double.class));
                details.add(detail);
            }
        }

        if (condition.getSorted() == OrderCondition.ranking) {
            details.sort(Collections.reverseOrder(Comparator.comparing(
                i -> (i.getItem().getCountReviews() + i.getItem().getCountOrderItems()) * i
                    .getItem().getAverageRating())));
        }
        return MainViewForm.builder()
            .list(itemMapper.mainPage(details))
            .totalCount(fetch.size() == 0 ? 0
                : Objects.requireNonNull(fetch.get(0).get(item.itemId.count())).intValue())
            .build();
    }

    private OrderSpecifier<?> order(OrderCondition condition) {
        if (condition == null) {
            return review.rating.avg().desc();
        }
        switch (condition) {
            case date:
                return item.itemCreate.desc();
            case dateAsc:
                return item.itemCreate.asc();
            case sales:
                return orderItem.count().desc();
            case price:
                return itemDetail.price.desc();
            case priceAsc:
                return itemDetail.price.asc();
            case review:
                return review.count().desc();
            default:
                return review.rating.avg().desc();
        }
    }

    public MainViewForm getSellerItems(Long sellerId, ItemSearchCondition condition,
        ItemMapper itemMapper) {

        List<Tuple> fetch = queryFactory
            .select(itemDetail,
                review.rating.avg().coalesce(0d),
                item.itemId.count(),
                review.count(),
                orderItem.count()
            )
            .from(item)
            .offset((long) condition.getPage() * condition.getSize())
            .limit(condition.getSize())
            .join(itemDetail).on(item.eq(itemDetail.item))
            .join(seller).on(item.seller.eq(seller))
            .leftJoin(orderItem).on(itemDetail.eq(orderItem.itemDetail))
            .leftJoin(review).on(orderItem.eq(review.orderItem))
            .where(
                eqBrand(condition.getBrand()),
                eqPriceOver(condition.getPriceOver()),
                eqPriceUnder(condition.getPriceUnder()),
                afterDate(condition.getStartDate()),
                beforeDate(condition.getEndDate()),
                eqCategory(condition.getCategoryId()),
                eqLogisticChargeType(condition.getShippingChargeType())
            )
            .where(seller.clientId.eq(sellerId))
            .groupBy(item)
//            .orderBy(review.rating.avg().desc())
            .orderBy(order(condition.getSorted()))
            .fetchJoin()
            .fetch();

        return getMainViewForm(condition, itemMapper, fetch);

//        return queryFactory
//            .select(itemDetail)
//            .from(item)
//            .join(itemDetail).on(itemDetail.in(item.itemDetails))
//            .join(seller).on(seller.eq(item.seller))
//            .where(seller.clientId.eq(sellerId))
//            .orderBy(item.itemCreate.desc())
//            .fetch();
//

    }

}
