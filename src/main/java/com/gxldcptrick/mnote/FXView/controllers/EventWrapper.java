package com.gxldcptrick.mnote.FXView.controllers;

import com.gxldcptrick.mnote.FXView.events.EventHolder;

public abstract class EventWrapper {
    public EventWrapper(EventHolder holder){
        this.attachToEvents(holder);
    }

    protected abstract void attachToEvents(EventHolder holder);
    public abstract  void detachFromEvents(EventHolder holder);
}
