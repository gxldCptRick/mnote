package com.gxldcptrick.mnote.models;

import java.io.Serializable;

import javafx.scene.paint.Color;

public class SavableColor implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 63636211L;
	private double red;
	private double green;
	private double blue;
	private double opacity;

	public SavableColor(Color saveData) {
		this(saveData.getRed(), saveData.getGreen(), saveData.getBlue(), saveData.getOpacity());
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

	public Color getColor() {
		return new Color(red, green, blue, opacity);
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
