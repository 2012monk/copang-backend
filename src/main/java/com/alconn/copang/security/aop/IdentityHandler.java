package com.alconn.copang.security.aop;

import com.alconn.copang.client.Client;
import com.alconn.copang.exceptions.UnauthorizedException;
import com.alconn.copang.security.AuthToken;
import com.alconn.copang.security.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Aspect
@Component
@Order(1)
public class IdentityHandler {

    private final JwtTokenProvider provider;

//    @Secured("CLIENT")
//    @After("execution(* com.alconn.copang.security.JwtValidateFilter.doFilter())")
    @Around(value = "com.alconn.copang.config.PointCutConfig.identifyRequired()")
    public Object joinPoint(ProceedingJoinPoint pjp) throws Throwable {
        log.info("authorization aop working =============================\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        AuthToken authentication = (AuthToken) SecurityContextHolder.getContext().getAuthentication();
//        if (authentication.getToken() == null) {
//            throw new AccessDeniedException("인증정보가 없습니다");
//        }

//        Arrays.stream(pjp.getArgs()).forEach(System.out::println);

        Client client = provider.resolveUserFromToken(authentication.getToken()).orElseThrow(() -> new UnauthorizedException("인증정보가 잘못 되었습니당!"));
        Long id = client.getClientId();
//        try{
//
//        }catch (Exception e){
//            log.warn("print", e);
//        }


        //        return pjp.proceed();
//        return pjp.proceed(pjp.getArgs());
//        Arrays.stream(pjp.getArgs()).forEach(c -> log.warn(c.getClass().getName()));
//        return pjp.proceed(pjp.getArgs());

        return pjp.proceed(pjp.getArgs());
    }
}
