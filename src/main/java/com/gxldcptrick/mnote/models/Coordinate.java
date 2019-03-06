package com.gxldcptrick.mnote.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Coordinate {
    @JsonProperty
    private int x;

    @JsonProperty
    private int y;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
