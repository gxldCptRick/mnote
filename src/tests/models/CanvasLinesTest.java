package tests.models;

import static org.junit.jupiter.api.Assertions.*;
import static tests.IOMethods.*;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;

import models.CanvasLine;
import models.CanvasLines;
import models.SavablePoint2D;


class CanvasLinesTest {

	@Test
	void newLineIsNotNull() {
	
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
	void canCreateTwoNewUniqueLines() {
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
	void allLinesCreatedAreSaved() {
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
	void canvasLinesAreSavable() {
		
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
