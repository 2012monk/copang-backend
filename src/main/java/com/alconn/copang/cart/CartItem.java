package com.alconn.copang.cart;

import com.alconn.copang.common.JoinItemBaseEntity;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class CartItem extends JoinItemBaseEntity {

    @Id
    @GeneratedValue
    private Long cartItemId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    private int unitTotal;

    public int calculateTotal() {
        int result = this.item.getPrice() * this.amount;
        return result;
    }

    public void connectToCart(Cart cart) {
        this.cart = cart;
    }

    public void disconnectToCart() {
        cart.deleteItem(this);
        this.cart = null;
    }

    public void subtractAmount(@Positive int amount) {
        if (amount < 0) {
            return;
        }
        if (this.amount >= 0 && this.amount - amount > 0) {
            this.amount -= amount;
        } else {
            disconnectToCart();
        }
    }

    public void subtractAmount() {
        this.subtractAmount(1);
    }

    public void addAmount(@Positive int amount) {
        this.amount += amount;
    }

    public void updateAmount(@Positive int amount) {
        this.amount = amount;
        setUnitTotal();
    }

    public void setUnitTotal() {
        this.unitTotal = this.amount * this.price;
    }
}
