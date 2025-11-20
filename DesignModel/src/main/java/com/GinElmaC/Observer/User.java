package com.GinElmaC.Observer;

import com.GinElmaC.Observer.event.event;
import com.GinElmaC.Observer.listener.eventListener;

import java.util.function.Consumer;

public class User implements eventListener {
    private final String name;
    //行为
    private Consumer<String> action;

    public User(String name,Consumer<String> action) {
        this.name = name;
        this.action = action;
    }

    public void doAction(String info){
        action.accept(info);
    }

    public String getName() {
        return name;
    }

    @Override
    public void doEvent(event e) {
        doAction((String)e.source());
    }
}
