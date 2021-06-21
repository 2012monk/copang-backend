package com.alconn.copang.security;

import com.alconn.copang.utils.HttpUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomLogoutHandler implements LogoutHandler {

    private final Set<String> blackList;


    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        System.out.println("\n\n\n\n\n Logout \n\n"+ (blackList != null));
        HttpUtils.getCookie(request, "ref").ifPresent(blackList::add);
        Cookie invalidate = new Cookie("ref", "no");
        invalidate.setMaxAge(-1);
        response.addCookie(invalidate);
        HttpUtils.getAuthenticationHeader(request, AuthenticationScheme.BEARER).ifPresent(blackList::add);
        blackList.forEach(log::warn);
        try {
            response.getWriter().write("ok");
        } catch (IOException e) {
        }
    }
}
