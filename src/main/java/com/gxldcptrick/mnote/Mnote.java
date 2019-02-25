package com.gxldcptrick.mnote;

import com.gxldcptrick.mnote.components.MnoteLayout;
import com.gxldcptrick.mnote.events.ClientEvents;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.awt.*;

public class Mnote extends Application {

    private final static double Screen_Width;
    private final static double Screen_Height;

    static {
        Screen_Width = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        Screen_Height = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    }

    @Override
    public void start(Stage stage) {
        JavaFxObservable.eventsOf(stage, WindowEvent.WINDOW_CLOSE_REQUEST)
                .subscribe(ClientEvents.getInstance().getDisconnect());

        Scene scene = new Scene(new MnoteLayout(), 1500, 500);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
