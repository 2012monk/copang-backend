package com.alconn.copang.inquiry;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    List<Inquiry> findInquiriesByItemDetail_Item_ItemId(@Param(value = "itemId") Long itemId);


}
