package com.gxldcptrick.mnote.controllers;

import com.gxldcptrick.mnote.commonLib.JavaFXEvents;
import io.reactivex.disposables.Disposable;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import io.reactivex.rxjavafx.observables.JavaFxObservable;

public class CanvasController {
    @FXML
    private Canvas canvas;

    private GraphicsContext context;

    @FXML
    public void initialize(){
        context = canvas.getGraphicsContext2D();

        JavaFxObservable.eventsOf(canvas, MouseEvent.MOUSE_PRESSED)
                .subscribe(JavaFXEvents.getInstance().getMouseDownEvents());

        JavaFxObservable.eventsOf(canvas, MouseEvent.MOUSE_DRAGGED)
                .subscribe(JavaFXEvents.getInstance().getMouseDragEvents());

        JavaFxObservable.eventsOf(canvas, MouseEvent.MOUSE_RELEASED)
                .subscribe(JavaFXEvents.getInstance().getMouseUpEvents());

        JavaFXEvents.getInstance().getMouseDownEvents().subscribe(this::startLine);
        JavaFXEvents.getInstance().getMouseDragEvents().subscribe(this::writeLine);
        JavaFXEvents.getInstance().getMouseDragEvents().subscribe(this::endLine);
    }

    private void endLine(MouseEvent mouseEvent) {
        context.lineTo(mouseEvent.getX(), mouseEvent.getY());
        context.stroke();
    }

    private void writeLine(MouseEvent mouseEvent) {
        context.lineTo(mouseEvent.getX(), mouseEvent.getY());
        context.stroke();
    }

    private void startLine(MouseEvent mouseEvent) {
        context.beginPath();
        context.lineTo(mouseEvent.getX(), mouseEvent.getY());
        context.stroke();
    }


}
