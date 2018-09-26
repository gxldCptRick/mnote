package com.gxldcptrick.mnote.commonLib;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;

public class Event<TEventListener extends EventListener<TArgs>, TArgs extends EventArgs> {

    private Collection<TEventListener> listeners;

    public Event(){
        this(new ConcurrentLinkedQueue<>());
    }

    public Event(Collection<TEventListener> listeners){
        this.listeners = listeners;
    }

    public void subscribe(TEventListener listener){
        Objects.requireNonNull(listener, "Cannot subscribe a null Listener");
            listeners.add(listener);
    }
    public boolean unsubscribe(TEventListener listener){
        return listeners.remove(listener);
    }
    public void invoke(Object sender, TArgs args){
        for (var listener: listeners) {
                listener.callBack(sender, args);
        }
    }

    public int getSize() {
        return listeners.size();
    }
}
