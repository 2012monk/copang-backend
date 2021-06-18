package com.alconn.copang.client;

import com.alconn.copang.common.AccessTokenContainer;
import com.alconn.copang.common.LoginToken;
import com.alconn.copang.exceptions.InvalidTokenException;
import com.alconn.copang.exceptions.LoginFailedException;
import com.alconn.copang.security.privider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ClientService {

    private final ClientRepo repo;

    private final JwtTokenProvider provider;

    public AccessTokenContainer login(LoginToken token) throws LoginFailedException, InvalidTokenException {
        Client client = repo.findClientByUsername(token.getUsername()).orElseThrow(LoginFailedException::new);
        if (client.getPassword().equals(token.getPassword())) {
            String accessToken = provider.createAccessToken(client).orElseThrow(InvalidTokenException::new);
            return AccessTokenContainer.builder()
                .access_token(accessToken)
                .username(client.getUsername())
                .role(client.getRole())
                .build();
        }else {
            throw new LoginFailedException("password unmatched");
        }
    }

    public Client signupClient(Client client) {
        // TODO // validate login *//

        return repo.save(client);

    }

}
