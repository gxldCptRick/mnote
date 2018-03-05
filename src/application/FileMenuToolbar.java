package application;

import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;

public class FileMenuToolbar extends HBox {

	private Button file;

	
	public FileMenuToolbar() {
		file = new Button("file");
		ContextMenu cm = new ContextMenu();
		
		file.setContextMenu(cm);
		
		cm.getItems().addAll(new MenuItem("Save"), 
				new MenuItem("Save As"), 
				new MenuItem("Load Note"), 
				new MenuItem("New Note"));
				
		file.setOnAction(event -> {
		
			cm.show(file, Side.BOTTOM, 0, 0);
	
		});
		
		getChildren().addAll(file);
	}
	
}
