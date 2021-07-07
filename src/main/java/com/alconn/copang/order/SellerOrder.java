package com.alconn.copang.order;

import com.alconn.copang.seller.Seller;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor @AllArgsConstructor
@Builder
@Getter
@Entity
public class SellerOrder {

    @Id
    @GeneratedValue
    private Long sellerOrderId;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;

    @Builder.Default
    @OneToMany(mappedBy = "sellerOrder")
    private List<OrderItem> orderItems = new ArrayList<>();

    private int totalAmount;

    private int totalPrice;

    public void placeSellerOrder() {
        orderItems.forEach(o -> o.setSellerOrder(this));
    }



}
