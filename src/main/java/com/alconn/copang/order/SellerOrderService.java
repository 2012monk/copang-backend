package com.alconn.copang.order;

import com.alconn.copang.exceptions.NoSuchEntityExceptions;
import com.alconn.copang.order.dto.SellerOrderForm;
import com.alconn.copang.order.mapper.SellerOrderMapper;
import com.alconn.copang.seller.Seller;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SellerOrderService {

    private final SellerOrderRepository sellerOrderRepository;

    private final SellerOrderMapper sellerOrderMapper;

    @Transactional
    public boolean placeSellerOrder(Orders orders) {
            List<OrderItem> orderItems = orders.getOrderItemList();
            Map<Seller, List<OrderItem>> selectBySeller =
                orderItems.stream().collect(Collectors.groupingBy(c -> c.getItemDetail().getItem().getSeller()));

            List<SellerOrder> sellerOrders =
                selectBySeller.entrySet().stream().map(c ->
                    SellerOrder.builder()
                        .seller(c.getKey())
                        .orderItems(c.getValue())
                        .build()
                ).collect(Collectors.toList());

            sellerOrders.forEach(SellerOrder::placeSellerOrder);

            sellerOrderRepository.saveAll(sellerOrders);
            return true;
    }

    public List<SellerOrderForm.Response> getOrdersBySeller(Long sellerId) {
        List<SellerOrder> sellerOrders = sellerOrderRepository.findSellerOrdersBySeller_ClientId(sellerId);
        sellerOrders.forEach(SellerOrder::calculateTotal);

        return sellerOrders.stream()
            .map(s -> sellerOrderMapper.mtoForm(s, s.getOrderItems().get(0).getOrders())).collect(Collectors.toList());
    }

    public List<SellerOrder> getSellers(Long sellerId) {
        return sellerOrderRepository.findSellerOrdersBySeller_ClientId(sellerId);
    }
}
