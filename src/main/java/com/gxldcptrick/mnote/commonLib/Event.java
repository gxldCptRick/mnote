package com.gxldcptrick.mnote.commonLib;

public class Event<TEventArgs extends EventArgs> {

    private final Delegate<TEventArgs> delegate;
    public Event(Delegate<TEventArgs> delegate){
        this.delegate = delegate;
    }

    public void subscribe(EventListener<TEventArgs> listener){
        this.delegate.subscribe(listener);
    }
    public void unsubscribe(EventListener<TEventArgs> listener){
        this.delegate.unsubscribe(listener);
    }
}
