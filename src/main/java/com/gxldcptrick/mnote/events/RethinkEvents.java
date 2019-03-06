package com.gxldcptrick.mnote.events;

import com.fasterxml.jackson.databind.JsonNode;
import com.gxldcptrick.mnote.models.DrawingPackage;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import javafx.stage.WindowEvent;

public class RethinkEvents {
    private RethinkEvents() {

    }

    private static final RethinkEvents instance = new RethinkEvents();

    public static RethinkEvents getInstance(){
        return instance;
    }

    private final Subject<WindowEvent> disconnect = PublishSubject.create();
    private final Subject<Boolean> disconnectFromChanges = PublishSubject.create();
    private final Subject<String> newClientConnected = PublishSubject.create();
    private final Subject<Boolean> online = PublishSubject.create();
    private final Subject<String> sessionId = PublishSubject.create();
    private final Subject<DrawingPackage> linesBeingDrawn = PublishSubject.create();

    public Subject<WindowEvent> getDisconnect() {
        return disconnect;
    }
    public Subject<Boolean> getDisconnectFromChanges(){
        return disconnectFromChanges;
    }
    public Subject<String> getNewClientConnected() {
        return newClientConnected;
    }
    public Subject<Boolean> getOnline() {
        return online;
    }
    public Subject<String> getSessionId() {
        return sessionId;
    }
    public Subject<DrawingPackage> getLinesBeingDrawn() {
        return linesBeingDrawn;
    }
}
