package com.alconn.copang.security;

import com.alconn.copang.client.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

@Slf4j
public class AuthToken extends AbstractAuthenticationToken {

    private final String token;

    private Object user;

    public AuthToken(String token, Object user, Role role) {
        super(Collections.singletonList(new SimpleGrantedAuthority(role == null ? "ROLE_GUEST" : "ROLE_" + role.name())));
        log.warn("role {}", role == null);
        this.token = token;
        this.user = user;
    }


    @Override
    public Object getCredentials() {
        return this.token;
    }

    @Override
    public Object getPrincipal() {
        return this.user;
    }

    public String getToken() {
    return this.token;
    }
}
