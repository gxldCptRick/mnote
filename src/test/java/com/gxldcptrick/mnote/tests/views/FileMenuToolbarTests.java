package com.gxldcptrick.mnote.tests.views;

import static org.junit.jupiter.api.Assertions.*;

import com.gxldcptrick.mnote.tests.TestApp;
import com.gxldcptrick.mnote.FXView.views.FileMenuToolbar;
import javafx.application.Platform;
import org.junit.jupiter.api.Test;

public class FileMenuToolbarTests {

    @Test
    public void canUpdateCurrentFileName() {

        Platform.runLater(() -> {

            // arrange
            FileMenuToolbar menu = new FileMenuToolbar();

            String expected = null;
            String actual;
            // act
            menu.updateFileName(null);

            actual = menu.getCurrentFileName();

            // assert

            assertEquals(expected, actual);

        });

    }

}