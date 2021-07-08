package com.alconn.copang.order;

import static com.alconn.copang.order.QSellerOrder.sellerOrder;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
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
}
