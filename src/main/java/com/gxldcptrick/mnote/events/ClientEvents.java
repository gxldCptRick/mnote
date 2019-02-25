package com.gxldcptrick.mnote.events;

import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import javafx.stage.WindowEvent;

public class ClientEvents {
    private ClientEvents() {

    }

    private static final ClientEvents instance = new ClientEvents();

    public static ClientEvents getInstance(){
        return instance;
    }

    private final Subject<WindowEvent> disconnect = PublishSubject.create();
    private final Subject<Boolean> disconnectFromChanges = PublishSubject.create();
    private final Subject<Object> newClientConnected = PublishSubject.create();
    private final Subject<Boolean> online = PublishSubject.create();

    public Subject<WindowEvent> getDisconnect() {
        return disconnect;
    }
    public Subject<Boolean> getDisconnectFromChanges(){
        return disconnectFromChanges;
    }
    public Subject<Object> getNewClientConnected() {
        return newClientConnected;
    }
    public Subject<Boolean> getOnline() {
        return online;
    }
}
