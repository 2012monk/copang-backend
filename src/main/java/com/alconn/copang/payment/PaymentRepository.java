package com.alconn.copang.payment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<ImpPaymentInfo, Long> {

}
