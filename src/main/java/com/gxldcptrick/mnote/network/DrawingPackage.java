package com.gxldcptrick.mnote.network;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gxldcptrick.mnote.FXView.enums.PointType;
import com.gxldcptrick.mnote.FXView.models.Brush;
import com.gxldcptrick.mnote.FXView.models.SavablePoint2D;

import java.io.Serializable;
import java.util.Objects;

public class DrawingPackage {
    private SavablePoint2D point2d;
    private Brush brush;
    private PointType type;
    private String ClientUUID;

    public DrawingPackage() { }
    public DrawingPackage(SavablePoint2D point2d, Brush brush, PointType pointType) {
        this.setPoint2d(point2d);
        this.setBrush(brush);
        this.setType(pointType);
    }

    public boolean equals(DrawingPackage otherPackage){
        var isEquals = false;
        if(otherPackage != null){
            isEquals = getType().equals(otherPackage.getType()) &&
                    getBrush().equals(otherPackage.getBrush()) &&
                    getPoint2d().equals(otherPackage.getPoint2d());
        }
        return isEquals;
    }

    @Override
    public boolean equals(Object o) {
        var isEquals = false;
        if (this == o) isEquals = true;
        else if (o != null && getClass() == o.getClass()) isEquals = equals((DrawingPackage) o);
        return isEquals;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPoint2d(), getType(), getBrush());
    }

    public void setClientUUID(String clientUUID) {
        if (clientUUID != null)
            ClientUUID = clientUUID;
    }

    public void setBrush(Brush brush) {
        if (brush != null)
            this.brush = brush;
    }

    public void setPoint2d(SavablePoint2D point2d) {
        if (point2d != null)
            this.point2d = point2d;
    }

    @JsonProperty("point")
    public SavablePoint2D getPoint2d() {
        return point2d;
    }
    public Brush getBrush() { return brush; }
    public PointType getType() {
        return type;
    }
    @JsonProperty("client_id")
    public String getClientUUID() {
        return ClientUUID;
    }
    public void setType(PointType type) {
        if (type != null)
            this.type = type;
    }
}
