package com.alconn.copang.item;

import com.alconn.copang.category.Category;
import com.alconn.copang.item.rating.ItemRank;
import com.alconn.copang.seller.Seller;
import com.alconn.copang.shipment.ShipmentInfo;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item {

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;

    @CreationTimestamp
    @Column(name = "item_time", updatable = false)
    private LocalDate itemCreate;

    @OneToMany(mappedBy = "item", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Builder.Default
    private List<ItemDetail> itemDetails = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn
    private ItemRank itemRank;

    @Transient
    private Double averageRating;

    @Transient
    private Long countReviews;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "shipment_info_id")
    private ShipmentInfo shipmentInfo;

    @Id
    @GeneratedValue
    private Long itemId;

    @Column(nullable = false)
    private String itemName;

    @Lob
    private String itemComment;

    private String brand;

    @Transient
    private Long countOrderItems;


    //=====
    public void changeCategory(Category category) {
        this.category = category;
        category.getItemList().add(this);
    }

    //수정
    public void updateMethod(String itemName, String itemComment, String brand) {
        this.itemName = (itemName == null ? this.itemName : itemName);
        this.itemComment = (itemComment == null ? this.itemComment : itemComment);
        this.brand = (itemComment == null ? this.brand : brand);

    }


    public void setAvg(Double aDouble) {
        this.averageRating = aDouble;
    }

    public void setCountReviews(Long aLong) {
        this.countReviews = aLong;
    }

    public void setCountOrderItems(Long aLong) {
        this.countOrderItems = aLong;
    }
}
