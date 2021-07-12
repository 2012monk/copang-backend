package com.alconn.copang.shipment;

import com.alconn.copang.address.Address;
import com.alconn.copang.address.AddressForm;
import com.alconn.copang.client.UserForm;
import com.alconn.copang.order.dto.OrderItemForm;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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

        @NotNull(message = "아이템아이디는 필수입니다")
        private Long orderItemId;

        @NotEmpty(message = "송장번호는 필수입니다")
        private String trackingNumber;

        private LogisticCode logisticCode;

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

        private Long orderItemId;

        private LogisticCode logisticCode;

        private String logisticCompany;
    }



}
