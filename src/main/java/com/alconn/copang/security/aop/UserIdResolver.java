package com.alconn.copang.security.aop;

import com.alconn.copang.annotations.InjectId;
import com.alconn.copang.client.Client;
import com.alconn.copang.exceptions.UnauthorizedException;
import com.alconn.copang.security.AuthToken;
import com.alconn.copang.security.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
@Component
public class UserIdResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider provider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(InjectId.class) && parameter.getParameterType().isAssignableFrom(Long.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        AuthToken authToken = (AuthToken) SecurityContextHolder.getContext().getAuthentication();
        Client client = provider.resolveUserFromToken(authToken.getToken()).orElseThrow(() -> new UnauthorizedException("인증정보가 잘못 되었습니당!"));

        return client.getClientId();
    }
}
