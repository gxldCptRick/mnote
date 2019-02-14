package com.gxldcptrick.mnote.commonLib;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public abstract class Event<TArgs extends EventArgs> {
    protected Collection<EventListener<TArgs>> listeners;
    public Event(){
        this.listeners = new ArrayList<>();
    }
    public void subscribe(EventListener<TArgs> listener){
        Objects.requireNonNull(listener, "listener Cannot Be Null");
        listeners.add(listener);
    }
    public void unsubscribe(EventListener<TArgs> listener){
        listeners.remove(listener);
    }
}
