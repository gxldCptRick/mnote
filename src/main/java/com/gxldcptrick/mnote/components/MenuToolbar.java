package com.gxldcptrick.mnote.components;

import com.gxldcptrick.mnote.events.RethinkEvents;
import com.gxldcptrick.mnote.events.MenuBarEvents;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;

public class MenuToolbar extends HBox {
    private MenuBar menuBar;
    private Menu file;
    private Menu network;
    private Label sessionId;

    public MenuToolbar(){
        menuBar = new MenuBar();
        sessionId = new Label("Not online");
        sessionId.setStyle("-fx-font: 14 arial;");
        setUpFileMenu();
        setUpNetworkMenu();
        setSpacing(5);
        getChildren().addAll(menuBar, sessionId);
    }

    private void setUpFileMenu() {
        file = new Menu("File");
        file.getItems().addAll(createFileMenuItems());
        menuBar.getMenus().add(file);
    }

    private List<MenuItem> createFileMenuItems() {
        var items = new ArrayList<MenuItem>();
        items.add(new MenuItem("Save"));
        items.add(new MenuItem("Save As"));
        items.add(new MenuItem("Load Note"));
        items.add(new MenuItem("New Note"));
        items.add(new MenuItem("Export As Png"));

        return items;
    }

    private void setUpNetworkMenu() {
        network = new Menu("Network");
        network.getItems().addAll(createNetworkMenuItems());
        menuBar.getMenus().add(network);
    }

    private List<MenuItem> createNetworkMenuItems() {
        var items = new ArrayList<MenuItem>();
        var startSession = new MenuItem("Start Session");
        var joinSession = new MenuItem(("Join Session"));
        var endSession = new MenuItem("End Session");
        items.add(startSession);
        items.add(joinSession);
        items.add(endSession);
        endSession.setDisable(true);

        JavaFxObservable.actionEventsOf(startSession)
                .subscribe(MenuBarEvents.getInstance().getStartSession());

        JavaFxObservable.actionEventsOf(joinSession)
                .subscribe(MenuBarEvents.getInstance().getJoinSession());

        JavaFxObservable.actionEventsOf(endSession)
                .subscribe(MenuBarEvents.getInstance().getEndSession());

        RethinkEvents.getInstance().getOnline()
                .subscribe(online -> {
                    startSession.setDisable(online);
                    endSession.setDisable(!online);
                    joinSession.setDisable(online);
                });

        RethinkEvents.getInstance().getSessionId()
                .map(sessionId -> {
                    if (sessionId.equals(""))
                        return "Not online";
                    else
                        return "Session id: " + sessionId;
                })
                .subscribe(sessionId::setText);

        return items;
    }
}
