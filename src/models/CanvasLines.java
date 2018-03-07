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

	public List<List<SavablePoint2D>> getLines() {

		return new ArrayList<>(lines);

	}

	public List<SavablePoint2D> getCurrentLine() {

		return new ArrayList<>(currentLine);

	}

	public CanvasLines() {
		
		currentLine = new ArrayList<>();
		
		lines = new ArrayList<>();

	}

	public void startNewLine() {

		currentLine = new ArrayList<>();

		lines.add(currentLine);

	}

	public boolean isLineStarted() {

		return currentLine != null && !lines.contains(currentLine);

	}

	public void addNextPoint(SavablePoint2D savablePoint2D) {

		if (savablePoint2D != null)
			currentLine.add(savablePoint2D);

	}

	public void drawLines(GraphicsContext gc) {

		for (List<SavablePoint2D> line : lines) {
			Point2D startPoint = line.get(0).get2DPoint();
			System.out.println(line);

			// gc.moveTo(startPoint.getX(),startPoint.getY());

			Iterator<SavablePoint2D> iterator = line.iterator();

			while (iterator.hasNext()) {
				Point2D nextPoint = iterator.next().get2DPoint();

				if (nextPoint != startPoint) {

					// gc.lineTo(nextPoint.getX(), nextPoint.getY());

				}

			}

			// gc.stroke();

		}

		if (!lines.contains(currentLine)) {

			Iterator<SavablePoint2D> iterator = currentLine.iterator();

			Point2D startPoint = currentLine.get(0).get2DPoint();

			gc.moveTo(startPoint.getX(), startPoint.getY());

			while (iterator.hasNext()) {

				Point2D nextPoint = iterator.next().get2DPoint();

				if (startPoint != nextPoint) {

					gc.lineTo(nextPoint.getX(), nextPoint.getY());

				}

			}

		}

	}
	
	public boolean equals(CanvasLines other) {
		
		return other != null && this.lines.equals(other.lines) && this.currentLine.equals(other.currentLine);
	}


	@Override
	public boolean equals(Object other) {
		boolean equal = false;
		if(CanvasLines.class.isInstance(other)) {
			
			equal = equals((CanvasLines) other);
			
		}
		
		return equal;
	}

}
