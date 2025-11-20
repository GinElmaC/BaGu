package com.GinElmaC.Observer;

import com.GinElmaC.Observer.event.WeatherUpdateEvent;
import com.GinElmaC.Observer.event.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WeatherInfo {

    private TVStatic tvStatic;

    public WeatherInfo(TVStatic tvStatic) {
        this.tvStatic = tvStatic;
    }

    public String getInfo(){
        if(new Random().nextBoolean()){
            return "晴天";
        }else{
            return "雨天";
        }
    }

    public void start() throws InterruptedException {
        while(true){
            String weather = getInfo();
            tvStatic.infoUpdate(new WeatherUpdateEvent(weather));
            Thread.sleep(3000);
        }
    }
}
