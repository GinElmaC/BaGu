package com.GinElmaC.Observer;

import com.GinElmaC.Observer.event.WeatherUpdateEvent;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        TVStatic tvStatic = new TVStatic();
        WeatherInfo weatherInfo = new WeatherInfo(tvStatic);
        User u1 = new User("u1",(info)->{
            if(info.equals("晴天")){
                System.out.println("u1喜欢"+info);
            }else{
                System.out.println("u1不喜欢"+info);
            }
        });
        User u2 = new User("u2",(info)->{
            if(info.equals("晴天")){
                System.out.println("u2喜欢"+info);
            }else{
                System.out.println("u2不喜欢"+info);
            }
        });

        tvStatic.regi(u1, WeatherUpdateEvent.class);
        tvStatic.regi(u2, WeatherUpdateEvent.class);

        weatherInfo.start();
    }
}
