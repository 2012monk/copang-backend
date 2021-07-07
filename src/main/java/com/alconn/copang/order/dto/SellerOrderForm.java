package com.alconn.copang.order.dto;

import com.alconn.copang.address.AddressForm;
import com.alconn.copang.client.UserForm;
import java.util.List;

public class SellerOrderForm {


    public static class Response{

        private Long sellerOrderId;

        private UserForm.Response client;

        private List<OrderItemForm> orderItems;

        private AddressForm address;



    }

}
