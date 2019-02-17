package com.gxldcptrick.mnote.commonLib;

import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import javafx.scene.input.MouseEvent;

public class JavaFXEvents {

    private JavaFXEvents() {}

    private static final JavaFXEvents instance = new JavaFXEvents();

    public static JavaFXEvents getInstance() {
        return instance;
    }

    private final Subject<MouseEvent> mouseEvents = PublishSubject.create();

    public Subject<MouseEvent> getMouseEvents() {
        return mouseEvents;
    }

}
