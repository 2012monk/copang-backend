package com.alconn.copang.wishlist;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WishRepository extends JpaRepository<Wish,Long> {
    List<Wish> findByClient_ClientId(Long clientId);

    Wish findByClient_ClientIdAndWishId(Long clientId,Long wishId);

    List<Wish> findByClient_ClientIdAndWishIdIn(Long clientId, List<Long> wishId);
}
