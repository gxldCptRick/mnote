package com.gxldcptrick.mnote.FXView.components;

import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.gxldcptrick.mnote.FXView.controllers.CanvasDrawingController;
import com.gxldcptrick.mnote.FXView.events.EventHolder;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class CanvasContainer {
    private CanvasToolbar toolbar;
    private DrawingBoard whiteBoard;
    private VBox layout;

    public CanvasContainer(double width, double height, EventHolder holder) {
        setupLayout();
        setupToolbar(holder);
        setupDrawingBoard(width, height, holder);
    }

    private void setupToolbar(EventHolder holder) {
        toolbar = new CanvasToolbar(holder);
        layout.getChildren().add(toolbar.getLayout());
    }

    private void setupLayout() {
        layout = new VBox();
        layout.setSpacing(5);
    }

    public Pane getLayoutNode() {
        return this.layout;
    }

    private void setupDrawingBoard(double width, double height, EventHolder holder) {
        layout.getChildren().addAll(toolbar.getLayout(), this.whiteBoard);
        whiteBoard = new DrawingBoard(width, height, holder);
        layout.getChildren().add(whiteBoard);
    }

    public RenderedImage getRenderedImage() {
        return whiteBoard.captureImage();
    }
}
