package controllers;

import java.awt.Toolkit;
import java.io.File;
import java.nio.file.Files;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import views.FileMenuToolbar;

public class Main extends Application {

	private VBox mainLayout;
	private Group mainGroup;
	private DrawableCanvas drawSurface;
	private FileMenuToolbar tools;
	@Override
	public void start(Stage primaryStage) {	
		drawSurface = new DrawableCanvas(Toolkit.getDefaultToolkit().getScreenSize().getWidth(),
				Toolkit.getDefaultToolkit().getScreenSize().getHeight());

		tools = new FileMenuToolbar();

		tools.getSaveOption().setOnAction(event -> {
			
			File newSave = tools.getFileChooser().showSaveDialog(primaryStage);

			if (newSave != null) {

				tools.saveFile(newSave, drawSurface);

			}

		});

		tools.getSaveAsOption().setOnAction(event -> {

			File overwriteSave = tools.getFileChooser().showSaveDialog(primaryStage);

			if(overwriteSave != null) {
				
				tools.saveFile(overwriteSave, drawSurface);
				
			}
			
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
		
		tools.getNewNoteOption().setOnAction(event -> {
			
			promptToSave(primaryStage);
			
			mainLayout = new VBox();
			drawSurface = new DrawableCanvas(Toolkit.getDefaultToolkit().getScreenSize().getWidth(), Toolkit.getDefaultToolkit().getScreenSize().getHeight());
			mainLayout.getChildren().addAll(tools, drawSurface.getLayout());
			drawSurface.getLayout().prefWidthProperty().bind(primaryStage.widthProperty().multiply(.95));
			drawSurface.getLayout().prefHeightProperty().bind(primaryStage.heightProperty());
			Scene mainScene = new Scene(mainLayout, primaryStage.getWidth(), primaryStage.getHeight());
			
			primaryStage.setScene(mainScene);

		});

		mainLayout = new VBox();

		mainLayout.getChildren().addAll(tools, drawSurface.getLayout());

		mainGroup = new Group();

		mainGroup.getChildren().add(mainLayout);

		Scene mainScene = new Scene(mainGroup, 1500, 500);

		primaryStage.setScene(mainScene);

		primaryStage.setTitle("MNote");

		drawSurface.getLayout().prefWidthProperty().bind(primaryStage.widthProperty().multiply(.95));

		drawSurface.getLayout().prefHeightProperty().bind(primaryStage.heightProperty());

		primaryStage.show();

	}

	private void promptToSave(Stage stage) {
		Dialog<?> savePop = new Alert(AlertType.WARNING);
		
		savePop.setContentText("Unsaved changes");
		
		savePop.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> {
			
			File save = tools.getFileChooser().showSaveDialog(stage);
			
			if(save != null) {
				
				tools.saveFile(save, drawSurface);
			
			}
			
		});
	}

	public static void main(String[] args) {
		launch(args);
	}
}
