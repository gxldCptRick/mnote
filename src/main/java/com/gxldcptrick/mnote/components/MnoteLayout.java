package com.gxldcptrick.mnote.components;

import com.gxldcptrick.mnote.events.ClientEvents;
import com.gxldcptrick.mnote.events.MenuBarEvents;
import com.gxldcptrick.mnote.models.User;
import com.gxldcptrick.mnote.network.Rethink;
import javafx.event.ActionEvent;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.Optional;

public class MnoteLayout extends VBox {
    private MenuToolbar menuToolbar;
    private CanvasToolbar canvasToolbar;
    private ScrollableCanvas canvasScrollPane;
    private Pane pane;
    private User user;
    private boolean online;
    private Rethink rethink;

    public MnoteLayout(){
        canvasScrollPane = new ScrollableCanvas();
        canvasToolbar = new CanvasToolbar();
        menuToolbar = new MenuToolbar();
        user = new User();
        online = false;

        getChildren().addAll(menuToolbar, canvasToolbar, canvasScrollPane);

        setUpMenuToolbarEvents();
    }

    private void setUpMenuToolbarEvents() {
        MenuBarEvents.getInstance().getStartSession()
                .subscribe(this::startSession);

        MenuBarEvents.getInstance().getJoinSession()
                .subscribe(this::joinSession);

        MenuBarEvents.getInstance().getEndSession()
                .subscribe(this::endSession);
    }

    private void startSession(ActionEvent actionEvent) {
        var textInput = new TextInputDialog("127.0.0.1");
        textInput.setTitle("Enter RethinkDB host");
        textInput.setContentText("Enter RethinkDB host");
        Optional result = textInput.showAndWait();
        if (result != Optional.empty()){
            rethink = new Rethink(textInput.getEditor().getText(), user.getUuid());
            if (rethink.connectedToRethink())
                rethink.start();
        }
    }

    private void endSession(ActionEvent actionEvent) {
        System.out.println("Ending session");
        ClientEvents.getInstance().getDisconnectFromChanges().onNext(true);
        rethink.close();
    }

    private void joinSession(ActionEvent actionEvent) {
    }
}
