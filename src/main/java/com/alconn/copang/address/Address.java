package com.alconn.copang.address;

import com.alconn.copang.client.Client;
import com.alconn.copang.common.EntityPriority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@NoArgsConstructor @AllArgsConstructor
@Getter
@Entity
//@Table(name = "anont")
public class Address {

    @Id @GeneratedValue
    private Long addressId;

    private String addressName;

    private String receiverName;

    private String address;

    private String detail;

    private String receiverPhone;

    @Builder.Default
    private String preRequest = "";

    // TODO 연관관계 강요 Address 완성시 체크
//    @ManyToOne(optional = false)

//    @ManyToOne(fetch = FetchType.LAZY)
    @ManyToOne
    @JoinColumn(name = "client_id", updatable = false)
    private Client client;

    @Enumerated(EnumType.STRING)
    private EntityPriority priority;

    @Builder.Default
    private boolean defaultAddres = false;

    public void setClient(Client client) {
        this.client = client;
    }

    public void lowerPriority() {
        this.priority = EntityPriority.SECONDARY;
    }

    public void setPrimary() {
        this.priority = EntityPriority.PRIMARY;
    }


    public void updateAddress(String address, String detail) {
        this.address = address != null ? address : this.addressName;
        this.detail = detail != null ? detail : this.detail;
    }

    public void updateReceiver(String receiverName, String receiverPhone) {

    }

    public void update(String address, String detail, String receiverName, String receiverPhone,
        String preRequest) {
        this.address = address != null ? address : this.addressName;
        this.detail = detail != null ? detail : this.detail;
        this.receiverName = receiverName != null ? receiverName : this.receiverName;
        this.receiverPhone = receiverPhone != null ? receiverPhone : this.receiverPhone;
        this.preRequest = preRequest != null ? preRequest : this.preRequest;
    }
}
