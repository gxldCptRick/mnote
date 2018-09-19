package com.gxldcptrick.mnote.FXView.models;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.scene.paint.Color;

public class SavableColor implements Serializable{
	private static final long serialVersionUID = 63636211L;
	private double red;
	private double green;
	private double blue;
	private double opacity;
    private transient Color color;
    public SavableColor(){
        this(0,0,0,0);
    }
	public SavableColor(double red, double green, double blue) {
		this(red, green, blue, 1);
	}
	public SavableColor(double red, double green, double blue, double opacity) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.opacity = opacity;
	}


    public double getOpacity() {
        return opacity;
    }

    public void setOpacity(double opacity) {
        this.opacity = opacity;
    }

    public double getBlue() {

        return blue;
    }

    public void setBlue(double blue) {
        this.blue = blue;
    }

    public double getRed() {

        return red;
    }

    public void setRed(double red) {
        this.red = red;
    }

    public double getGreen() {

        return green;
    }

    public void setGreen(double green) {
        this.green = green;
    }

	@JsonIgnore
	public Color getColor()
	{
		if(color == null) color = new Color(red, green, blue, opacity);
		return color;
	}
	
	public boolean equals(SavableColor color) {
		return color != null && color.red == this.red && color.blue == this.blue && color.green == this.green && color.opacity == this.opacity;
	}
	
	@Override
	public boolean equals(Object other) {
		boolean equal = false;
		
		if(getClass().isInstance(other)) {
			equal = equals(getClass().cast(other));
		}

		return equal;
	}
	
	@Override
	public int hashCode() {
		return Double.hashCode(red) ^ Double.hashCode(blue) ^ Double.hashCode(green);
	}
}
