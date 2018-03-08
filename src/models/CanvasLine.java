package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class CanvasLine implements Serializable{
	
	
	private static final long serialVersionUID = 7230L;
	
	private double lineWidth;
	private List<SavablePoint2D> points;
	private SavableColor color;

	public CanvasLine(CanvasLine currentLine) {

		this(currentLine.color, currentLine.lineWidth);
		this.points = new ArrayList<>(currentLine.points);
	}

	public CanvasLine(SavableColor color, double lineWidth) {

		this.color = color;
		this.lineWidth = lineWidth;
		this.points = new ArrayList<>();
		
	}

	public CanvasLine(Color color, double lineWidth) {
		this(new SavableColor(color), lineWidth);
	}

	public void addNextPoint(SavablePoint2D nextPoint) {
		if(nextPoint != null)
		points.add(nextPoint);
		
	}

	public void drawLine(GraphicsContext gc) {
		
		gc.setLineWidth(lineWidth);
		
		gc.setStroke(color.getColor());
		
		Point2D initialPoint = getInitialPoint().get2DPoint();
		
		gc.moveTo(initialPoint.getX(), initialPoint.getY());
		
		Iterator<SavablePoint2D> iterator = points.iterator();
		
		while(iterator.hasNext()) {
			
			Point2D nextPoint = iterator.next().get2DPoint();
			
			if(nextPoint != initialPoint) {
				
				gc.lineTo(nextPoint.getX(), nextPoint.getY());
				
			}
			
		}
		
		gc.stroke();
		
	}

	public SavablePoint2D getInitialPoint() {
		// TODO Auto-generated method stub
		return points.get(0);
	}
	
	
	public boolean equals(CanvasLine line) {
		
		return  line != null && line.points.equals(this.points) && line.lineWidth == this.lineWidth && line.color.equals(this.color);
		
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
	public String toString() {
		
		return  points + " " + color + " " + lineWidth;
		
	}
	
}
