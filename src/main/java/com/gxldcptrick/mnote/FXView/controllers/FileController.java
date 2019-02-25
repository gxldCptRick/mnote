package com.gxldcptrick.mnote.FXView.controllers;

import com.gxldcptrick.mnote.FXView.events.EventHolder;
import com.gxldcptrick.mnote.FXView.models.NoteBookData;
import com.gxldcptrick.mnote.commonLib.EventArgs;

public class FileController extends EventWrapper {
    private NoteBookData saveData;
    public FileController(EventHolder holder) {
        super(holder);
        saveData = new NoteBookData();
    }

    public NoteBookData getSaveData(){
        return this.saveData;
    }

    @Override
    protected void attachToEvents(EventHolder holder) {
        holder.getEmptyEvents().subscribeToEvent(this::saveFile, "Save Event");
        holder.getEmptyEvents().subscribeToEvent(this::saveAsFile, "Save As Event");
        holder.getEmptyEvents().subscribeToEvent(this::loadFile, "Load Event");
        holder.getEmptyEvents().subscribeToEvent(this::newFile, "New File Event");
        holder.getEmptyEvents().subscribeToEvent(this::exportFile, "Export Event");
    }

    @Override
    public void detachFromEvents(EventHolder holder) {
        holder.getEmptyEvents().unsubscribeToEvent(this::saveFile, "Save Event");
        holder.getEmptyEvents().unsubscribeToEvent(this::saveAsFile, "Save As Event");
        holder.getEmptyEvents().unsubscribeToEvent(this::loadFile, "Load Event");
        holder.getEmptyEvents().unsubscribeToEvent(this::newFile, "New File Event");
        holder.getEmptyEvents().unsubscribeToEvent(this::exportFile, "Export Event");
    }



    private void exportFile(Object sender, EventArgs eventArgs) {
        //TODO: doing the work
    }

    private void newFile(Object sender, EventArgs eventArgs) {
        //TODO: doing the work
    }

    private void loadFile(Object sender, EventArgs eventArgs) {
        //TODO: doing the work
    }

    private void saveAsFile(Object sender, EventArgs eventArgs) {
        //TODO: doing the work
    }

    private void saveFile(Object sender, EventArgs eventArgs) {
        //TODO: doing the work
    }
}
