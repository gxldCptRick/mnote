package com.gxldcptrick.mnote.FXView.components;

import java.awt.image.RenderedImage;

import com.gxldcptrick.mnote.FXView.models.*;
import com.gxldcptrick.mnote.FXView.enums.SpecialEffect;

import com.gxldcptrick.mnote.commonLib.Event;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.canvas.*;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;

public class DrawingBoard extends ScrollPane {
    private transient Group noteSurface;
    private transient Canvas drawSurface;
    public final Event<MouseEventArgs> canvasMouseDragEvent;
    public final Event<MouseEventArgs> canvasMouseDownEvent;
    public final Event<MouseEventArgs> canvasMouseUpEvent;
    public final Event<MouseEventArgs> canvasClickedEvent;
    public final Event<MouseEventArgs> noteDoubleClickedEvent;
    public DrawingBoard(double width, double height) {
        this.initialize(width, height);
        this.noteSurface = new Group();
        this.canvasMouseDownEvent = new Event<>();
        this.canvasMouseDragEvent = new Event<>();
        this.canvasMouseUpEvent = new Event<>();
        this.canvasClickedEvent = new Event<>();
        this.noteDoubleClickedEvent = new Event<>();
    }

    public Canvas getDrawSurface(){
        return this.drawSurface;
    }

    public Group getNoteSurface(){
        return this.noteSurface;
    }
    public RenderedImage captureImage() {
        WritableImage image = new WritableImage((int) drawSurface.getWidth(), (int) drawSurface.getHeight());
        this.snapshot(null, image);
        RenderedImage img = SwingFXUtils.fromFXImage(image, null);
        return img;
    }

    public void clearGroup(){
        this.noteSurface.getChildren().clear();
    }

    private void initialize(double width, double height) {
        this.initializeCanvas(width, height);
        this.initializeLayout();
    }

    private void initializeLayout() {
        this.noteSurface.getChildren().add(drawSurface);
        this.setContent(this.noteSurface);
        setupScrollPaneMousePanEvents();
    }

    private void initializeCanvas(double width, double height) {
        this.drawSurface = new Canvas(width, height);
        setUpDrawing();
        setupCanvasClickEvents();
    }

    private void setUpDrawing() {
        this.drawSurface.setOnMouseDragged((e) -> this.canvasMouseDragEvent.invoke(this.drawSurface, new MouseEventArgs(e)));
        this.drawSurface.setOnMouseReleased((e) -> this.canvasMouseUpEvent.invoke(this.drawSurface, new MouseEventArgs(e)));
        this.drawSurface.setOnMousePressed((e) -> this.canvasMouseDownEvent.invoke(this.drawSurface, new MouseEventArgs(e)));
        this.noteSurface.setOnMouseClicked((e) -> this.noteDoubleClickedEvent.invoke(this.noteSurface, new MouseEventArgs(e)));
    }

    private void setupCanvasClickEvents() {
        this.drawSurface.setOnMouseClicked(event -> {
           this.canvasClickedEvent.invoke(this.drawSurface, new MouseEventArgs(event));
        });
    }

    private void setupScrollPaneMousePanEvents() {
        this.addEventFilter(InputEvent.ANY, (event) -> {
            if (event.getEventType().toString().equals("SCROLL"))
                event.consume();
        });
        this.setOnMousePressed(event -> {
            if (event.isSecondaryButtonDown())
                this.setPannable(true);
        });
        this.setOnMouseReleased(event -> this.setPannable(false));
        this.setOnMouseDragged(event -> {
            calculateHorizontalBound(event);
            calculateVerticalBounds(event);
        });
    }

    private void calculateVerticalBounds(MouseEvent event) {
        if (event.getY() + 10 > this.getViewportBounds().getHeight()) {
            this.setVvalue(
                    calculateJump(this.getVvalue(), this.getViewportBounds().getHeight(), drawSurface.getHeight(), 1));
        } else if (event.getY() < 10) {
            this.setVvalue(
                    calculateJump(this.getVvalue(), this.getViewportBounds().getHeight(), drawSurface.getHeight(), -1));
        }
    }

    private void calculateHorizontalBound(MouseEvent event) {
        if (event.getX() + 100 > this.getWidth()) {
            this.setHvalue(
                    calculateJump(this.getHvalue(), this.getViewportBounds().getWidth(), drawSurface.getWidth(), 1));
        } else if (event.getX() < 10) {
            this.setHvalue(
                    calculateJump(this.getHvalue(), this.getViewportBounds().getWidth(), drawSurface.getWidth(), -1));
        }
    }

    public void clearDrawings() {
        GraphicsContext gc = drawSurface.getGraphicsContext2D();
        gc.setEffect(SpecialEffect.None.lineEffect);
        gc.clearRect(0, 0, drawSurface.getWidth(), drawSurface.getHeight());
    }

    private double calculateJump(double currentSize, double sizeOfView, double sizeOfObject, double direction) {
        return currentSize + (sizeOfView / sizeOfObject) * .1 * direction;
    }
}