package com.alconn.copang.address;

import com.alconn.copang.common.EntityPriority;
import com.alconn.copang.exceptions.NoSuchEntityExceptions;
import com.alconn.copang.exceptions.ValidationException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AddressService {

    private final AddressRepository addressRepository;

    private final AddressMapper mapper;

    // 최댓값 5
    @Transactional
    public AddressForm registerAddress(AddressForm form, Long clientId) throws ValidationException {
        List<Address> list = addressRepository.findAddressesByClient_ClientId(clientId);

        if (list.size() > 4) {
            throw new ValidationException("주소는 최대 5개 까지 등록할수 있습니다");
        }
        form.setClientId(clientId);
        Address address = mapper.toEntity(form);
        if (list.isEmpty()) {
            address.setPrimary();
        }

        addressRepository.save(address);

        AddressForm res = mapper.toDto(address);
        return res;
    }

    @Transactional
    public List<AddressForm> getAllAddresses(Long clientId) {
        List<Address> list = addressRepository.findAddressesByClient_ClientId(clientId);
        List<AddressForm> res = mapper.toDto(list);
        return res;
    }

    @Transactional
    public AddressForm getPrimaryAddress(Long clientId) {
        Address address = addressRepository
            .findAddressByClient_ClientIdAndPriority(clientId, EntityPriority.PRIMARY);

        return mapper.toDto(address);
    }

    @Transactional
    public boolean setPrimaryAddress(Long addressId, Long clientId) throws NoSuchEntityExceptions {
        Address updateAddress = addressRepository.findById(addressId).orElseThrow(
            NoSuchEntityExceptions::new);

        Address address = addressRepository
            .findAddressByClient_ClientIdAndPriority(clientId, EntityPriority.PRIMARY);

        address.lowerPriority();
        updateAddress.setPrimary();
        return true;
    }

    @Transactional
    public boolean deleteAddress(Long addressId, Long clientId) throws NoSuchEntityExceptions {
        Address validate = addressRepository.findAddressesByClient_ClientId(clientId).stream().filter(
            i -> i.getAddressId().equals(addressId)
        ).findAny().orElseThrow(NoSuchEntityExceptions::new);

        addressRepository.deleteById(addressId);

        return true;
    }

    @Transactional
    public AddressForm updateAddress(AddressForm form, Long addressId)
        throws NoSuchEntityExceptions {
        Address address = addressRepository.findById(addressId).orElseThrow(NoSuchEntityExceptions::new);

//        address.updateAddress(form.getAddress(), form.getDetail());
//
//        address.updateReceiver(form.getReceiverName(), form.getReceiverPhone());

        address.update(form.getAddress(),
            form.getDetail(),
            form.getReceiverName(),
            form.getReceiverPhone(),
            form.getPreRequest());

        return mapper.toDto(address);

    }
}

