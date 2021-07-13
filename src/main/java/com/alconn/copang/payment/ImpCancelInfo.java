package com.alconn.copang.payment;

import com.alconn.copang.order.ReturnOrder;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ImpCancelInfo {

    @Id
    @GeneratedValue
    private Long impCancelInfoId;

    private String pg_tid;

    private Integer amount;

    private String reason;

    private String receipt_url;

    @Transient
    private Long canceled_at;

    private LocalDateTime canceledAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "imp_payment_info_id")
    private ImpPaymentInfo impPaymentInfo;

    @OneToOne(mappedBy = "impCancelInfo")
    private ReturnOrder returnOrder;

    public void convertLocalDate() {
        if (canceled_at != null) {
//            this.canceledAt = LocalDateTime
//                .ofEpochSecond(canceled_at, 0, ZoneOffset.of(ZoneOffset.systemDefault()
//                    .getId()));
            this.canceledAt = new Timestamp(canceled_at * 1000).toLocalDateTime();
        }
    }

    public void setReturnOrder(ReturnOrder order) {
        this.returnOrder = order;
    }

    public void setPay(ImpPaymentInfo impPaymentInfo) {
        this.impPaymentInfo = impPaymentInfo;
    }
}
