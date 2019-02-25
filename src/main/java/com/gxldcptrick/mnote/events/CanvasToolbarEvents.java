package com.gxldcptrick.mnote.events;

import com.gxldcptrick.mnote.enums.SpecialEffect;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import javafx.event.ActionEvent;
import javafx.scene.paint.Color;

public class CanvasToolbarEvents {
    private CanvasToolbarEvents() {

    }

    private static final CanvasToolbarEvents instance = new CanvasToolbarEvents();

    public static CanvasToolbarEvents getInstance(){
        return instance;
    }

    private final Subject<Color> changedColor = PublishSubject.create();
    private final Subject<Double> changedLineSize = PublishSubject.create();
    private final Subject<ActionEvent> clearCanvas = PublishSubject.create();
    private final Subject<SpecialEffect> changedEffect = PublishSubject.create();
    private final Subject<ActionEvent> deletingLine = PublishSubject.create();

    public Subject<Color> getChangedColor() {
        return changedColor;
    }
    public Subject<Double> getChangedLineSize() {
        return changedLineSize;
    }
    public Subject<ActionEvent> getClearCanvas() {
        return clearCanvas;
    }
    public Subject<SpecialEffect> getChangeSpecialEfects() {
        return changedEffect;
    }
    public Subject<ActionEvent> getDeletingLine() {
        return deletingLine;
    }
}
