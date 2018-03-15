package com.gxldcptrick.mnote.tests.models;

import static com.gxldcptrick.mnote.tests.IOMethods.*;
import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.junit.Test;

import com.gxldcptrick.mnote.models.CanvasLine;
import com.gxldcptrick.mnote.models.CanvasLines;
import com.gxldcptrick.mnote.models.SavablePoint2D;


public class CanvasLinesTest {

	@Test
	public void newLineIsNotNull() {
	
		// arrange
		CanvasLines lines = new CanvasLines();
		CanvasLine badCase = null;
		CanvasLine actual;
		
		// act
		lines.startNewLine();
		actual = lines.getCurrentLine();

		// assert
		assertNotEquals(badCase, actual);

	}

	@Test
	public void canCreateTwoNewUniqueLines() {
		// arrange
		CanvasLines lines = new CanvasLines();
		CanvasLine firstLine;
		CanvasLine secondLine;
		SavablePoint2D differingPoint = new SavablePoint2D(100, 100);

		// act
		lines.startNewLine();
		
		firstLine = lines.getCurrentLine();

		lines.startNewLine();
		
		lines.addNextPoint(differingPoint);

		secondLine = lines.getCurrentLine();
		
		// assert
		assertNotEquals(firstLine, secondLine);

	}

	@Test
	public void allLinesCreatedAreSaved() {
		// arrange
		CanvasLines lines = new CanvasLines();
		List<CanvasLine> originalLines = lines.getLines();
		List<CanvasLine> secondaryLines;

		// act
		lines.startNewLine();
		secondaryLines = lines.getLines();

		// assert
		assertNotEquals(originalLines, secondaryLines);

	}

	@Test
public 	void canvasLinesAreSavable() {
		
		// arrange
		CanvasLines expected = new CanvasLines();
		CanvasLines actual;

		// act
		expected.startNewLine();
		File saveFile = saveFile("test.co", expected);
		actual = loadFile(saveFile);

		System.out.println(expected.getLines());
		System.out.println(actual.getLines());
		
		// assert
		assertNotEquals(actual, null);
		
		assertEquals(expected, actual);

	}
	
	
														/*Helper Methods for the tests*/	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



}
