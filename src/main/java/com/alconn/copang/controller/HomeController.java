package com.alconn.copang.controller;

import com.alconn.copang.annotations.IdentitySecured;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {


    @GetMapping("/")
    public String hello(){
        return "Hello!!!";
    }

    @Secured("ROLE_GUEST")
    @GetMapping("/access")
    public String access() {
        return "ok";
    }

//    @PreAuthorize("isAuthenticated()")
    @Secured("ROLE_CLIENT")
    @GetMapping("/access2")
    public String authenticate(){
        return "ok";
    }

    @IdentitySecured
    @DeleteMapping("/api/dtest")
    public String delete() {
        return "ok";
    }
}
