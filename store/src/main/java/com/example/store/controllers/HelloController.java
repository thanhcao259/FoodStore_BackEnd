package com.example.store.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/store")
public class HelloController {

    @GetMapping("/hello")
    public String hello(){
        return "Hello World";
    }
}
