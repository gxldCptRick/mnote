package com.gxldcptrick.mnote.tests.views;

import static org.junit.Assert.*;

import com.gxldcptrick.mnote.tests.TestApp;
import com.gxldcptrick.mnote.views.FileMenuToolbar;
import javafx.application.Platform;
import org.junit.Test;

public class FileMenuToolbarTests {

    @Test
    public void toolbarIsAlive() {

        Platform.runLater(() -> {
            FileMenuToolbar menu = new FileMenuToolbar();

            assertNotNull(menu.getExportAsOption());

            assertNotNull(menu.getFileChooser());

            assertNotNull(menu.getLoadNoteOption());

            assertNotNull(menu.getNewNoteOption());

            assertNotNull(menu.getSaveAsOption());

            assertNotNull(menu.getSaveOption());


        });
    }

}