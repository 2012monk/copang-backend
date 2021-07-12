package com.alconn.copang.review;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findReviewsByOrderItem_ItemDetail_Item_ItemIdOrderByRatingDesc(
        @Param(value = "itemId") Long itemId, Sort sort);

    List<Review> findReviewsByWriter_ClientIdOrderByRatingDesc(@Param(value = "clientId") Long clientId);

    List<Review> findReviewsByOrderItem_ItemDetail_Item_Seller_ClientIdOrderByRatingDesc(@Param(value = "sellerId") Long sellerId);
}
