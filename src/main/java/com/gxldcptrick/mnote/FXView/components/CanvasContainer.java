package com.gxldcptrick.mnote.FXView.components;

import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.gxldcptrick.mnote.FXView.controllers.CanvasDrawingController;
import com.gxldcptrick.mnote.commonLib.Event;
import com.gxldcptrick.mnote.commonLib.EventArgs;
import com.gxldcptrick.mnote.commonLib.EventListener;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class CanvasContainer implements Serializable {
    private static final long serialVersionUID;

    static {
        serialVersionUID = generateID("420 BLAZE IT");
    }

    private static long generateID(String input) {
        long counter = 0;
        char[] inputs = input.toCharArray();
        for (int i =  0; i < inputs.length; i++) {
            counter += inputs[i] + i;
        }
        return counter;
    }
    private CanvasToolbar toolbar;
    private double width;
    private double height;
    private CanvasDrawingController canvasController;
    private transient DrawingBoard whiteBoard;
    private transient VBox layout;

    public CanvasContainer(double width, double height) {
        this.width = width;
        this.height = height;
        initialize(width, height);
    }

    public Pane getLayoutNode() {
        return this.layout;
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        initialize(this.width, this.height);
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    private void initialize(double width, double height) {
        initializeLayout();
        initializeBoard(width, height);
        initializeToolbar();
        layout.getChildren().addAll(toolbar.getLayout(), this.whiteBoard);
        this.canvasController = new CanvasDrawingController(this.whiteBoard);
    }

    private void initializeBoard(double width, double height) {
        if (whiteBoard == null) this.whiteBoard = new DrawingBoard(width, height);
    }

    private void initializeLayout() {
        if (layout == null) layout = new VBox();
        layout.setSpacing(5);
    }

    private void initializeToolbar() {
        if (toolbar == null) toolbar = new CanvasToolbar(this.canvasController.getBrush());
        MenuItem clearCanvas = new MenuItem("Whiteboard");
        MenuItem clearDrawing = new MenuItem("Drawings");
        MenuItem clearAnnotations = new MenuItem("Annotations");
        toolbar.getContextMenu().getItems().addAll(clearCanvas, clearDrawing, clearAnnotations);
        clearCanvas.setOnAction(event -> {
            this.canvasController.clearNotes();
            this.canvasController.clearLines();
            this.whiteBoard.clearDrawings();
        });
        clearDrawing.setOnAction(event -> whiteBoard.clearDrawings());
        clearAnnotations.setOnAction(event -> whiteBoard.clearGroup());
    }

    public RenderedImage getRenderedImage() {
        return whiteBoard.captureImage();
    }
}
