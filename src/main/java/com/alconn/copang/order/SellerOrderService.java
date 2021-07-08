package com.alconn.copang.order;

import com.alconn.copang.exceptions.NoSuchEntityExceptions;
import com.alconn.copang.order.dto.SellerOrderForm;
import com.alconn.copang.order.dto.SellerOrderForm.Response;
import com.alconn.copang.order.mapper.SellerOrderMapper;
import com.alconn.copang.seller.Seller;
import com.alconn.copang.shipment.Shipment;
import com.alconn.copang.shipment.ShipmentForm;
import com.alconn.copang.shipment.ShipmentForm.Request;
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

    private final OrderQueryRepository orderQueryRepository;

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

    @Transactional
    public String placeShipment(Long sellerOrderId, Request form)
        throws NoSuchEntityExceptions {
        SellerOrder order = sellerOrderRepository.findById(sellerOrderId).orElseThrow(NoSuchEntityExceptions::new);

        Shipment shipment = sellerOrderMapper.toShipment(form);
        List<Long> shippingItems = form.getShippingItems();
        List<OrderItem> items =
            order.getOrderItems().stream().filter(o -> shippingItems.contains(o.getOrderItemId())).map(
                i ->{
                    OrderItem orderItem = OrderItem.builder().orderItemId(i.getOrderItemId()).build();
                    orderItem.setShipment(shipment);
                    return orderItem;
                }
            ).collect(Collectors.toList());

        sellerOrderRepository.save(order);
        return "success";
    }

    public List<Response> getSellerOrder(Long clientId) {
        return orderQueryRepository.getOrderBySellerWith(clientId).stream().map(
            o -> sellerOrderMapper.mtoForm(o, o.getOrderItems().get(0).getOrders())
        ).collect(Collectors.toList());
    }

    public List<Response> searchByKeyWords(Long clientId, String abb_df) {
        return orderQueryRepository.searchOrderByKeyWord(clientId, abb_df).stream().map(
            o -> sellerOrderMapper.mtoForm(o, o.getOrderItems().get(0).getOrders())
        ).collect(Collectors.toList());
    }
}
