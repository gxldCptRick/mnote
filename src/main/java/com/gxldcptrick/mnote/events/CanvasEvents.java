package com.gxldcptrick.mnote.events;

import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import javafx.scene.input.MouseEvent;

public class CanvasEvents {

    private CanvasEvents() {}

    private static final CanvasEvents instance = new CanvasEvents();

    public static CanvasEvents getInstance() {
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
