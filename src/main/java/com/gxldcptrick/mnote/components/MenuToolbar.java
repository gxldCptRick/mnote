package com.gxldcptrick.mnote.components;

import com.gxldcptrick.mnote.events.ClientEvents;
import com.gxldcptrick.mnote.events.MenuBarEvents;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
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

    public MenuToolbar(){
        menuBar = new MenuBar();
        setUpFileMenu();
        setUpNetworkMenu();
        setSpacing(5);
        getChildren().add(menuBar);
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
        var endSession = new MenuItem("End Session");
        var joinSession = new MenuItem(("Join Session"));
        items.add(startSession);
        items.add(endSession);
        items.add(joinSession);
        endSession.setDisable(true);

        JavaFxObservable.actionEventsOf(startSession)
                .subscribe(MenuBarEvents.getInstance().getStartSession());

        JavaFxObservable.actionEventsOf(joinSession)
                .subscribe(MenuBarEvents.getInstance().getJoinSession());

        JavaFxObservable.actionEventsOf(endSession)
                .subscribe(MenuBarEvents.getInstance().getEndSession());

        ClientEvents.getInstance().getOnline()
                .subscribe(online -> {
                    startSession.setDisable(online);
                    endSession.setDisable(!online);
                });


        return items;
    }
}
