package com.gxldcptrick.mnote.FXView.controllers;

import com.gxldcptrick.mnote.FXView.components.NoteComponent;
import com.gxldcptrick.mnote.FXView.events.EventHolder;
import com.gxldcptrick.mnote.FXView.models.MouseEventArgs;
import com.gxldcptrick.mnote.FXView.models.NoteData;
import com.gxldcptrick.mnote.commonLib.EventArgs;
import javafx.scene.Group;

import java.util.ArrayList;
import java.util.List;

public class NoteController extends EventWrapper{
    private final List<NoteData> data;

    public NoteController(final EventHolder holder){
        super(holder);
        this.data = new ArrayList<>();
    }

    @Override
    public void attachToEvents(final EventHolder holder){
        holder.getMouseEvents().subscribeToEvent(this::addNoteToGroup, "Note Clicked");
        holder.getEmptyEvents().subscribeToEvent(this::clear, "Notes Cleared");
    }

    @Override
    public void detachFromEvents(final EventHolder holder) {
        holder.getMouseEvents().unsubscribeToEvent(this::addNoteToGroup, "Note Clicked");
        holder.getEmptyEvents().unsubscribeToEvent(this::clear, "Notes Cleared");
    }

    public void redrawNotesOnScreen(final Group parent){
        for (var note: data) {
            attachNoteToGroup(note, parent);
        }
    }

    private void attachNoteToGroup(NoteData note, Group parent) {
        new NoteComponent(note, parent);
    }


    private void clear(Object sender, EventArgs e){
        if(sender != null){
            this.data.clear();
        }
    }

    private void addNoteToGroup(Object sender, MouseEventArgs e){
        if(sender instanceof Group){
            var nativeEvent = e.event;
            var noteData = new NoteData(nativeEvent.getX(), nativeEvent.getY());
            attachNoteToGroup(noteData, (Group) sender);
        }
    }
}
