package com.GinElmaC.proxy;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class CreateJavaFile{
    public static File write() throws IOException {
        String context = "package com.GinElmaC.proxy;\n" +
                "\n" +
                "/**\n" +
                " * 打印自己的名字和长度\n" +
                " */\n" +
                "public class NameAndLengthImpl implements MyInter{\n" +
                "    @Override\n" +
                "    public void fun1() {\n" +
                "        System.out.println(\"fun1\"+\"fun1\".length());\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public void fun2() {\n" +
                "        System.out.println(\"fun2\");\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public void fun3() {\n" +
                "        System.out.println(\"fun3\");\n" +
                "    }\n" +
                "}\n";
        File JavaFile = new File("NameAndLengthImpl.java");
        Files.writeString(JavaFile.toPath(),context);
        return JavaFile;
    }

    public static void main(String[] args) throws IOException {
        write();
    }
}
