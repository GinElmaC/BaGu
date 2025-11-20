package com.GinElmaC.SpringObserver;

import org.springframework.context.ApplicationEvent;

public class LoginEvent extends ApplicationEvent {
    String source;

    public LoginEvent(String source) {
        super(source);
        this.source = source;
    }

    @Override
    public String getSource() {
        return source;
    }
}
