package com.placement.Placement.controller;

import com.placement.Placement.constant.AppPath;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AppPath.API + AppPath.PLACEMENT)
public class TestController {

    @GetMapping("/hello/customer")
    public String hello(){
        return "nice customer";
    }

    @GetMapping("/hello/admin")
    public String helloAdmin(){
        return "nice admin";
    }

    @GetMapping("/hello/super_admin")
    public String hellosuper(){
        return "nice super admin";
    }
}
