package com.gxldcptrick.mnote;

import com.gxldcptrick.mnote.components.MnoteLayout;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Mnote extends Application {

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new MnoteLayout(), 1500, 500);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
