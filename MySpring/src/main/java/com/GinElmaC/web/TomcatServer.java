package com.GinElmaC.web;

import com.GinElmaC.annotation.Component;
import com.GinElmaC.annotation.PostConstruct;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import java.io.File;
import java.io.IOException;

@Component
public class TomcatServer {
    @PostConstruct
    void start() throws LifecycleException {
        int port = 8080;
        //创建tomcat实例
        Tomcat tomcat = new Tomcat();
        //绑定端口
        tomcat.setPort(port);
        tomcat.getConnector();

        //创建上下文，与文件夹有关
        String contextPath = "";
        String docBase = new File(".").getAbsolutePath();
        Context context = tomcat.addContext(contextPath,docBase);

        /**
         * addServlet(要绑定的上下文, servlet的名字, Servlet的实例)
         */
        tomcat.addServlet(contextPath, "helloServlet", new HttpServlet() {
            /**
             * 重写doGet方法，接收到get方法请求处理
             */
            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                resp.setContentType("text/html");
                resp.getWriter().write("<h1>Hello from RunTomcat</h1> <br>"+req.getRequestURL().toString());
            }
        });
        //绑定“helloServlet”这个Servlet处理什么请求，这里"/*"表示处理所有请求
        context.addServletMappingDecoded("/*","helloServlet");
        //启动
        tomcat.start();
        System.out.println("tomcat is starting...");
    }
}
