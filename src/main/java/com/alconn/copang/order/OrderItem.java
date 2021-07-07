package com.alconn.copang.order;

import com.alconn.copang.item.ItemDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor @AllArgsConstructor
@Getter @Builder
@Entity
public class OrderItem {

    @Id @GeneratedValue
    private Long orderItemId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "item_id")
    private ItemDetail itemDetail;

    @ManyToOne(optional = false)
    @JoinColumn(name = "orders_id")
    private Orders orders;

    @ManyToOne
    @JoinColumn(name = "seller_order_id")
    private SellerOrder sellerOrder;

    private int amount;

    private int unitTotal;

    private int shippingPrice;

    public void setOrders(Orders orders) {
        this.orders = orders;
//        orders.addOrderItem(this);
    }

    public void calculateTotal() {
        this.unitTotal = this.amount * this.itemDetail.getPrice();
    }

    public void setSellerOrder(SellerOrder sellerOrder) {
        this.sellerOrder = sellerOrder;
    }
}
