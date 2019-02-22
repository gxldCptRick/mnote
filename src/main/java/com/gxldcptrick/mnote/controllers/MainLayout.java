package com.gxldcptrick.mnote.controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MainLayout {
    @FXML
    public MenuBar menuBar;
    public HBox canvasToolBar;
    public ScrollPane scrollPane;
    public AnchorPane anchorPane;
    public VBox mainLayout;

    private Canvas canvas;
    GraphicsContext graphicsContext;

    @FXML
    public void initialize(){
        canvas = new Canvas(1080, 1920);
         graphicsContext = canvas.getGraphicsContext2D();
        scrollPane.setContent(canvas);

        bla();
    }

    private void bla() {
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED,
                event -> {
                    System.out.println("Starting line");
                    graphicsContext.beginPath();
                    graphicsContext.moveTo(event.getX(), event.getY());
                    graphicsContext.stroke();
                });

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                event -> {
                    System.out.println("Dragging");
                    graphicsContext.lineTo(event.getX(), event.getY());
                    graphicsContext.stroke();
                });

        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED,
                event -> {
                    System.out.println("Ending line");

                });
    }
}
