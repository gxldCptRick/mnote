package com.gxldcptrick.mnote.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gxldcptrick.mnote.commonLib.Event;
import com.gxldcptrick.mnote.commonLib.EventListener;
import com.gxldcptrick.mnote.FXView.models.DrawingPackage;

import java.io.*;
import java.net.Socket;

public class ClientConnection implements AutoCloseable {

    private final ObjectMapper objectMapper;
    private Socket socket;
    public int getPort(){ return socket.getPort(); }
    public Event<EventListener<DrawingEventArgs> , DrawingEventArgs> clientSendingArgs;

    public ClientConnection(Socket socket){
        this.socket = socket;
        this.clientSendingArgs = new Event<>();
        this.objectMapper = new ObjectMapper();

    }

    @Override
    public void close() throws IOException {
        if(socket != null && !socket.isClosed()) socket.close();
    }

    public void sendPackageToClient(DrawingPackage packet) {
        try{
            var writableStream = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            var jsonPacket = objectMapper.writeValueAsString(packet);
            writableStream.write(jsonPacket);
            writableStream.newLine();
            writableStream.flush();
            System.out.println("Sending Packet on: " + socket.getPort());
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void start() {
        try(var readingStream = new BufferedReader(new InputStreamReader(socket.getInputStream()))){
            while(!socket.isClosed()){
                var input = readingStream.readLine();
                if(input != null && !input.isBlank()){
                    System.out.println("Reading Stuff on " + socket.getPort() + ": " + input);
                    var value = objectMapper.readValue(input, DrawingPackage.class);
                    this.clientSendingArgs.invoke(this, new DrawingEventArgs(value));
                }
                else {
                    socket.close();
                }
            }
        }catch(EOFException e){
            System.out.println("Client Ended by sending nothing");
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
