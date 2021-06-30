package com.alconn.copang.utils;

import com.alconn.copang.security.AuthenticationScheme;
import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.parameters.P;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public class HttpUtils {

    public static Optional<String> getAuthenticationHeader(HttpServletRequest request, AuthenticationScheme scheme) {

        try{

            String authentication = getHeader(request, HttpHeaders.AUTHORIZATION).orElse("no token ");
            String requestScheme = authentication.split(" ")[0];
            String value = authentication.split(" ")[1];

            Pattern pattern = Pattern.compile(scheme.getVal(), Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(requestScheme);
            if (matcher.find()){
                return Optional.ofNullable(value);
            }
        }catch (Exception e){
            log.info("header parse failed : {}", e.getMessage());
        }

//        if (scheme.getVal().equals(requestScheme)) {
//        }
        return Optional.empty();
    }

    public static Optional<String> getHeader(HttpServletRequest request, String name) {

        Map<String ,String> header = Collections.list(request.getHeaderNames())
                .stream()
                .collect(Collectors.toMap(h -> h.toLowerCase(Locale.ROOT), request::getHeader));
        String value = header.get(name.toLowerCase(Locale.ROOT));
        return Optional.ofNullable(value);
    }

    public static Optional<String> getCookie(HttpServletRequest request, String name) {
        try{
            Cookie cookie = Arrays.stream(request.getCookies())
                    .filter(c -> c.getName().equals(name))
                    .collect(Collectors.toList()).get(0);
            if (cookie != null) {
                return Optional.of(cookie.getValue());
            }
        }catch (Exception e){}

        return Optional.empty();
    }


}
