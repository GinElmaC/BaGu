package com.GinElmaC.Observer;

import com.GinElmaC.Observer.event.event;
import jdk.nashorn.internal.runtime.linker.LinkerCallSite;

import java.util.*;

import com.GinElmaC.Observer.listener.eventListener;

public class TVStatic {
    //订阅列表
    private static List<eventListener> listeners = new ArrayList<>();

    private final Map<Class<? extends event>, List<eventListener>> listenerMap = new HashMap<>();
    //订阅
    public void regi(eventListener listener,Class<? extends event> eventClass){
        listenerMap.computeIfAbsent(eventClass,k->new ArrayList<>()).add(listener);
    }

    public void infoUpdate(event e){
        Class<? extends event> aClass = e.getClass();
        List<eventListener> eventListeners = listenerMap.get(aClass);
        if(eventListeners != null){
            for(eventListener eventListener:eventListeners){
                eventListener.doEvent(e);
            }
        }
    }
}
