package controllers;

import java.awt.Toolkit;
import java.io.File;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import views.FileMenuToolbar;

public class Main extends Application {

	private VBox mainLayout;
	private DrawableCanvas drawSurface;
	private FileMenuToolbar tools;

	@Override
	public void start(Stage primaryStage) {

		drawSurface = new DrawableCanvas(Toolkit.getDefaultToolkit().getScreenSize().getWidth(),
				Toolkit.getDefaultToolkit().getScreenSize().getHeight());

		tools = new FileMenuToolbar();

		tools.getSaveOption().setOnAction((event) -> {
			File newSave = tools.getFileChooser().showSaveDialog(primaryStage);
			
			if(newSave != null) {
				
				tools.saveFile(newSave, drawSurface);

			}

		});

		tools.getSaveAsOption().setOnAction((event) -> {

			tools.getFileChooser().showSaveDialog(primaryStage);

		});

		tools.getLoadNoteOption().setOnAction(event -> {
			File oldSave = tools.getFileChooser().showOpenDialog(primaryStage);
			

			if (oldSave != null && oldSave.getName().endsWith(".co")) {
	
				mainLayout.getChildren().remove(this.drawSurface.getLayout());
				
				this.drawSurface = (DrawableCanvas) tools.loadFile(oldSave);
				
				mainLayout.getChildren().add(drawSurface.getLayout());

				drawSurface.getLayout().prefWidthProperty().bind(primaryStage.widthProperty().multiply(.95));

				drawSurface.getLayout().prefHeightProperty().bind(primaryStage.heightProperty());
			}
		});

		mainLayout = new VBox();

		mainLayout.getChildren().addAll(tools, drawSurface.getLayout());

		Scene mainScene = new Scene(mainLayout, 1500, 500);

		primaryStage.setScene(mainScene);

		primaryStage.setTitle("MNote");

		drawSurface.getLayout().prefWidthProperty().bind(primaryStage.widthProperty().multiply(.95));

		drawSurface.getLayout().prefHeightProperty().bind(primaryStage.heightProperty());

		primaryStage.show();
		
	}

	public static void main(String[] args) {
		launch(args);
	}
}
