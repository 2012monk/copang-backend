package com.alconn.copang.security;

import com.alconn.copang.client.Client;
import com.alconn.copang.security.privider.JwtTokenProvider;
import com.alconn.copang.utils.HttpUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtValidateFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService service;



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
        Optional<String> token = HttpUtils.getAuthenticationHeader(request, AuthenticationScheme.BEARER);
        token.ifPresent(t -> SecurityContextHolder.getContext().setAuthentication(service.getAuthentication(t)));
        log.info("context : {}", SecurityContextHolder.getContext().getAuthentication() == null);
        }catch (Exception e){

        }
        finally {
            filterChain.doFilter(request, response);
        }


    }
}
