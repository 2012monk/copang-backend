package com.alconn.copang.utils;

import com.alconn.copang.security.AuthenticationScheme;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class HttpUtils {

    public static Optional<String> getAuthenticationHeader(HttpServletRequest request, AuthenticationScheme scheme) {

        String authentication = getHeader(request, HttpHeaders.AUTHORIZATION).orElseThrow(IllegalArgumentException::new);
        String requestScheme = authentication.split(" ")[0];
        String value = authentication.split(" ")[1];

        if (scheme.getVal().equals(requestScheme)) {
            return Optional.ofNullable(value);
        }
        return Optional.empty();
    }

    public static Optional<String> getHeader(HttpServletRequest request, String name) {

        Map<String ,String> header = Collections.list(request.getHeaderNames())
                .stream()
                .collect(Collectors.toMap(h -> h, request::getHeader));
        String value = header.get(name);
        return Optional.ofNullable(value);
    }


}
