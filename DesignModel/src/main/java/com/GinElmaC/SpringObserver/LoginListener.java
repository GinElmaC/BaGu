package com.GinElmaC.SpringObserver;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class LoginListener {

    @EventListener(classes = LoginEvent.class)
    public void login(LoginEvent event){
        System.out.println(event.getSource()+"签到经验增加");
    }
}
