package controllers;

import java.awt.Toolkit;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import views.FileMenuToolbar;

public class Main extends Application {

	private VBox mainLayout;
	private Group mainGroup;
	private DrawableCanvas drawSurface;
	private FileMenuToolbar tools;
	private List<TextField> notes;
	private List<Label> noteDisplay;
	@Override
	public void start(Stage primaryStage) {

		notes = new ArrayList<>();
		noteDisplay = new ArrayList<>();
		
		drawSurface = new DrawableCanvas(Toolkit.getDefaultToolkit().getScreenSize().getWidth(),
				Toolkit.getDefaultToolkit().getScreenSize().getHeight());

		tools = new FileMenuToolbar();

		tools.getSaveOption().setOnAction((event) -> {
			
			File newSave = tools.getFileChooser().showSaveDialog(primaryStage);

			if (newSave != null) {

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

		mainGroup = new Group();

		mainGroup.getChildren().add(mainLayout);

		mainGroup.setOnMouseClicked(event -> {
			System.out.println(event.getClickCount());
			if (event.getClickCount() >= 2) {

				TextField note = new TextField();
				this.notes.add(note);
				note.setLayoutX(event.getX());
				note.setLayoutY(event.getY());
				mainGroup.getChildren().add(note);
				note.requestFocus();
				
				note.setOnAction(noteEvent -> {

					mainGroup.getChildren().remove(note);
					Label text = new Label(note.getText());
					this.noteDisplay.add(text);
					text.setLayoutX(note.getLayoutX());
					text.setLayoutY(note.getLayoutY());
					mainGroup.getChildren().add(text);
					
					text.setOnMouseClicked(textEvent -> {
						
						mainGroup.getChildren().remove(text);
						mainGroup.getChildren().add(note);
						note.requestFocus();
						
					});
				});

			}

		});

		drawSurface.setEventTarget(mainGroup);

		Scene mainScene = new Scene(mainGroup, 1500, 500);

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
