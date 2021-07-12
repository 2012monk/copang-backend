package com.alconn.copang.payment;

import com.alconn.copang.order.ReturnOrder;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor @AllArgsConstructor
@Entity
public class ImpCancelInfo {

    @Id @GeneratedValue
    private Long impCancelInfoId;

    private String pg_tid;

    private Integer amount;

    private String reason;

    private String receipt_url;

    private Long canceled_at;

    @ManyToOne(optional = false)
    @JoinColumn(name = "imp_payment_info_id")
    private ImpPaymentInfo impPaymentInfo;

    @OneToOne(mappedBy = "impCancelInfo")
    private ReturnOrder returnOrder;


    public void setReturnOrder(ReturnOrder order) {
        this.returnOrder = order;
    }
}
