package com.gxldcptrick.mnote.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.gxldcptrick.mnote.enums.SpecialEffect;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;

@JsonDeserialize(using = BrushDeserializer.class)
public class Brush {
    private double currentWidth;
    private StrokeLineCap brushCap;
    private SpecialEffect effect;
    private Color color;

    public Brush(){
        this.currentWidth = 1.0;
        this.effect = SpecialEffect.None;
        this.color = Color.BLACK;
    }

    public double getCurrentWidth() {
        return currentWidth;
    }

    public void setCurrentWidth(double currentWidth) {
        this.currentWidth = currentWidth;
    }

    public StrokeLineCap getBrushCap() {
        return brushCap;
    }

    public void setBrushCap(StrokeLineCap brushCap) {
        this.brushCap = brushCap;
    }

    public SpecialEffect getEffect() {
        return effect;
    }

    public void setEffect(SpecialEffect effect) {
        this.effect = effect;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Width: " + currentWidth + "\n" +
                "Effect: " + effect.lineEffect + "\n" +
                "Color: " + color.toString() + "\n";
    }
}
