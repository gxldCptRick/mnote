package com.gxldcptrick.mnote.controllers;

import com.gxldcptrick.mnote.commonLib.JavaFXEvents;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MainLayout {
    @FXML
    public MenuBar menuBar;
    public HBox canvasToolBar;
    public Canvas canvas;
    public ScrollPane scrollPane;
    public AnchorPane anchorPane;
    public VBox mainLayout;

    @FXML
    public void initialize(){

        JavaFXEvents.getInstance().getMouseEvents().subscribe(actionEvent -> {
            System.out.println(actionEvent.getEventType());
            System.out.println(actionEvent.getX() + " " + actionEvent.getY());
        },
                (e) -> {
                    System.out.println(e.getMessage());
                });
    }
}
