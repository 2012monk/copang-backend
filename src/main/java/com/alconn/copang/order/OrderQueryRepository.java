package com.alconn.copang.order;

import static com.alconn.copang.client.QClient.client;
import static com.alconn.copang.order.QOrderItem.orderItem;
import static com.alconn.copang.order.QOrders.orders;
import static com.alconn.copang.order.QReturnOrder.returnOrder;
import static com.alconn.copang.order.QSellerOrder.sellerOrder;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    private final Pattern SP_CHAR =
        Pattern.compile("[!@#$%^&*()-_=+:;\"'{}\\[\\]<>,./?₩~`]");


    public List<SellerOrder> getOrdersByTime(LocalDateTime startDateTime,
        LocalDateTime endDateTime, Long sellerId) {
        return jpaQueryFactory
            .selectFrom(sellerOrder)
            .where(sellerOrder.seller.clientId.eq(sellerId))
            .where(sellerOrder.orderItems.any().orders.orderDate.after(startDateTime))
            .where(sellerOrder.orderItems.any().orders.orderDate.before(endDateTime))
            .fetch();
    }

    public List<SellerOrder> getOrderBySellerWith(Long sellerId) {
        return jpaQueryFactory
            .selectFrom(sellerOrder)
            .offset(0)
            .limit(1)
//            .where(sellerOrder.orderItems.any().amount.eq(1))
            .where(sellerOrder.seller.clientId.eq(sellerId))
            .fetch();
    }

    public List<SellerOrder> searchOrderByKeyWord(Long sellerId, String keyWords) {
        String filterd = filterSpChar(keyWords);
        System.out.println("filterd = " + filterd);
        String[] breakKeys = filterd.split(" ");
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(sellerOrder.seller.clientId.eq(sellerId));
//        for (String s:breakKeys){
//            builder.and(orderItem.itemDetail.item.itemName.contains(s));
//        }

        return jpaQueryFactory
            .selectFrom(sellerOrder)
//            .from(orderItem.sellerOrder)
//            .join(orderItem.sellerOrder)
            .where(builder)
//            .fetchJoin()
//            .join(sellerOrder.orderItems)
            .fetch();
    }

    private String filterSpChar(String keyWords) {
        Matcher matcher = SP_CHAR.matcher(keyWords);
        return matcher.replaceAll("");
    }

    public List<OrderItem> findByIds(List<Long> ids) {
        return jpaQueryFactory
            .selectFrom(QOrderItem.orderItem)
            .where(QOrderItem.orderItem.orderItemId.in(ids))
            .fetch();

    }

    public List<ReturnOrder> findByIds(Set<Long> list) {
        return jpaQueryFactory
            .selectFrom(QReturnOrder.returnOrder)
            .where(QReturnOrder.returnOrder.returnOrderId.in(list))
            .fetch();
    }

    public List<ReturnOrder> getCanceledOrders(Long clientId) {
        return jpaQueryFactory
            .selectFrom(returnOrder)
            .join(orderItem).on(orderItem.in(returnOrder.orderItems))
            .join(orders).on(orderItem.in(orders.orderItemList))
            .join(client).on(orders.client.eq(client))
            .where(client.clientId.eq(clientId))
            .orderBy(returnOrder.receiptDate.desc())
            .fetch();
    }
}
