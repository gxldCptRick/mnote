package models;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javafx.geometry.Point2D;

public class SavablePoint2D implements Serializable {
	/**
	 * version 6969.
	 */
	private static final long serialVersionUID = 6969L;

	private double x;
	private double y;
	private transient Point2D pointToSave;

	public SavablePoint2D(double x, double y) {
		
		this.x = x;
		this.y = y;
		pointToSave = new Point2D(x,y);
		
	}
	
	public SavablePoint2D(Point2D oldPoint) {
		if (oldPoint != null) {
			x = oldPoint.getX();
			y = oldPoint.getY();

			pointToSave = oldPoint;
		}
		else {
			
			throw new IllegalArgumentException("Point Cannot Be Null");
			
		}
	}

	public Point2D get2DPoint() {
		
		return pointToSave;

	}
	
	public void writeObject(ObjectOutputStream out) throws IOException, ClassNotFoundException{
		
		out.defaultWriteObject();
		
	}
	
	public void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
		
		ObjectInputStream.GetField field = in.readFields();
		
		x = field.get("x", -1);
		y = field.get("y", -1);
		
		pointToSave = new Point2D(x, y);
		
	}

	
	
}
