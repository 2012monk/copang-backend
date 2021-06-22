package com.alconn.copang.client;

import com.alconn.copang.common.AccessTokenContainer;
import com.alconn.copang.common.LoginToken;
import com.alconn.copang.exceptions.InvalidTokenException;
import com.alconn.copang.exceptions.LoginFailedException;
import com.alconn.copang.exceptions.NoSuchUserException;
import com.alconn.copang.security.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ClientService {

    private final ClientRepo repo;

    private final JwtTokenProvider provider;

    public AccessTokenContainer login(LoginToken token) throws LoginFailedException, InvalidTokenException {
        Client client = repo.findClientByUsername(token.getUsername()).orElseThrow(LoginFailedException::new);
        if (client.getPassword().equals(token.getPassword())) {
            String accessToken = provider.createAccessToken(client).orElseThrow(InvalidTokenException::new);
            return getAccessTokenContainer(client);
        } else {
            throw new LoginFailedException("password unmatched");
        }
    }

    public Client signupClient(Client client) {
        // TODO // validate login *//

        return repo.save(client);

    }

    public Client getClient(Long id) throws NoSuchUserException {
        return repo.findById(id).orElseThrow(NoSuchUserException::new);
    }

    private AccessTokenContainer getAccessTokenContainer(Client client) {

        String username = client.getUsername();
        Role role = client.getRole();
        String token = provider.createAccessToken(client).orElse("");
        return AccessTokenContainer.builder()
                .access_token(token)
                .username(username)
                .role(role)
                .build();
    }

    public AccessTokenContainer getAccessTokenFromRefreshToken(String token) throws InvalidTokenException, NoSuchUserException {
        Long id = provider.getUserIdFromRefreshToken(token).orElseThrow(InvalidTokenException::new);

        Client client = repo.findById(id).orElseThrow(NoSuchUserException::new);

        return getAccessTokenContainer(client);
    }

    public List<Client> getAllClients() {
        return repo.findAll();
    }
}
