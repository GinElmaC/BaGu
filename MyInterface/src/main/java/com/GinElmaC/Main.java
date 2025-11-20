package com.GinElmaC;

import com.GinElmaC.proxy.MyHandler;
import com.GinElmaC.proxy.MyInter;
import com.GinElmaC.proxy.MyProxy;

import java.lang.reflect.Field;

public class Main {
    public static void main(String[] args) throws Exception {
        MyInter proxy = MyProxy.createProxyObject(new printName());

        MyInter proxyObject = MyProxy.createProxyObject(new logHandler(proxy));
        proxyObject.fun1();
        proxyObject.fun2();
        proxyObject.fun3();
    }
    static class printName implements MyHandler{

        @Override
        public String functionBody(String methodName) {
            return "System.out.println(\""+methodName+"\");";
        }
    }

    static class logHandler implements MyHandler{

        MyInter myInter;

        public logHandler(MyInter myInter) {
            this.myInter = myInter;
        }

        @Override
        public String functionBody(String methodName) {
            String context = "System.out.println(\"before\");\n" +
                    "        myInter."+methodName+";\n" +
                    "        System.out.println(\"after\");";
            return context;
        }

        @Override
        public void setProxy(MyInter proxy) {
            Class<? extends MyInter> aClass = proxy.getClass();
            try {
                Field field = aClass.getDeclaredField("myInter");
                field.setAccessible(true);
                field.set(proxy,myInter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
