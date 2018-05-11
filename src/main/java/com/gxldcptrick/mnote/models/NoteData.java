package com.gxldcptrick.mnote.models;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.sun.istack.internal.NotNull;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class NoteData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String content;
	private double xPosition;
	private double yPosition;

	private transient Label displayForText;
	private transient TextField inputForText;

	public NoteData(double xPosition, double yPosition) {
		this.xPosition = xPosition;
		this.yPosition = yPosition;

		initializeScene();
	}

	public Label getDisplayForText() {
		return displayForText;
	}

	public TextField getInputForText() {
		return inputForText;
	}


	public void setContent(String content) {
		if (content != null)
			this.content = content;
	}

	private void setupControlXandYPosition(Control c) {
		if(c != null){
			c.setLayoutX(xPosition);
			c.setLayoutY(yPosition);
		}

	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		ObjectInputStream.GetField fields = in.readFields();

		xPosition = fields.get("xPosition", 0d);
		yPosition = fields.get("yPosition", 0d);
		content = (String) fields.get("content", "");

		initializeScene();
		this.displayForText.setText(content);
		this.inputForText.setText(content);

	}

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

	private void initializeScene() {
		this.displayForText = new Label();
		this.inputForText = new TextField();

		setupControlXandYPosition(this.displayForText);
		setupControlXandYPosition(this.inputForText);// TODO Auto-generated method stub
	}

	public void updateText() {
		content = this.inputForText.getText(); 
		
		this.displayForText.setText(content);
	}
}
