package com.gxldcptrick.mnote.commonLib;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import javafx.scene.input.MouseEvent;

public class JavaFXEvents {

    private JavaFXEvents() {}

    private static final JavaFXEvents instance = new JavaFXEvents();

    public static JavaFXEvents getInstance() {
        return instance;
    }

    private final Subject<MouseEvent> mouseDown = PublishSubject.create();
    private final Subject<MouseEvent> mouseDrag = PublishSubject.create();
    private final Subject<MouseEvent> mouseUp = PublishSubject.create();

    public Subject<MouseEvent> getMouseDownEvents() {
        return mouseDown;
    }
    public Subject<MouseEvent> getMouseDragEvents() {
        return mouseDrag;
    }
    public Subject<MouseEvent> getMouseUpEvents() {
        return mouseUp;
    }

}
