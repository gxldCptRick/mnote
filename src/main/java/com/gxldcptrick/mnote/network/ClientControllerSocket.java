package com.gxldcptrick.mnote.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gxldcptrick.mnote.commonLib.Event;
import com.gxldcptrick.mnote.commonLib.EventListener;
import com.gxldcptrick.mnote.network.data.model.DrawingPackage;

import java.io.*;
import java.net.Socket;

public class ClientControllerSocket implements AutoCloseable, Runnable{
    private static final ObjectMapper mapper;
    static {
        mapper = new ObjectMapper();
    }
    private Socket serverConnection;
    private BufferedWriter serverOutput;
    private boolean connected;
    public Event<EventListener<DrawingEventArgs>, DrawingEventArgs> packetReadEvent;

    public ClientControllerSocket(Socket serverConnnection) {
        this.serverConnection = serverConnnection;
        this.packetReadEvent = new Event<>();
        connected = true;
    }

    public void sendPackageToServer(DrawingPackage drawingPackage) {
        try {
            if(serverOutput == null) this.serverOutput = new BufferedWriter(new OutputStreamWriter(this.serverConnection.getOutputStream()));
            var packageJSON = mapper.writeValueAsString(drawingPackage);
            serverOutput.write(packageJSON);
            serverOutput.newLine();
            serverOutput.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws Exception {
        this.connected = false;
        if(this.serverConnection != null && !this.serverConnection.isClosed()) this.serverConnection.close();
        if(this.serverOutput != null) this.serverOutput.close();
    }

    @Override
    public void run() {
        try(var reader = new BufferedReader(new InputStreamReader(this.serverConnection.getInputStream()))){
            do{
                var jsonSent = reader.readLine();
                var jackPackage = mapper.readValue(jsonSent, DrawingPackage.class);
                this.packetReadEvent.invoke(this, new DrawingEventArgs(jackPackage));
            } while(connected);
        }catch(IOException e){

        }
    }
}
