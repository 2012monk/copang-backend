package com.alconn.copang.security.provider;

import com.alconn.copang.security.AuthToken;
import com.alconn.copang.exceptions.InvalidTokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtTokenProvider provider;

//    private final ClientService service;

    public JwtAuthenticationProvider(JwtTokenProvider provider) {
        super();
        this.provider = provider;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = (String) authentication.getCredentials();
        try{
            provider.validateAccessToken(token);
            authentication.setAuthenticated(true);
            log.info("authentication {}{}{}{}",
                    authentication.getCredentials(),
                    authentication.getPrincipal(),
                    authentication.getAuthorities(),
                    authentication.getName());

        }catch (InvalidTokenException e){
//            throw new AuthenticationServiceException("authentication failed");
//            throw new BadCredentialsException("token invalid");
        }
        return authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        if (authentication.isAssignableFrom(AuthToken.class)) {
            return true;
        }
        return false;
    }
}
