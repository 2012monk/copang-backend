package com.alconn.copang.review;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findReviewsByOrderItem_ItemDetail_ItemDetailId(
        @Param(value = "itemDetailId") Long itemDetailId, Sort sort);

}
