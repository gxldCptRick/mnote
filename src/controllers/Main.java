package controllers;

import java.awt.Toolkit;
import java.io.File;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import views.FileMenuToolbar;

public class Main extends Application {

	private VBox mainLayout;
	private DrawableCanvas drawSurface;
	private FileMenuToolbar tools;
	private File recentlyOpenedFile;

	@Override
	public void start(Stage primaryStage) {
		drawSurface = new DrawableCanvas(Toolkit.getDefaultToolkit().getScreenSize().getWidth(),
				Toolkit.getDefaultToolkit().getScreenSize().getHeight());

		tools = new FileMenuToolbar();

		initializeTools(primaryStage);

		mainLayout = new VBox();

		mainLayout.getChildren().addAll(tools, drawSurface.getLayout());

		Scene mainScene = new Scene(mainLayout, 1500, 500);

		primaryStage.setScene(mainScene);

		primaryStage.setTitle("MNote");

		bindSurfaceWithStage(primaryStage);

		primaryStage.show();

	}

	private void bindSurfaceWithStage(Stage primaryStage) {

		drawSurface.getLayout().prefWidthProperty().bind(primaryStage.widthProperty().multiply(.95));

		drawSurface.getLayout().prefHeightProperty().bind(primaryStage.heightProperty());

	}

	private void initializeTools(Stage primaryStage) {
		tools.getSaveOption().setOnAction(event -> {
			if (this.recentlyOpenedFile == null) {
			
				File newSave = tools.getFileChooser().showSaveDialog(primaryStage);
				saveFile(newSave);
			
			} else {
				
				saveFile(this.recentlyOpenedFile);
			
			}
		});

		tools.getSaveAsOption().setOnAction(event -> {

			if (this.recentlyOpenedFile != null) {
				tools.getFileChooser().setInitialFileName(this.recentlyOpenedFile.getName());
			}

			File overwriteSave = tools.getFileChooser().showSaveDialog(primaryStage);

			saveFile(overwriteSave);

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

			drawSurface = new DrawableCanvas(Toolkit.getDefaultToolkit().getScreenSize().getWidth(),
					Toolkit.getDefaultToolkit().getScreenSize().getHeight());

			mainLayout = new VBox();
			mainLayout.getChildren().addAll(tools, drawSurface.getLayout());

			drawSurface.getLayout().prefWidthProperty().bind(primaryStage.widthProperty().multiply(.95));
			drawSurface.getLayout().prefHeightProperty().bind(primaryStage.heightProperty());

			Scene mainScene = new Scene(mainLayout, 1500, 500);

			primaryStage.setScene(mainScene);

		});

	}

	private void promptToSave(Stage stage) {
		Dialog<?> savePop = new Alert(AlertType.CONFIRMATION);

		savePop.setContentText("Would You like to save your changes?");

		savePop.showAndWait().ifPresent(response -> {
			if (response == ButtonType.OK) {

				File save = tools.getFileChooser().showSaveDialog(stage);

				saveFile(save);
			}

		});
	}

	private void saveFile(File save) {
		if (save != null) {

			if (!save.getAbsolutePath().endsWith(".co"))
				save = new File(save.getAbsolutePath() + ".co");

			tools.saveFile(save, drawSurface);

			this.recentlyOpenedFile = save;
		}

	}

	public static void main(String[] args) {
		launch(args);
	}
}
