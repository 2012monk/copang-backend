package com.alconn.copang.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DocumentController {

    @RequestMapping("/docs")
    public String docs() {
        return "redirect:/docs/index.html";
    }
}
