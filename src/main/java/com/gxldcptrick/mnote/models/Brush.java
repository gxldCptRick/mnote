package com.gxldcptrick.mnote.models;

import com.gxldcptrick.mnote.enums.SpecialEffect;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;

public class Brush {
    private static double currentWidth;
    private static StrokeLineCap brushCap;
    private static SpecialEffect effect;
    private static Color color;

    private Brush(){
    }

    private static Brush brush;

    public static Brush getInstance(){
        if (brush == null){
            brush = new Brush();
            setUpBrushDefaults();
        }
        return brush;
    }

    private static void setUpBrushDefaults() {
        currentWidth = 1.0;
        effect = SpecialEffect.None;
        color = Color.BLACK;
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
}
