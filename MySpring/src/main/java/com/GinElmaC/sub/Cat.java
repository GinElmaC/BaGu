package com.GinElmaC.sub;

import com.GinElmaC.annotation.Autowired;
import com.GinElmaC.annotation.Component;
import com.GinElmaC.annotation.PostConstruct;

@Component
public class Cat {
    @Autowired
    private test test;

    @Autowired
    private Cat cat;

    @PostConstruct
    public void init(){
        System.out.println("cat创建完成"+cat);
    }
}
