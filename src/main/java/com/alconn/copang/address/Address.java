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
public class Address {

    @Id @GeneratedValue
    private Long addressId;

    private String addressName;

    private String receiverName;

    private String city;

    private String detail;

    private String receiverPhone;

    private String preRequest;

    // TODO 연관관계 강요 Address 완성시 체크
//    @ManyToOne(optional = false)
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    private EntityPriority priority;

    public void setClient(Client client) {
        this.client = client;
    }

}
