package com.GinElmaC.proxy;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.util.concurrent.atomic.AtomicInteger;

public class MyProxy {
    private static final AtomicInteger count = new AtomicInteger(1);

    /**
     * 创建类的文件
     * @param className
     * @return
     * @throws IOException
     */
    private static File createJavaFile(String className,MyHandler myHandler) throws IOException {
        String function1 = myHandler.functionBody("fun1()");
        String function2 = myHandler.functionBody("fun2()");
        String function3 = myHandler.functionBody("fun3()");
        String context = "package com.GinElmaC.proxy;\n" +
                "\n" +
                "public class " + className + " implements MyInter{\n" +
                "    MyInter myInter;\n"+
                "    @Override\n" +
                "    public void fun1() {\n" +
                "        "+function1+"\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public void fun2() {\n" +
                "        "+function2+"\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public void fun3() {\n" +
                "        "+function3+"\n" +
                "    }\n" +
                "}\n";
        File JavaFile = new File(className+".java");
        Files.writeString(JavaFile.toPath(),context);
        return JavaFile;
    }

    /**
     * 动态得到类名
     * @return
     */
    private static String getClassName(){
        return "Name$proxy"+count.incrementAndGet();
    }

    /**
     * 创建代理的方法，利用这个类的类加载器加载目标对象字节码文件，并使用目标类的构造方法来创还能对象
     * @param className
     * @return
     * @throws Exception
     */
    private static MyInter newInstance(String className,MyHandler myHandler) throws Exception {
        Class<?> aClass = MyProxy.class.getClassLoader().loadClass(className);
        Constructor<?> constructor = aClass.getConstructor();
        MyInter proxy = (MyInter)constructor.newInstance();
        myHandler.setProxy(proxy);
        return proxy;
    }

    /**
     * 对外提供的函数
     * @return
     * @throws Exception
     */
    public static MyInter createProxyObject(MyHandler myHandler) throws Exception {
        String className = getClassName();
        File javaFile = createJavaFile(className,myHandler);
        Compiler.compile(javaFile);
        return newInstance("com.GinElmaC.proxy."+className,myHandler);
    }
}
