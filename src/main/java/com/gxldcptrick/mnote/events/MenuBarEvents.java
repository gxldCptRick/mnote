package com.gxldcptrick.mnote.events;

import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import javafx.event.ActionEvent;

public class MenuBarEvents {
    private MenuBarEvents() {}

    private static final MenuBarEvents instance = new MenuBarEvents();

    public static MenuBarEvents getInstance(){
        return instance;
    }

    private final  Subject<ActionEvent> startSession = PublishSubject.create();
    private final Subject<ActionEvent> joinSession = PublishSubject.create();
    private final Subject<ActionEvent> endSession = PublishSubject.create();

    public Subject<ActionEvent> getStartSession() {
        return startSession;
    }
    public Subject<ActionEvent> getJoinSession() {
        return joinSession;
    }
    public Subject<ActionEvent> getEndSession() {
        return endSession;
    }
}
