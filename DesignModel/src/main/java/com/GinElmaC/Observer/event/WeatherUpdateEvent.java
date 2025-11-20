package com.GinElmaC.Observer.event;

public class WeatherUpdateEvent extends BaseEvent{

    String info;

    public WeatherUpdateEvent(String info) {
        this.info = info;
    }

    @Override
    public Object source() {
        return info;
    }

}
