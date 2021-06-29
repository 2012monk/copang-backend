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
        Address address = saveAddress(form, clientId);

        return mapper.toDto(address);
    }

    private Address saveAddress(AddressForm form, Long clientId) throws ValidationException {
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
        return address;
    }

    /**
     * @param form     등록폼
     * @param clientId 유저 아이디
     * @return AddressForm 주소 반환객체
     * @throws ValidationException    주소 최대갯수 초과시
     * @throws NoSuchEntityExceptions 주소가 정상적으로 저장되지 않아 기본배송지 설정실패
     */
    @Transactional
    public AddressForm registerPrimaryAddress(AddressForm form, Long clientId)
        throws ValidationException, NoSuchEntityExceptions {
        Address address = saveAddress(form, clientId);
        setPrimaryAddress(address.getAddressId(), clientId);

        return mapper.toDto(address);
    }

    @Transactional
    public List<AddressForm> getAllAddresses(Long clientId) {
        List<Address> list = addressRepository.findAddressesByClient_ClientId(clientId);
        return mapper.toDto(list);
    }

    @Transactional
    public AddressForm getPrimaryAddress(Long clientId) throws NoSuchEntityExceptions {
        Address address = addressRepository
            .findAddressByClient_ClientIdAndPriority(clientId, EntityPriority.PRIMARY)
            .orElseThrow(NoSuchEntityExceptions::new);

        return mapper.toDto(address);
    }

    @Transactional
    public boolean setPrimaryAddress(Long addressId, Long clientId) throws NoSuchEntityExceptions {
        Address updateAddress = addressRepository.findById(addressId).orElseThrow(
            NoSuchEntityExceptions::new);

        addressRepository
            .findAddressByClient_ClientIdAndPriority(clientId, EntityPriority.PRIMARY).ifPresent(
            Address::lowerPriority
        );

        updateAddress.setPrimary();
        return true;
    }

    @Transactional
    public AddressForm deleteAddress(Long addressId, Long clientId) throws NoSuchEntityExceptions {
        Address validate = addressRepository.findAddressesByClient_ClientId(clientId).stream()
            .filter(
                i -> i.getAddressId().equals(addressId)
            ).findAny().orElseThrow(NoSuchEntityExceptions::new);
        addressRepository.deleteById(addressId);

        if (validate.getPriority() == EntityPriority.PRIMARY) {
            List<Address> list = addressRepository.findAddressesByClient_ClientId(clientId);
            if (!list.isEmpty()) {
                list.get(0).setPrimary();
            }
        }

        return AddressForm.builder().addressId(addressId).clientId(clientId).build();
    }

    @Transactional
    public AddressForm updateAddress(AddressForm form, Long addressId, Long clientId)
        throws NoSuchEntityExceptions, ValidationException {

        Address address = addressRepository.findById(addressId)
            .orElseThrow(NoSuchEntityExceptions::new);

        if (!address.getClient().getClientId().equals(clientId)) {
            throw new ValidationException("요청하신 리소스의 권한이 없습니다");
        }

        address.update(form.getAddress(),
            form.getDetail(),
            form.getReceiverName(),
            form.getReceiverPhone(),
            form.getPreRequest());

        return mapper.toDto(address);

    }
}

