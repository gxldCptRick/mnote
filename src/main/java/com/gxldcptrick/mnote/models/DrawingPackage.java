package com.gxldcptrick.mnote.models;

import java.util.List;

public class DrawingPackage {
    private Brush brush;

    private String clientId;

    private List<Coordinate> coordinates;

    private String id;

    public Brush getBrush() {
        return brush;
    }

    public void setBrush(Brush brush) {
        this.brush = brush;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public List<Coordinate> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Coordinate> coordinates) {
        this.coordinates = coordinates;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "DrawingPackage{" +
                "brush=" + brush +
                ", clientId='" + clientId + '\'' +
                ", coordinates=" + coordinates +
                ", id='" + id + '\'' +
                '}';
    }
}