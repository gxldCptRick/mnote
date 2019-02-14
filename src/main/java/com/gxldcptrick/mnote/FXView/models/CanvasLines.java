package com.gxldcptrick.mnote.FXView.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.gxldcptrick.mnote.FXView.enums.SpecialEffect;

import javafx.geometry.Point2D;
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
		currentLine = new CanvasLine();
	}

	public void startNewLine(Color colorOfLine, double lineWidth) {
		startNewLine(colorOfLine,lineWidth, null);
	}
	
	public void startNewLine(Color colorOfLine, double lineWidth, SpecialEffect specialEffect) {
		startNewLine(new Brush(lineWidth, ));
	}

	public void startNewLine(Brush brush){
		currentLine = new CanvasLine(colorOfLine, lineWidth , specialEffect);
		lines.add(currentLine);
	}
	public boolean removeLine(Point2D point) {
		boolean foundLine = false;
		CanvasLine line = null;

		for(int i = lines.size() - 1; i > -1 && !foundLine; i--) {
			foundLine = lines.get(i).contains(point);
			if(foundLine) {
				line = lines.remove(i);
			}
		}
		if(line == currentLine) {
			this.currentLine = new CanvasLine();
		}

		return foundLine;
	}

	public void startNewLine() {
		startNewLine(Color.BLACK, 10);
	}

	public void addNextPoint(SavablePoint2D savablePoint2D) {
		if (savablePoint2D != null)
			currentLine.addNextPoint(savablePoint2D);
	}

	public void drawLines(GraphicsContext gc) {
		System.out.println("Drawing Lines ");
		this.lines.forEach((e) -> e.drawLine(gc));
		if (currentLine != null && !lines.contains(currentLine)) {
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

    public boolean isLineStarted() {
        return this.currentLine != new CanvasLine();
    }
	
	@Override
	public int hashCode() {
		return lines.hashCode() ^ currentLine.hashCode();
		
	}

	@Override
    public String toString(){
	    return this.lines.toString();
    }

}
