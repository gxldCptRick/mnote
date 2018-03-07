package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class CanvasLines implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 55704392l;

	private List<CanvasLine> lines;
	private CanvasLine currentLine;

	public List<CanvasLine> getLines() {

		return new ArrayList<>(lines);

	}

	public CanvasLine getCurrentLine() {

		return new CanvasLine(currentLine);

	}

	public CanvasLines() {
		
		
		lines = new ArrayList<>();

	}

	public void startNewLine(Color colorOfLine, double lineWidth) {

		currentLine = new CanvasLine(colorOfLine, lineWidth);

		lines.add(currentLine);

	}
	
	public void startNewLine() {

		startNewLine(Color.BLACK, 10);
	
	}

	public boolean isLineStarted() {

		return currentLine != null && !lines.contains(currentLine);

	}

	public void addNextPoint(SavablePoint2D savablePoint2D) {

		if (savablePoint2D != null)
			currentLine.addNextPoint(savablePoint2D);

	}

	public void drawLines(GraphicsContext gc) {

		for (CanvasLine line : lines) {
			
			line.drawLine(gc);

		}

		if (!lines.contains(currentLine)) {

			currentLine.drawLine(gc);
		}

	}
	
	public boolean equals(CanvasLines other) {
		
		return other != null && other.lines.equals(this.lines) && other.currentLine.equals(this.currentLine);
	
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
