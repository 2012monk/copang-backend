package com.alconn.copang.client;

import com.alconn.copang.client.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepo extends JpaRepository<Client, Long> {

    Optional<Client> findClientByUsername(String username);
}
