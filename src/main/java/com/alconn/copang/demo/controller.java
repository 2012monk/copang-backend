package com.alconn.copang.demo;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class controller {

//    @Secured("CLIENT")
    @GetMapping("/access-TEST")
    public String hello() {
        return "hello";
    }
}
