package com.gxldcptrick.mnote.controllers;

import com.gxldcptrick.mnote.FXView.enums.SpecialEffect;
import com.gxldcptrick.mnote.events.CanvasEvents;
import com.gxldcptrick.mnote.events.CanvasToolbarEvents;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.scene.paint.Color;

public class CanvasController {
    @FXML
    private Canvas canvas;

    private GraphicsContext context;

    @FXML
    public void initialize(){
        context = canvas.getGraphicsContext2D();

//        JavaFxObservable.eventsOf(canvas, MouseEvent.MOUSE_PRESSED)
//                .subscribe(CanvasEvents.getInstance().getMouseDownEvents());
//
//        JavaFxObservable.eventsOf(canvas, MouseEvent.MOUSE_DRAGGED)
//                .subscribe(CanvasEvents.getInstance().getMouseDragEvents());
//
//        JavaFxObservable.eventsOf(canvas, MouseEvent.MOUSE_RELEASED)
//                .subscribe(CanvasEvents.getInstance().getMouseUpEvents());
//
//        CanvasEvents.getInstance().getMouseDownEvents().subscribe(this::startLine);
//        CanvasEvents.getInstance().getMouseDragEvents().subscribe(this::writeLine);
//        CanvasEvents.getInstance().getMouseUpEvents().subscribe(this::endLine);

//        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, this::startLine);
//        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::writeLine);
//        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, this::endLine);

        CanvasToolbarEvents.getInstance().getChangeSpecialEfects().subscribe(this::changeEffect);
        CanvasToolbarEvents.getInstance().getChangedLineSize().subscribe(this::changeLineSize);
        CanvasToolbarEvents.getInstance().getChangedColor().subscribe(this::changeColor);
        CanvasToolbarEvents.getInstance().getClearCanvas().subscribe(this::clearCanvas);
        CanvasToolbarEvents.getInstance().getDeletingLine().subscribe(this::deletingLine);
    }

//    private void endLine(MouseEvent mouseEvent) {
//        System.out.println("Ending line at X:" + mouseEvent.getX() + "  Y: " + mouseEvent.getY());
//        context.lineTo(mouseEvent.getX(), mouseEvent.getY());
//        context.stroke();
//        context.closePath();
//    }
//
//    private void writeLine(MouseEvent mouseEvent) {
//        System.out.println("writing line at X:" + mouseEvent.getX() + "  Y: " + mouseEvent.getY());
//        context.lineTo(mouseEvent.getX(), mouseEvent.getY());
//        context.stroke();
//    }
//
//    private void startLine(MouseEvent mouseEvent) {
//        System.out.println("Starting line at X:" + mouseEvent.getX() + "  Y: " + mouseEvent.getY());
//        context.beginPath();
//        context.lineTo(mouseEvent.getX(), mouseEvent.getY());
//        context.stroke();
//    }



    private void changeEffect(SpecialEffect specialEffect) {
        System.out.println("Setting special effects to " + specialEffect.lineEffect);
        context.setEffect(specialEffect.lineEffect);
    }

    private void changeLineSize(Double size) {
        System.out.println("Setting line size to " + size);
        context.setLineWidth(size);
    }

    private void changeColor(Color color) {
        System.out.println("Changing color");
        context.setFill(color);
    }

    private void clearCanvas(ActionEvent actionEvent) {
        System.out.println("Clearing canvas");
        context.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void deletingLine(ActionEvent actionEvent) {
        System.out.println("Deleting line");
    }



}
