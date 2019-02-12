package com.gxldcptrick.mnote.commonLib;

import java.util.*;

public class Event<TArgs extends EventArgs> {

    private Collection<EventListener<TArgs>> listeners;

    public Event(){
        this.listeners = new ArrayList<>();
    }

    public void subscribe(EventListener<TArgs> listener){
        Objects.requireNonNull(listener, "listener Cannot Be Null");
            listeners.add(listener);
    }
    public boolean unsubscribe(EventListener<TArgs> listener){
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
