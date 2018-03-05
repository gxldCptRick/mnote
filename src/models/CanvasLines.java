package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

public class CanvasLines implements Serializable {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 55704392l;
	
	
	private List<List<SavablePoint2D>> lines;
	private List<SavablePoint2D> currentLine;
	
	public CanvasLines(){
		
		lines = new ArrayList<>();
		currentLine = new ArrayList<>();
		
	}

	public void startNewLine() {
		
		if(lines.contains(currentLine)) {
			
			currentLine = new ArrayList<>();
			
		}
		
	}

	public boolean isLineStarted() {
		
		return currentLine != null && !lines.contains(currentLine);
	
	}
	
	public void addNextPoint(SavablePoint2D savablePoint2D) {
		
		
		currentLine.add(savablePoint2D);
		
	}
	
	public void endLine() {
		
		if(lines != null && !lines.contains(currentLine)) {
			
			lines.add(currentLine);
			
		}
	
		
	}
	
	public void drawLines(GraphicsContext gc) {
		
		for (List<SavablePoint2D> line : lines) {
			Point2D startPoint = line.get(0).get2DPoint();
			
			gc.moveTo(startPoint.getX(),startPoint.getY());
			
			Iterator<SavablePoint2D> iterator = line.iterator();
			
			while(iterator.hasNext()) {
				Point2D nextPoint = iterator.next().get2DPoint();
			
				if( nextPoint != startPoint) {
					
					gc.lineTo(nextPoint.getX(), nextPoint.getY());
					
				}
								
			}
			
			gc.stroke();
		
		}
		if(!lines.contains(currentLine)) {

			Iterator<SavablePoint2D> iterator = currentLine.iterator();
			
			Point2D startPoint = currentLine.get(0).get2DPoint();
			
			gc.moveTo(startPoint.getX(), startPoint.getY());
			
			while(iterator.hasNext()) {
				
				Point2D nextPoint = iterator.next().get2DPoint();
				
				if(startPoint != nextPoint) {
					
					gc.lineTo(nextPoint.getX(), nextPoint.getY());
					
				}
				
				
				
			}
			
		}
		
	}
	
}
