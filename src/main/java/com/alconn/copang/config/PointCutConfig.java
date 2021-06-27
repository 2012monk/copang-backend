package com.alconn.copang.config;

import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

public interface PointCutConfig {

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RestController)")
    default void restControllers(){}

    @Pointcut(value = "@annotation(com.alconn.copang.annotations.IdentitySecured)")
    default void identifyRequired(){}

    // parameter annotation 에서 포인트컷 적용안됨
    @Pointcut(value = "@annotation(com.alconn.copang.annotations.InjectId)")
//    @Pointcut(value = "execution(* *(.., @InjectId (*), ..))")
    default void injectId(){}
}
