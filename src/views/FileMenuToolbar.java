package views;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class FileMenuToolbar extends HBox {

	private Button file;
	private ContextMenu context;
	private MenuItem saveOption;
	private MenuItem saveAsOption;
	private MenuItem loadNoteOption;
	private MenuItem newNoteOption;
	private FileChooser fileChooser;

	public FileMenuToolbar() {

		file = new Button("File");

		context = new ContextMenu();

		fileChooser = new FileChooser();
		File initialDirectory = new File("../../mnote/");
		
		if(!initialDirectory.isDirectory())
			initialDirectory.mkdir();
		
		fileChooser.setInitialDirectory(initialDirectory);
		fileChooser.setSelectedExtensionFilter(new ExtensionFilter("mnote files", "co"));
		fileChooser.setInitialFileName("default.co");
		
		
		file.setContextMenu(context);

		setupMenuItems();

		file.setOnAction(event -> {

			context.show(file, Side.BOTTOM, 0, 0);

		});

		getChildren().addAll(file);
	}

	public MenuItem getSaveOption() {
		return saveOption;
	}

	public MenuItem getSaveAsOption() {
		return saveAsOption;
	}

	public MenuItem getLoadNoteOption() {
		return loadNoteOption;
	}

	public MenuItem getNewNoteOption() {
		return newNoteOption;
	}

	private void setupMenuItems() {

		saveOption = new MenuItem("Save");
		saveAsOption = new MenuItem("Save As");
		loadNoteOption = new MenuItem("Load Note");
		newNoteOption = new MenuItem("New Note");

		context.getItems().addAll(saveOption, saveAsOption, loadNoteOption, newNoteOption);

	}

	///@@@@ "File reading (4 points) and writing"
	public void saveFile(File newSave, Serializable data) {

		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(newSave))) {

			System.out.println("Hi");
			out.writeObject(data);

		} catch (Exception e) {

			e.printStackTrace();

		}
	}

	public Serializable loadFile(File loadingFile) {

		Serializable data = null;
		
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(loadingFile))) {

			data = (Serializable) in.readObject();

		} catch (IOException | ClassNotFoundException e) {

			e.printStackTrace();
			
		}
		
		return data;
	}

	public FileChooser getFileChooser() {
		// TODO Auto-generated method stub
		return this.fileChooser;
	}

}
