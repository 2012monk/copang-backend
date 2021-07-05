package com.alconn.copang.wishlist;

import com.alconn.copang.client.Client;
import com.alconn.copang.item.ItemDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Wish {

    @Id @GeneratedValue
    private Long wishId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_detail_id")
    private ItemDetail itemDetail;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    public void itemDetailConnect(ItemDetail itemDetail){
        this.itemDetail=itemDetail;
    }

    public void clientConnect(Client client){
        this.client=client;
    }

    @Override
    public String toString() {
        return "Wish{" +
                "wishId=" + wishId +
                ", itemDetail=" + itemDetail +
                ", client=" + client +
                '}';
    }
}
