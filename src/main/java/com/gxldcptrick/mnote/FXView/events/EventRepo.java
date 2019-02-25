package com.gxldcptrick.mnote.FXView.events;

import com.gxldcptrick.mnote.commonLib.Event;
import com.gxldcptrick.mnote.commonLib.EventArgs;
import com.gxldcptrick.mnote.commonLib.EventListener;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class EventRepo<T extends EventArgs>{
    private Map<String, Event<T>> eventsInRepo;

    EventRepo(){
        eventsInRepo = new Hashtable<>();
    }

    public void addEventToRepo(Event<T> eventToAdd, String eventName){
        validateParameters(eventToAdd, eventName);
        eventsInRepo.put(eventName, eventToAdd);
    }

    public void removeEventFromRepo(Event<T> eventToRemove, String eventName){
        validateParameters(eventToRemove, eventName);
        if(eventsInRepo.containsKey(eventName) && eventsInRepo.get(eventName) == eventToRemove){
            eventsInRepo.remove(eventName, eventToRemove);
        }
    }

    public void subscribeToEvent(EventListener<T> listener, String eventName){
        validateParameters(listener, eventName);
        eventsInRepo.get(eventName).subscribe(listener);
    }

    private void validateParameters(Event<T> eventToAdd, String eventName) {
        if(eventToAdd == null) throw new IllegalArgumentException("eventToAdd cannot be null");
        if(eventName == null) throw new IllegalArgumentException("eventName Cannot be null");
    }

    private void validateParameters(EventListener<T> listener, String eventName) {
        if(listener == null) throw new IllegalArgumentException("listener cannot be null");
        if(eventName == null) throw new IllegalArgumentException("eventName Cannot be null");
        if(!eventsInRepo.containsKey(eventName)) throw new IllegalArgumentException("The Event \""+ eventName + "\" does not exist.");
    }

    public void unsubscribeToEvent(EventListener<T> listener, String eventName) {
        validateParameters(listener, eventName);
        eventsInRepo.get(eventName).unsubscribe(listener);
    }
}
