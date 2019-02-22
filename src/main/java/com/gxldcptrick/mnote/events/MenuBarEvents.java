package com.gxldcptrick.mnote.events;

public class MenuBarEvents {
    private MenuBarEvents() {}

    private static final MenuBarEvents instance = new MenuBarEvents();

    public static MenuBarEvents getInstance(){
        return instance;
    }
}
