package com.alconn.copang.payment;

import com.alconn.copang.order.Orders;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor @AllArgsConstructor

@Builder
@Getter
@Entity
public class ImpPaymentInfo {

    @Id @GeneratedValue
    private Long impPaymentId;

    @OneToOne(mappedBy = "impPaymentInfo")
//    @JoinColumn(name = "order_id")
    private Orders orders;

    private String imp_uid;

    private Integer amount;

    private String pay_method;

    private String pg_provider;

    private String pg_tid;

    private String pg_id;

    private Boolean escrow;

    private LocalDateTime paidAt;

    private LocalDateTime cancelledAt;

    private LocalDateTime failedAt;

    @OneToMany(mappedBy = "impPaymentInfo")
    private List<ImpCancelInfo> cancel_history;


    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    public void setPaidAt(Integer paid_at) {
        this.paidAt = new Timestamp(paid_at).toLocalDateTime();
    }

    public void setCancelledAt(Integer cancelledAt) {
        this.cancelledAt = new Timestamp(cancelledAt).toLocalDateTime();
    }

    public void setFailedAt(Integer failedAt) {
        this.failedAt = new Timestamp(failedAt).toLocalDateTime();
    }
}
