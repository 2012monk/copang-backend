package com.alconn.copang.inquiry;

import com.alconn.copang.seller.Seller;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
public class Reply {

    @Id @GeneratedValue
    private Long replyId;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;

    private String content;

}
