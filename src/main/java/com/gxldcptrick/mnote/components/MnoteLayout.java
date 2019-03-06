package com.gxldcptrick.mnote.components;

import com.gxldcptrick.mnote.events.RethinkEvents;
import com.gxldcptrick.mnote.events.MenuBarEvents;
import com.gxldcptrick.mnote.models.DrawingPackage;
import com.gxldcptrick.mnote.network.Rethink;
import javafx.event.ActionEvent;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.Optional;
import java.util.UUID;

public class MnoteLayout extends VBox {
    private MenuToolbar menuToolbar;
    private CanvasToolbar canvasToolbar;
    private ScrollableCanvas canvasScrollPane;
    private Pane pane;
    private String user;
    private boolean online;
    private Rethink rethink;

    public MnoteLayout(){
        canvasScrollPane = new ScrollableCanvas();
        canvasToolbar = new CanvasToolbar();
        menuToolbar = new MenuToolbar();
        user = UUID.randomUUID().toString();
        online = false;

        getChildren().addAll(menuToolbar, canvasToolbar, canvasScrollPane);

        setUpMenuToolbarEvents();
    }

    private void setUpMenuToolbarEvents() {
        MenuBarEvents.getInstance().getStartSession()
                .subscribe(this::startSession, System.out::println);

        MenuBarEvents.getInstance().getJoinSession()
                .subscribe(this::joinSession);

        MenuBarEvents.getInstance().getEndSession()
                .subscribe(this::endSession);
    }

    private void startSession(ActionEvent actionEvent) {
        var textInput = new TextInputDialog("Start session");
        textInput.setTitle("Enter Session Id");
        textInput.setContentText("Enter Session Id");
        Optional result = textInput.showAndWait();
        System.out.println(result);
        if (result != Optional.empty()){
            rethink = new Rethink(textInput.getEditor().getText(), user);
            if (rethink.connectedToRethink()){
                rethink.startSession();
            }
        }
    }

    private void endSession(ActionEvent actionEvent) {
        System.out.println("Ending session");
        RethinkEvents.getInstance().getDisconnectFromChanges().onNext(true);
        rethink.close();
    }

    private void joinSession(ActionEvent actionEvent) {
        System.out.println("Joining session");
        var textInput = new TextInputDialog("Session Id");
        textInput.setHeaderText("Enter session Id");
        textInput.setContentText("Enter session Id");
        Optional result = textInput.showAndWait();
        if (result != Optional.empty()){
            rethink = new Rethink(textInput.getEditor().getText(), user);
            if (rethink.connectedToRethink())
                rethink.joinSession();
        }
    }
}
