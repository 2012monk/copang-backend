package com.alconn.copang.aop;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@Component
public class GlobalLoggingUnit {

    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    void get(){};

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    void post(){};
    @Pointcut("@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    void delete(){};
    @Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
    void put() {};
    @Pointcut("@annotation(org.springframework.web.bind.annotation.PatchMapping)")
    void patch(){};

    @Around("com.alconn.copang.config.PointCutConfig.get() || post() || delete() || put() || patch()")
    public Object logger(ProceedingJoinPoint pjp) throws Throwable {

        log.info("work\n\n\n\n\n\n\n\n");
        HttpServletRequest request = ((ServletRequestAttributes) Objects
            .requireNonNull(RequestContextHolder
                .getRequestAttributes())).getRequest();

        log.info("requested uri {} \n {}", request.getRequestURI(), getRequestParams());
        return pjp.proceed(pjp.getArgs());
    }

    private String getRequestParams() {

        String params = "";

        RequestAttributes requestAttribute = RequestContextHolder.getRequestAttributes();

        if(requestAttribute != null){
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();

            Map<String, String[]> paramMap = request.getParameterMap();

            String parameters =
                paramMap.entrySet().stream()
                .map(en -> String.format("%s = [%s]", en.getKey(),
                    Arrays.stream(en.getValue()).map(s -> String.format("(%s)", s)).
                    collect(Collectors.joining(","))))
                .collect(Collectors.joining(","));

            if(!paramMap.isEmpty()) {
                params = " [" + parameters + "]";
            }
        }
        return params;
    }

//    private String paramMapToString(Map<String, String[]> paramMap) {
//        return paramMap.entrySet().stream()
//            .map(entry -> String.format("%s -> (%s)")
//            .collect(Collectors.joining(", "));
//    }
}
