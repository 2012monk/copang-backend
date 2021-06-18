package com.alconn.copang.common;

import com.alconn.copang.client.Client;
import com.alconn.copang.client.Role;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;

public class AuthToken extends AbstractAuthenticationToken {

    private final String token;

    private Object user;

    public AuthToken(String token, Object user, Role role) {
        super(Collections.singletonList(new SimpleGrantedAuthority(role.name())));
        this.token = token;
        this.user = user;
    }

    public AuthToken(Collection<? extends GrantedAuthority> authorities, String token, Object user) {
        super(authorities);
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
}
