package com.gxldcptrick.mnote.components;

import com.gxldcptrick.mnote.events.CanvasEvents;
import com.gxldcptrick.mnote.events.CanvasToolbarEvents;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.Effect;
import javafx.scene.input.MouseEvent;

public class ScrollableCanvas extends ScrollPane {
    private Canvas canvas;
    private GraphicsContext context;
    private Effect currentEffect;

    public ScrollableCanvas(){
        canvas = new Canvas(1500, 500);
        context = canvas.getGraphicsContext2D();

        setUpMouseEvents();
        subscribeToCanvasToolbarEvents();
        setContent(canvas);
    }

    @SuppressWarnings("Duplicates")
    private void setUpMouseEvents() {
        JavaFxObservable.eventsOf(canvas, MouseEvent.MOUSE_PRESSED)
                .subscribe(CanvasEvents.getInstance().getMouseDownEvents());

        JavaFxObservable.eventsOf(canvas, MouseEvent.MOUSE_DRAGGED)
                .subscribe(CanvasEvents.getInstance().getMouseDragEvents());

        JavaFxObservable.eventsOf(canvas, MouseEvent.MOUSE_RELEASED)
                .subscribe(CanvasEvents.getInstance().getMouseUpEvents());

        CanvasEvents.getInstance().getMouseDownEvents().subscribe(this::startLine);
        CanvasEvents.getInstance().getMouseDragEvents().subscribe(this::writeLine);
        CanvasEvents.getInstance().getMouseUpEvents().subscribe(this::endLine);
    }

    private void endLine(MouseEvent mouseEvent) {
        System.out.println("Ending line at X:" + mouseEvent.getX() + "  Y: " + mouseEvent.getY());
        context.lineTo(mouseEvent.getX(), mouseEvent.getY());
        context.stroke();
        context.closePath();
    }

    private void writeLine(MouseEvent mouseEvent) {
        System.out.println("writing line at X:" + mouseEvent.getX() + "  Y: " + mouseEvent.getY());
        context.lineTo(mouseEvent.getX(), mouseEvent.getY());
        context.stroke();
    }

    private void startLine(MouseEvent mouseEvent) {
        System.out.println("Starting line at X:" + mouseEvent.getX() + "  Y: " + mouseEvent.getY());
        context.beginPath();
        context.lineTo(mouseEvent.getX(), mouseEvent.getY());
        context.stroke();
    }

    private void subscribeToCanvasToolbarEvents() {
        CanvasToolbarEvents.getInstance().getChangedColor().subscribe(context::setStroke);
        CanvasToolbarEvents.getInstance().getChangedLineSize().subscribe(context::setLineWidth);
        CanvasToolbarEvents.getInstance().getDeletingLine().subscribe(actionEvent -> System.out.println("Need to make this work!!!! "));
        CanvasToolbarEvents.getInstance().getChangeSpecialEfects().subscribe(specialEffect ->{
                    context.setEffect(specialEffect.lineEffect);
                    currentEffect = specialEffect.lineEffect;
                }
        );
        CanvasToolbarEvents.getInstance().getClearCanvas()
                .subscribe(ae -> {
                    context.setEffect(null);
                    context.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                    context.setEffect(currentEffect);
                });
    }
}
