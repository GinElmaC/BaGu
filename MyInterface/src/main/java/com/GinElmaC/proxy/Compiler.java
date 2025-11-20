package com.GinElmaC.proxy;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

public class Compiler {

    /**
     * 动态编译指定的Java源文件到指定目录
     * @param javaFile 要编译的Java源文件
     * @throws RuntimeException 如果编译失败或文件不存在
     */
    public static void compile(File javaFile) {
        // 1. 参数校验
        if (javaFile == null || !javaFile.exists()) {
            throw new IllegalArgumentException("Java文件不存在或为null");
        }
        if (!javaFile.getName().endsWith(".java")) {
            throw new IllegalArgumentException("必须提供.java源文件");
        }

        // 2. 创建target/classes目录
        File outputDir = new File("MyInterface/target/classes");
        if (!outputDir.exists() && !outputDir.mkdirs()) {
            throw new RuntimeException("无法创建输出目录: " + outputDir.getAbsolutePath());
        }

        // 3. 获取系统Java编译器
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new RuntimeException("无法获取Java编译器，请确保使用JDK而非JRE运行");
        }

        // 4. 配置编译参数
        try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(
                null,
                null,
                StandardCharsets.UTF_8)) {

            // 设置输出目录为target/classes
            fileManager.setLocation(StandardLocation.CLASS_OUTPUT,
                    Collections.singletonList(outputDir));

            // 5. 执行编译
            Iterable<? extends JavaFileObject> compilationUnits =
                    fileManager.getJavaFileObjectsFromFiles(Collections.singletonList(javaFile));

            DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
            boolean success = compiler.getTask(
                    null,
                    fileManager,
                    diagnostics,
                    null,
                    null,
                    compilationUnits
            ).call();

            // 6. 处理编译结果
            if (!success) {
                StringBuilder errorMsg = new StringBuilder("编译失败:\n");
                for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
                    errorMsg.append(String.format(
                            "Line %d: %s%n",
                            diagnostic.getLineNumber(),
                            diagnostic.getMessage(null)
                    ));
                }
                throw new RuntimeException(errorMsg.toString());
            }
        } catch (IOException e) {
            throw new RuntimeException("编译过程中发生IO异常", e);
        }
    }
}
