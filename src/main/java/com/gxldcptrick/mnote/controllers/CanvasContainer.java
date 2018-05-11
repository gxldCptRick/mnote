package com.gxldcptrick.mnote.controllers;

import java.awt.Toolkit;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.gxldcptrick.mnote.views.CanvasToolbar;

import com.gxldcptrick.mnote.views.DrawingBoard;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class CanvasContainer implements Serializable {
    /**
     *
     */
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

    private transient DrawingBoard board;
    private transient VBox layout;


    public CanvasContainer(double width, double height) {
        initialize(width, height);
    }

    public Pane getLayout() {
        return this.layout;
    }

    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();

        initialize(Toolkit.getDefaultToolkit().getScreenSize().getWidth(),
                Toolkit.getDefaultToolkit().getScreenSize().getHeight());

        board.reloadData(in);
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        this.board.saveData(out);
    }

    private void initialize(double width, double height) {
        initializeLayout();

        initializeBoard(width, height);

        initializeToolbar();

        layout.getChildren().addAll(toolbar.getLayout(), this.board);
    }

    private void initializeBoard(double width, double height) {
        if (board == null)
            this.board = new DrawingBoard(width, height);
    }

    private void initializeLayout() {
        if (layout == null)
            layout = new VBox();

        layout.setSpacing(5);
    }

    public void killNetworkConnection(){
        this.board.killSockets();
    }

    private void initializeToolbar() {
        if (toolbar == null)
            toolbar = new CanvasToolbar(this.board.getCanvasBrush());

        MenuItem clearCanvas = new MenuItem("Whiteboard");

        MenuItem clearDrawing = new MenuItem("Drawings");

        MenuItem clearAnnotations = new MenuItem("Annotations");

        toolbar.getContextMenu().getItems().addAll(clearCanvas, clearDrawing, clearAnnotations);

        clearCanvas.setOnAction(event ->
                board.clearBoard()
        );

        clearDrawing.setOnAction(event ->
                board.clearLines()
        );

        clearAnnotations.setOnAction(event ->
                board.clearAnnotations()
        );
    }


    public RenderedImage getRenderedImage() {
        return board.captureImage();
    }
}
