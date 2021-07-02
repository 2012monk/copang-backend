package com.alconn.copang.config;

import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

public interface PointCutConfig {

    @Pointcut(value = "@annotation(org.springframework.web.bind.annotation.RestController)")
    default void restControllers(){}

    @Pointcut(value = "@annotation(com.alconn.copang.annotations.IdentitySecured)")
    default void identifyRequired(){}

    // parameter annotation 에서 포인트컷 적용안됨
    @Pointcut(value = "@annotation(com.alconn.copang.annotations.InjectId)")
    default void injectId(){}

    @Pointcut(value = "@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    default void mappings(){};


    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    default void get(){};

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    default void post(){};
    @Pointcut("@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    default void delete(){};
    @Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
    default void put() {};
    @Pointcut("@annotation(org.springframework.web.bind.annotation.PatchMapping)")
    default void patch(){};



//    @Pointcut(value = "execution(* *(.., @InjectId (*), ..))")
}
