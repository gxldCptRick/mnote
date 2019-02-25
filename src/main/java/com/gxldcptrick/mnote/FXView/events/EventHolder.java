package com.gxldcptrick.mnote.FXView.events;

import com.gxldcptrick.mnote.FXView.models.BrushChangedEventArgs;
import com.gxldcptrick.mnote.FXView.models.MouseEventArgs;
import com.gxldcptrick.mnote.commonLib.Event;
import com.gxldcptrick.mnote.commonLib.EventArgs;
import com.gxldcptrick.mnote.network.DrawingEventArgs;

public class EventHolder {
    public static final EventHolder instance;
    static {
        instance = new EventHolder();
    }
    private EventRepo<EventArgs> emptyEvents;
    private EventRepo<MouseEventArgs> mouseEvents;
    private EventRepo<BrushChangedEventArgs> brushEvents;

    EventHolder(){
        emptyEvents = new EventRepo<>();
        mouseEvents =  new EventRepo<>();
        brushEvents = new EventRepo<>();
    }

    public EventRepo<MouseEventArgs> getMouseEvents(){
        return this.mouseEvents;
    }
    public EventRepo<EventArgs> getEmptyEvents() { return this.emptyEvents; }

    public EventRepo<BrushChangedEventArgs> getBrushEvents() { return brushEvents; }
}
