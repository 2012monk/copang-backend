package com.alconn.copang.order;

import com.alconn.copang.client.Client;
import com.alconn.copang.order.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import javax.swing.event.CaretListener;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {

    List<Orders> findOrdersByClient_ClientId(@Param(value = "clientId") Long clientId);

}
