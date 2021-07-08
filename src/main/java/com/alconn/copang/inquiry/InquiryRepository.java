package com.alconn.copang.inquiry;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    List<Inquiry> findInquiriesByItemDetail_Item_ItemId(@Param(value = "itemId") Long itemId);

//    List<Inquiry> findInquiriesByItemDetail_Item_Seller_ClientIdOrderByTime(@Param(value = "clientId") Long sellerId)

    List<Inquiry> findInquiriesByClient_ClientId(@Param(value = "clientID") Long clientId);

    List<Inquiry> findInquiriesByItemDetail_Item_Seller_ClientId(@Param(value = "clientId") Long clientId);
}
