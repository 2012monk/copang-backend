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


    @Query("select o from Orders o join fetch OrderItem ")
    List<Orders> joinTest();

}
