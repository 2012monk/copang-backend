package com.alconn.copang.seller;

import com.alconn.copang.annotations.InjectId;
import com.alconn.copang.client.Role;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Seller {

    @Id @GeneratedValue
    private Long sellerId;

    private String sellerCode;

    private Role role;

}
