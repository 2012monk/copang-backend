package com.alconn.copang.address;

import com.alconn.copang.annotations.InjectId;
import com.alconn.copang.common.ResponseMessage;
import com.alconn.copang.exceptions.NoSuchEntityExceptions;
import com.alconn.copang.exceptions.ValidationException;
import com.alconn.copang.order.dto.OrderForm.Response;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/address")
public class AddressController {

    private final AddressService service;


    @GetMapping
    public ResponseMessage<List<AddressForm>> listAddress(@InjectId Long clientId) {
        return ResponseMessage.success(
            service.getAllAddresses(clientId)
        );
    }

    @PostMapping
    public ResponseMessage<AddressForm> registerAddress(@InjectId Long clientId, @RequestBody AddressForm form)
        throws ValidationException {
        return ResponseMessage.success(
            service.registerAddress(form, clientId)
        );
    }


    @PutMapping("/{addressId}")
    public ResponseMessage<AddressForm> updateAddress(@PathVariable(name = "addressId") Long addressId, @RequestBody AddressForm form)
        throws ValidationException, NoSuchEntityExceptions {
        return  ResponseMessage.success(
            service.updateAddress(form, addressId)
        );
    }

    @PatchMapping("/{addressId}")
    public ResponseMessage<String> setDefaultAddress(@PathVariable Long addressId, @InjectId Long clientId)
        throws NoSuchEntityExceptions {
        return ResponseMessage.successMessage(
            service.setPrimaryAddress(addressId, clientId)
        );
    }




}
