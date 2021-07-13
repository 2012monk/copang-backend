package com.alconn.copang.order.dto;

import com.alconn.copang.address.Address;
import com.alconn.copang.address.AddressForm;
import com.alconn.copang.order.ReturnStatus;
import com.alconn.copang.shipment.LogisticCode;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ReturnOrderForm {

    @NoArgsConstructor @AllArgsConstructor
    @Builder
    @Getter
    public static class Request {

        private String returnReason;

        private Long addressId;

        private String pickupRequest;

        private Integer amount;

        private LogisticCode logisticCode;

    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class AcceptRequest {

        private Long returnOrderId;

        private Long orderItemId;

        private String trackingNumber;

    }

    @JsonInclude(Include.NON_NULL)
    @NoArgsConstructor @AllArgsConstructor
    @Builder
    @Getter
    public static class Response {

        private Long returnOrderId;

        private Integer orderTotalPrice;

        private Integer returnAmount;

        private Integer returnPrice;

        private String pickupRequest;

        private ReturnStatus returnStatus;

        private LogisticCode logisticCode;

        private String trackingNumber;

        private AddressForm address;

        private String cancelReceiptUrl;

        private List<OrderItemForm> returnItems;

        private String returnReason;

        @JsonFormat(timezone = "Seoul/Asia", pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime receiptDate;

        @JsonFormat(timezone = "Seoul/Asia", pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime canceledAt;
    }
}
