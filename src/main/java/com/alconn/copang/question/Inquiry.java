package com.alconn.copang.question;

import com.alconn.copang.client.Client;
import com.alconn.copang.item.ItemDetail;
import com.alconn.copang.seller.Seller;
import javax.persistence.CascadeType;
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
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
public class Inquiry {

    @Id @GeneratedValue
    private Long inquiryId;

    private String content;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "item_detail_id")
    private ItemDetail itemDetail;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "reply_id")
    private Reply reply;

    public void reply(Reply reply) {
        this.reply = reply;
    }
}
