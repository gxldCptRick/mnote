package com.gxldcptrick.mnote.FXView.models;
///@@@@ "Javadoc style documentation on an entire class (class and all its methods)"
import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.geometry.Point2D;
/**
 * 
 * @author Andres Hermilo Carrera
 * @since March 3, 2018
 * @version 1.1
 * 
 * **/
public class SavablePoint2D implements Serializable {
	private static final long serialVersionUID = 6969L;

	private double x;
	private double y;

	@JsonIgnore
	private transient Point2D truePoint;
	/**
	 * takes in the starting x and starting y position and creates a point that you may be able to persists with serialization.
	 * @param x the starting x value.
	 * @param y the starting y value.
	 * 
	 * **/
	public SavablePoint2D(){
	    this(0,0);
    }
	public SavablePoint2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	@JsonProperty("yValue")
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
    @JsonProperty("xValue")
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * takes in the former point that you want to save and stores the current x value of said point to become the starting x value for the saveable copy and does the same for the y value. 
	 * @param oldPoint the old point that will be saved within the object
	 */
	public SavablePoint2D(Point2D oldPoint) {
		if (oldPoint != null) {
			x = oldPoint.getX();
			y = oldPoint.getY();
		}
		else {
			throw new IllegalArgumentException("Point Cannot Be Null");
		}
	}

    @Override
    public boolean equals(Object o) {
	    boolean isEqual = false;
        if (this == o) isEqual = true;
        else if (o != null && getClass() == o.getClass())  isEqual = this.equals((SavablePoint2D) o);
        return isEqual;
    }

    public boolean equals(SavablePoint2D otherPoint){
	    var isEqual = false;
	    if(otherPoint != null) isEqual = otherPoint.getX() == getX() && otherPoint.getY() == getY();
	    return isEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY());
    }

    /**
	 * recreates the 2d point that was stored in the x and y values of the point and returns it as a new object.
	 * @return new point created with x and y values
	 */
	@JsonIgnore
	public Point2D get2DPoint() {
		if (truePoint == null) {
			this.truePoint = new Point2D(x, y);
		}
		return truePoint;
	}
}
