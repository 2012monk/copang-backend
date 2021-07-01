package com.alconn.copang.seller;

import com.alconn.copang.annotations.InjectId;
import com.alconn.copang.client.Client;
import com.alconn.copang.client.Role;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor @AllArgsConstructor
@SuperBuilder
@Entity
public class Seller extends Client {

    @GeneratedValue
    @Column(unique = true)
    private Long sellerCode;

}
