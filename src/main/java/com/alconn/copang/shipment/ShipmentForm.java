package com.alconn.copang.shipment;

import com.alconn.copang.address.Address;
import com.alconn.copang.address.AddressForm;
import com.alconn.copang.client.UserForm;
import com.alconn.copang.order.dto.OrderItemForm;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ShipmentForm {

    @JsonInclude(Include.NON_NULL)
    @NoArgsConstructor @AllArgsConstructor
    @Builder
    @Getter
    public static class Request {

        private List<Long> shippingItems;

    }

    @JsonInclude(Include.NON_NULL)
    @NoArgsConstructor @AllArgsConstructor
    @Builder
    @Getter
    public static class Response {

        private Long shipmentId;

        private AddressForm address;

        private List<OrderItemForm> orderItems;

        private String trackingNumber;

        private LogisticCode logisticCode;

        private String logisticCompany;
    }



}
