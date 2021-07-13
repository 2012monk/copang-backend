package com.alconn.copang.payment;

import com.alconn.copang.order.Orders;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import javax.persistence.CascadeType;
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

    @OneToOne(mappedBy = "impPaymentInfo", optional = false)
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

    @OneToMany(mappedBy = "impPaymentInfo", cascade = CascadeType.ALL)
    private List<ImpCancelInfo> cancel_history;


    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    public void setPaidAt(Long paid_at) {
        this.paidAt = new Timestamp(paid_at * 1000).toLocalDateTime();
//        this.paidAt = convertTime(paid_at);
    }

    public void setCancelledAt(Long cancelledAt) {
        this.cancelledAt = new Timestamp(cancelledAt * 1000).toLocalDateTime();
//        this.cancelledAt = convertTime(cancelledAt);
    }

    public void setFailedAt(Long failedAt) {
        this.failedAt = new Timestamp(failedAt * 1000).toLocalDateTime();
//        this.failedAt = convertTime(failedAt);
    }

    private LocalDateTime convertTime(Long epoch) {
        return LocalDateTime.ofEpochSecond(epoch, 0,ZoneOffset.of("Asia/Seoul"));
    }
    public void connectCancelOrder() {
        cancel_history.forEach(c -> c.setPay(this));
    }
}
