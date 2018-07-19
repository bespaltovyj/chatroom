package com.rnd.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityContoller {


    @GetMapping("/sec-msg")
    @Secured("ROLE_ADMIN")
    public String msg(){
        return "Security message for role ADMIN";
    }
}
