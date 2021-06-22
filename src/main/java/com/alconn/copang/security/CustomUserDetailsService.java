package com.alconn.copang.security;

import com.alconn.copang.client.Client;
import com.alconn.copang.client.Role;
import com.alconn.copang.security.privider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService {

    private final JwtTokenProvider provider;

    public UserDetails generateUserDetails(String accessToken) throws UsernameNotFoundException {
        Client client = provider.resolveUserFromToken(accessToken).orElseThrow(() -> new UsernameNotFoundException("invalid token"));
        return User.builder()
                .username(client.getUsername())
                .password(client.getPassword())
                .roles(client.getRole().name())
                .authorities(accessToken)
                .build();
    }

    public Client getClient(String accessToken) {
        return provider.resolveUserFromToken(accessToken).orElseThrow(() -> new UsernameNotFoundException("invalid token"));
    }

    public Authentication getAuthentication(String token) {
        Client client = provider.resolveUserFromToken(token).orElse(Client.builder().role(Role.GUEST).build());
        return new AuthToken(token, client, client.getRole());
    }

    public Authentication getAuthentication() {
        return new AuthToken("null", Client.builder().role(Role.GUEST).build(), Role.GUEST);
    }


}
