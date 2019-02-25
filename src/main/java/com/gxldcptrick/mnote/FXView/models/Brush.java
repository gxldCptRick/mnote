package com.gxldcptrick.mnote.FXView.models;

import com.gxldcptrick.mnote.FXView.enums.SpecialEffect;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Objects;

public class Brush {
    private double currentWidth;
    private StrokeLineCap brushCap;
    private boolean isDeleting;
    private SpecialEffect effect;
    private  SavableColor currentColor;

    public Brush(){
        this(.5, StrokeLineCap.ROUND, false, SpecialEffect.None, Color.BLACK);
    }

    public Brush(double currentWidth, StrokeLineCap brushCap, boolean isDeleting, SpecialEffect effect, Color currentColor){
        this.setCurrentWidth(currentWidth);
        this.setBrushCap(brushCap);
        this.setDeleting(isDeleting);
        this.setEffect(effect);
        this.setCurrentColor(new SavableColor(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), currentColor.getOpacity()));
    }

    public Brush(Brush brush) {
        Objects.requireNonNull(brush, "brush cannot be null");
        this.setBrushCap(brush.getBrushCap());
        this.setCurrentWidth(brush.getCurrentWidth());
        this.setCurrentColor(brush.getCurrentColor());
        this.setDeleting(brush.isDeleting);
        this.setEffect(brush.getEffect());
    }

    public boolean equals(Brush otherBrush){
        var isEqual = false;
        if(otherBrush != null){
            isEqual = Double.compare(otherBrush.getCurrentWidth(), getCurrentWidth()) == 0 &&
                    isDeleting() == otherBrush.isDeleting() &&
                    getBrushCap() == otherBrush.getBrushCap() &&
                    getEffect() == otherBrush.getEffect() &&
                    Objects.equals(getCurrentColor(), otherBrush.getCurrentColor());
        }
        return isEqual;
    }

    @Override
    public boolean equals(Object o) {
        var isEqual = false;
        if (this == o) isEqual =  true;
        else if (o != null && getClass() == o.getClass()) isEqual = equals((Brush) o);

        return isEqual;
    }

    @Override
    public int hashCode() {

        return Objects.hash(getCurrentWidth(), getBrushCap(), isDeleting(), getEffect(), getCurrentColor());
    }

    public SavableColor getCurrentColor() {
        return currentColor;
    }

    public void setCurrentColor(SavableColor currentColor) {
        this.currentColor = currentColor;
    }

    public boolean isDeleting() {
        return isDeleting;
    }

    public void setDeleting(boolean deleting) {
        isDeleting = deleting;
    }

    public SpecialEffect getEffect() {
        return effect;
    }

    public void setEffect(SpecialEffect effect) {
        this.effect = effect;
    }

    public StrokeLineCap getBrushCap() {
        return brushCap;
    }

    public void setBrushCap(StrokeLineCap brushCap) {
        this.brushCap = brushCap;
    }

    public double getCurrentWidth() {
        return currentWidth;
    }

    public void setCurrentWidth(double currentWidth) {
        this.currentWidth = currentWidth;
    }


    @Override
    public String toString(){
        return String.format("color: %s, width: %s, effect: %s, cap: %s",
                this.getCurrentColor().getColor(),
                this.getCurrentWidth(),
                this.getEffect(),
                this.getBrushCap());
    }
}
