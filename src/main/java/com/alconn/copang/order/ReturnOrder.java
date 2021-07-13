package com.alconn.copang.order;

import com.alconn.copang.address.Address;
import com.alconn.copang.payment.ImpCancelInfo;
import com.alconn.copang.shipment.LogisticCode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Builder
@NoArgsConstructor @AllArgsConstructor
@Entity
public class ReturnOrder {

    @Id @GeneratedValue
    private Long returnOrderId;

    private String trackingNumber;

    private String pickupRequest;

    private String returnReason;

    @CreationTimestamp
    private LocalDateTime receiptDate;

    private LocalDateTime completeDate;

    private Integer returnAmount;

    private LogisticCode logisticCode;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private ReturnStatus returnStatus = ReturnStatus.READY;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    private Integer returnPrice;

    @Builder.Default
    @OneToMany(mappedBy = "returnOrder")
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "imp_cancel_info_id")
    private ImpCancelInfo impCancelInfo;


    public void returningItem(OrderItem item) {
        this.orderItems.add(item);
    }

    public void returningItem(List<OrderItem> items) {
        this.orderItems.addAll(items);
    }

    public void updateTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public void setCancelInfo(ImpCancelInfo cancelInfo) {
        this.impCancelInfo = cancelInfo;
        cancelInfo.setReturnOrder(this);
    }
}