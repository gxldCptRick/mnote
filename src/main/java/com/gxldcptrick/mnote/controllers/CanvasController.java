package com.gxldcptrick.mnote.controllers;

import com.gxldcptrick.mnote.commonLib.JavaFXEvents;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

public class CanvasController {
    @FXML
    private Canvas canvas;

    private GraphicsContext context;

    @FXML
    public void initialize(){
        context = canvas.getGraphicsContext2D();
        JavaFxObservable.eventsOf(canvas, MouseEvent.MOUSE_PRESSED)
                .subscribe(mouseEvent -> {
                    startLine(mouseEvent);
                    JavaFXEvents.getInstance().getMouseEvents();
                });

        JavaFxObservable.eventsOf(canvas, MouseEvent.MOUSE_DRAGGED)
                .subscribe(mouseEvent ->{
                    writeLine(mouseEvent);
                    JavaFXEvents.getInstance().getMouseEvents();
                });

        JavaFxObservable.eventsOf(canvas, MouseEvent.MOUSE_RELEASED)
                .subscribe(mouseEvent -> {
                            endLine(mouseEvent);
                            JavaFXEvents.getInstance().getMouseEvents();
                        },
                        System.out::println,
                        System.out::println);
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
