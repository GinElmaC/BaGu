package com.GinElmaC.SpringObserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @Autowired
    private ApplicationEventPublisher publisher;

    @RequestMapping("/login")
    public void login(@RequestParam("user") String user){
        System.out.println(user+"登陆了");
        publisher.publishEvent(new LoginEvent(user));
    }
}
