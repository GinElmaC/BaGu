package com.GinElmaC.sub;


import com.GinElmaC.annotation.Autowired;
import com.GinElmaC.annotation.Component;
import com.GinElmaC.annotation.PostConstruct;

@Component()
public class test {
    @Autowired
    private Cat cat;

    @PostConstruct
    public void init(){
        System.out.println("初始化cat");
    }

    @PostConstruct
    public void init2(){
        System.out.println("第二个方法");
    }
}
