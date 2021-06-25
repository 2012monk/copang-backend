package com.alconn.copang.order.mapper;

import com.alconn.copang.order.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findOrdersByClient_Id(@Param(value = "clientId") Long clientId);
}
