package edu.iastate.cssm.springtwotokens.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping("/")
    String home() {
        return "Hello World!";
    }

    @PostMapping("/")
    String homePost(@RequestParam("message") String message) {
        return "Hello: " + message;
    }

}