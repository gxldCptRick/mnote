package com.gxldcptrick.mnote.tests.views;

import static com.gxldcptrick.mnote.tests.IOMethods.*;
import static org.junit.Assert.*;

import java.io.File;

import com.gxldcptrick.mnote.models.Brush;
import org.junit.Test;

import com.gxldcptrick.mnote.tests.TestApp;
import com.gxldcptrick.mnote.views.CanvasToolbar;

import javafx.application.Platform;

public class CanvasToolbarTests {

	@Test
	public void canBeSavedAndRestored() {
		
		Platform.runLater(() -> {
	
			//arrange
			CanvasToolbar expected = new CanvasToolbar(new Brush());
			CanvasToolbar actual;
			//act
			File save = saveFile("testCT.co", expected);
			
			actual = loadFile(save);
			
			
			//assert
			assertEquals(expected, actual);

			
		});		
	}
	


}
