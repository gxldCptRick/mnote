package com.gxldcptrick.mnote;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;

public class Main2 extends Application {

    private final static double Screen_Width;
    private final static double Screen_Height;

    static {
        Screen_Width = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        Screen_Height = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    }


    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/MainLayout.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/styleSheet.css").toExternalForm());
        stage.setTitle("MNote");
        stage.sizeToScene();
        stage.setScene(scene);
        stage.show();
    }
}
