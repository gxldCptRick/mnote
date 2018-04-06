package com.gxldcptrick.mnote.models;

import com.gxldcptrick.mnote.models.enums.SpecialEffect;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Brush implements Serializable {

    private static long serialVersionUID = -7915164485747229844l;
    private double currentWidth;
    private StrokeLineCap brushCap;
    private boolean isDeleting;
    private SpecialEffect effect;
    private  transient Color currentColor;

    public Brush(){

        currentWidth = .5;
        brushCap = StrokeLineCap.ROUND;
        currentColor = Color.BLACK;

    }

    //<editor-fold desc = "Getters and Setters">

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

    public boolean isSpecial() {
        return effect != null;
    }

    public Color getCurrentColor() {
        return currentColor;
    }

    public void setCurrentColor(Color currentColor) {
        this.currentColor = currentColor;
    }

    public double getCurrentWidth() {
        return currentWidth;
    }

    public void setCurrentWidth(double currentWidth) {
        this.currentWidth = currentWidth;
    }

    //</editor-fold>

   // <editor-fold desc = "Serilization">
    private void writeObject(ObjectOutputStream oos)throws IOException {

        oos.defaultWriteObject();
        oos.writeObject(new SavableColor(this.currentColor));

    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException{

        ois.defaultReadObject();
        this.currentColor = ((SavableColor) ois.readObject()).getColor();
        System.out.println(this.currentColor);

    }

    public void resetWithBrush(Brush brush) {

        this.currentColor = brush.currentColor;
        this.currentWidth = brush.currentWidth;
        this.effect = brush.effect;
        this.brushCap = brush.brushCap;
        this.isDeleting = brush.isDeleting;
    }
    //</editor-fold>

    @Override
    public String toString(){

        String message = "";

        message += isDeleting? "Deleting " : "Not Deleting ";

        message += this.brushCap.toString() + " ";

        message += this.currentColor + " ";

        message += "Width: " + this.currentWidth + " ";

        message += this.effect + " ";


        return message;
    }
}
