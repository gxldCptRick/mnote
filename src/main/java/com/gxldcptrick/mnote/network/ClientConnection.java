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
            var writableStream = new PrintStream(socket.getOutputStream());
            var jsonPacket = objectMapper.writeValueAsString(packet);
            System.out.println(jsonPacket);
            writableStream.println(jsonPacket);
            writableStream.flush();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void start() {
        try(var readingStream = new BufferedReader(new InputStreamReader(socket.getInputStream()))){
            while(socket.isConnected()){
                var input = readingStream.readLine();
                System.out.println(input);
                var value = objectMapper.readValue(input, DrawingPackage.class);
                this.clientSendingArgs.invoke(this, new DrawingEventArgs(value));
            }
        }catch(EOFException e){
            System.out.println("Client Ended by sending nothing");
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
