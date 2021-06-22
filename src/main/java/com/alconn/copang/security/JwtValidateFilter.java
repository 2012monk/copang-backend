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
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtValidateFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService service;

    private final Set<String> blackList;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try{
            Optional<String> token = HttpUtils.getAuthenticationHeader(request, AuthenticationScheme.BEARER);
            String res = token.orElseGet(() ->"no token");
            if (!blackList.contains(res)) {
                SecurityContextHolder.getContext().setAuthentication(service.getAuthentication(res));
            }
            else {
                log.info("blacklisted token access {}", blackList.size());
                SecurityContextHolder.getContext().setAuthentication(service.getAuthentication());
            }
            log.info("security context authentication : {}", SecurityContextHolder.getContext().getAuthentication());
            String ref = HttpUtils.getCookie(request, "ref").orElseGet(() -> "no ref");

        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            filterChain.doFilter(request, response);
        }


    }


}
