package com.gxldcptrick.mnote.models;
///@@@@ "Javadoc style documentation on an entire class (class and all its methods)"
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

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

	
	/**
	 * takes in the starting x and starting y position and creates a point that you may be able to persists with serialization.
	 * @param x the starting x value.
	 * @param y the starting y value.
	 * 
	 * **/
	public SavablePoint2D(double x, double y) {
		
		this.x = x;
		this.y = y;
		
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

	

	/**
	 * recreates the 2d point that was stored in the x and y values of the point and returns it as a new object.
	 * @return new point created with x and y values
	 */
	public Point2D get2DPoint() {
		
		return new Point2D(x,y);

	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
		
		ObjectInputStream.GetField field = in.readFields();
		
		x = field.get("x", -1d);
		y = field.get("y", -1d);
	
	}

	
	
}
