package com.alconn.copang.address;

import com.alconn.copang.common.EntityPriority;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findAddressesByClient_ClientId(@Param(value = "clientId") Long clientId);

    Optional<Address> findAddressByClient_ClientIdAndPriority(
        @Param(value = "clientId") Long clientId,@Param(value = "priority") EntityPriority priority);
}
