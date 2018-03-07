package tests.models;

import static org.junit.jupiter.api.Assertions.*;
import static tests.HelperMethods.*;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;

import models.CanvasLines;
import models.SavablePoint2D;


class CanvasLinesTest {

	@Test
	void newLineIsNotNull() {
		// arrange
		CanvasLines lines = new CanvasLines();
		List<SavablePoint2D> badCase = null;
		List<SavablePoint2D> actual;
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
		List<SavablePoint2D> firstLine;
		List<SavablePoint2D> secondLine;

		// act
		lines.startNewLine();
		firstLine = lines.getCurrentLine();

		lines.startNewLine();
		lines.addNextPoint(new SavablePoint2D(100, 100));

		secondLine = lines.getCurrentLine();
		// assert
		assertNotEquals(firstLine, secondLine);

	}

	@Test
	void canAddPointToLine() {
		// arrange
		CanvasLines lines = new CanvasLines();
		List<SavablePoint2D> currentLine;
		SavablePoint2D expected = new SavablePoint2D(200, 00);
		SavablePoint2D actual;

		// act
		lines.startNewLine();
		lines.addNextPoint(expected);
		currentLine = lines.getCurrentLine();
		actual = currentLine.get(0);

		// assert
		assertEquals(expected, actual);

	}

	@Test
	void allLinesCreatedAreSaved() {
		// arrange
		CanvasLines lines = new CanvasLines();
		List<List<SavablePoint2D>> originalLines = lines.getLines();
		List<List<SavablePoint2D>> secondaryLines;

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
		File saveFile = saveFile("test.co", expected);
		actual = loadFile(saveFile);

		// assert

		assertNotEquals(actual, null);

		assertEquals(expected, actual);

	}
	
	
														/*Helper Methods for the tests*/	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



}
