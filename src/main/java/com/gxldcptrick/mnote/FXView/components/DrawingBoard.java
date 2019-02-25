package com.gxldcptrick.mnote.FXView.components;

import java.awt.image.RenderedImage;

import com.gxldcptrick.mnote.FXView.events.EventHolder;
import com.gxldcptrick.mnote.FXView.models.*;
import com.gxldcptrick.mnote.FXView.enums.SpecialEffect;

import com.gxldcptrick.mnote.commonLib.Delegate;
import com.gxldcptrick.mnote.commonLib.EventArgs;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.canvas.*;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;

public class DrawingBoard extends ScrollPane {
    private transient Group noteSurface;
    private transient Canvas drawSurface;

    private Delegate<MouseEventArgs> canvasMouseDragDelegate;
    private Delegate<MouseEventArgs> canvasMouseDownDelegate;
    private Delegate<MouseEventArgs> canvasMouseUpDelegate;
    private Delegate<MouseEventArgs> canvasClickedDelegate;
    private Delegate<MouseEventArgs> noteDoubleClickedDelegate;


    public DrawingBoard(double width, double height, final EventHolder holder) {
        this.noteSurface = new Group();
        this.initialize(width, height);
        this.createInitialEvents();
        this.addEventsToRepo(holder);
        this.connectToClearEvents(holder);
    }

    private void connectToClearEvents(final EventHolder holder) {
        holder.getEmptyEvents().subscribeToEvent(this::clearDrawings, "Canvas Cleared");
        holder.getEmptyEvents().subscribeToEvent(this::clearGroup, "Notes Cleared");
    }

    private void addEventsToRepo(final EventHolder holder) {
        holder.getMouseEvents().addEventToRepo(this.canvasClickedDelegate, "Canvas Mouse Clicked");
        holder.getMouseEvents().addEventToRepo(this.canvasMouseDownDelegate, "Canvas Mouse Down");
        holder.getMouseEvents().addEventToRepo(this.canvasMouseUpDelegate, "Canvas Mouse Up");
        holder.getMouseEvents().addEventToRepo(this.canvasMouseDragDelegate, "Canvas Mouse Drag");
        holder.getMouseEvents().addEventToRepo(this.noteDoubleClickedDelegate, "Note Clicked");
    }

    private void createInitialEvents(){
        this.canvasClickedDelegate = new Delegate<>();
        this.canvasMouseDownDelegate = new Delegate<>();
        this.canvasMouseDragDelegate = new Delegate<>();
        this.canvasMouseUpDelegate = new Delegate<>();
        this.noteDoubleClickedDelegate = new Delegate<>();
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
        RenderedImage img;
        img = SwingFXUtils.fromFXImage(image, null);
        return img;
    }

    public void clearGroup(Object sender, EventArgs e){
        if(e != null && sender != null){
            this.noteSurface.getChildren().clear();
        }
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
        this.drawSurface.setOnMouseDragged((e) -> this.canvasMouseDragDelegate.invoke(this.drawSurface, new MouseEventArgs(e)));
        this.drawSurface.setOnMouseReleased((e) -> this.canvasMouseUpDelegate.invoke(this.drawSurface, new MouseEventArgs(e)));
        this.drawSurface.setOnMousePressed((e) -> this.canvasMouseDownDelegate.invoke(this.drawSurface, new MouseEventArgs(e)));
        this.noteSurface.setOnMouseClicked((e) -> this.noteDoubleClickedDelegate.invoke(this.noteSurface, new MouseEventArgs(e)));
    }

    private void setupCanvasClickEvents() {
        this.drawSurface.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2){
                this.canvasClickedDelegate.invoke(this.drawSurface, new MouseEventArgs(event));
            }
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

    private void clearDrawings(Object sender, EventArgs e) {
        if(e != null && sender != null){
            GraphicsContext gc = drawSurface.getGraphicsContext2D();
            gc.setEffect(SpecialEffect.None.lineEffect);
            gc.clearRect(0, 0, drawSurface.getWidth(), drawSurface.getHeight());
        }
    }

    private double calculateJump(double currentSize, double sizeOfView, double sizeOfObject, double direction) {
        return currentSize + (sizeOfView / sizeOfObject) * .1 * direction;
    }
}