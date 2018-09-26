package com.gxldcptrick.mnote.FXView.controllers;

import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.gxldcptrick.mnote.FXView.views.CanvasToolbar;

import com.gxldcptrick.mnote.FXView.views.DrawingBoard;
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
    private transient DrawingBoard whiteBoard;
    private transient VBox layout;

     public CanvasContainer(double width, double height) {
        this.width = width;
        this.height = height;
        initialize(width, height);
    }

    public Pane getLayout() {
        return this.layout;
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        initialize(this.width, this.height);
        whiteBoard.reloadData(in);
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        this.whiteBoard.saveData(out);
    }

    private void initialize(double width, double height) {
        initializeLayout();
        initializeBoard(width, height);
        initializeToolbar();
        layout.getChildren().addAll(toolbar.getLayout(), this.whiteBoard);
    }

    private void initializeBoard(double width, double height) {
        if (whiteBoard == null) this.whiteBoard = new DrawingBoard(width, height);
    }

    private void initializeLayout() {
        if (layout == null) layout = new VBox();
        layout.setSpacing(5);
    }

    private void initializeToolbar() {
        if (toolbar == null) toolbar = new CanvasToolbar(this.whiteBoard.getCanvasBrush());
        MenuItem clearCanvas = new MenuItem("Whiteboard");
        MenuItem clearDrawing = new MenuItem("Drawings");
        MenuItem clearAnnotations = new MenuItem("Annotations");
        toolbar.getContextMenu().getItems().addAll(clearCanvas, clearDrawing, clearAnnotations);
        clearCanvas.setOnAction(event -> whiteBoard.clearBoard());
        clearDrawing.setOnAction(event -> whiteBoard.clearLines());
        clearAnnotations.setOnAction(event -> whiteBoard.clearAnnotations());
    }

    public RenderedImage getRenderedImage() {
        return whiteBoard.captureImage();
    }
}
