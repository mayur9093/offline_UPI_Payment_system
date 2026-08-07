package com.mayur.offline_UPI_system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloController {
    @GetMapping("/hello")
    String hello() {
        return "Welcome to offline upi system";
    }

    @GetMapping("/about")
    String aboutus() {
        return "this is over offline upi system";
    }

    @GetMapping("/developer")
    String developedBy() {
        return "Developed By Mayur";
    }

    @GetMapping("/version")
    String version() {
        return "Version 1.0";
    }

}
