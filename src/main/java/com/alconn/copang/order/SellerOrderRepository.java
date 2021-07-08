package com.alconn.copang.order;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface SellerOrderRepository extends JpaRepository<SellerOrder, Long>{

    List<SellerOrder> findSellerOrdersBySeller_ClientId(@Param(value = "clientId") Long clientId);

}
