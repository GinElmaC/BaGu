package com.GinElmaC.Observer.event;

public class BaseEvent implements event{
    long timestamp;

    @Override
    public long timestamp() {
        return System.currentTimeMillis();
    }

    @Override
    public Object source() {
        return null;
    }
}
