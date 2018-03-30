package com.gxldcptrick.mnote.views;

import com.gxldcptrick.mnote.models.NoteData;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;

import java.util.ArrayList;
import java.util.List;

public class NoteGroup extends Group {


	private List<NoteData> notes;

	public NoteGroup() {
		
		initialize();
		
	}

	private void initialize() {

	    if(notes == null)
	        notes = new ArrayList<>();

	    reloadNotes();

		this.setOnMouseClicked(event -> {
			
			 if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() >= 2) {

					NoteData note = new NoteData(event.getX(), event.getY());

					this.notes.add(note);

					TextField input = note.getInputForText();
					List<Node> children = this.getChildren();

					children.add(input);

					input.requestFocus();

					input.setOnAction(inputEntered -> {

						note.updateText();

						children.add(note.getDisplayForText());
						children.remove(input);

						note.getDisplayForText().setOnMouseClicked(clicked -> {

							children.remove(note.getDisplayForText());

							children.add(input);

							input.requestFocus();
						});

					});
				}
			
		});
		
	}

    private void reloadNotes() {


        if (notes.size() > 0) {

            List<Node> children = this.getChildren();
            for (NoteData noteData : notes) {
                if (noteData != null) {

                    TextField note = noteData.getInputForText();
                    note.setOnAction(inputEntered -> {

                        noteData.updateText();

                        children.add(noteData.getDisplayForText());
                        children.remove(note);

                    });

                    noteData.getDisplayForText().setOnMouseClicked(clicked -> {

                        children.remove(noteData.getDisplayForText());

                        children.add(note);

                        note.requestFocus();
                    });

                    this.getChildren().add(noteData.getDisplayForText());

                }
            }

        }

	}

}
