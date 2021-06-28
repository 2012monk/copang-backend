package com.alconn.copang.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findOrderItemsByOrders_OrderId(@Param(value = "orderId") Long orderId);
}
