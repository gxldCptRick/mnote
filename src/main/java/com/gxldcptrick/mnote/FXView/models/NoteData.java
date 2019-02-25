package com.gxldcptrick.mnote.FXView.models;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class NoteData {
	private String content;
	private double xPosition;
	private double yPosition;

	public NoteData(double xPosition, double yPosition){
		this(xPosition, yPosition, "");
	}

	public NoteData(double xPosition, double yPosition, String initialContent){
		this.xPosition = xPosition;
		this.yPosition = yPosition;
		this.content = initialContent;
	}

	public double getxPosition() {
		return xPosition;
	}

	public double getyPosition() {
		return yPosition;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		if(content != null){
			this.content = content;
		}
	}
}
