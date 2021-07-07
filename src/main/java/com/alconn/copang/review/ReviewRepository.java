package com.alconn.copang.review;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findReviewsByOrderItem_ItemDetail_Item_ItemId(
        @Param(value = "itemId") Long itemId, Sort sort);

    List<Review> findReviewsByWriter_ClientId(@Param(value = "clientId") Long clientId);

    List<Review> findReviewsByOrderItem_ItemDetail_Item_Seller_ClientIdOrderByRegisterDate(@Param(value = "sellerId") Long sellerId);
}
