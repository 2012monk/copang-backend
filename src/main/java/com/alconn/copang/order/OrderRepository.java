package com.alconn.copang.order;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {

    List<Orders> findOrdersByClient_ClientId(@Param(value = "clientId") Long clientId);

//    @Query("select o from Orders o left outer join fetch o.orderItemList")
//    List<Orders> findSellerOrders(@Param(value = "sellerId") Long sellerId);


    @Query("select i from OrderItem i where i.itemDetail.item.seller.clientId=:sellerId group by i.orders")
    List<OrderItem> joinTest(@Param(value = "sellerId") Long sellerId);

}
