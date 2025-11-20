package com.GinElmaC.jvm;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class HotSpot {
    private String mainClass;
    private List<String> classPath;

    public HotSpot(String mainClass,String classPathString){
        this.mainClass = mainClass;
        this.classPath = Arrays.asList(classPathString.split(File.pathSeparator));
    }

//    public void start(){
//        classPath.stream()
//                .map(path->tryLoad(path,mainClass))
//                .filter(Objects::nonNull)
//                .findAny()
//                .orElseThrow(new ClassNotFoundException(mainClass+"找不到"));
//    }
}
