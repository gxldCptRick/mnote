package com.gxldcptrick.mnote.network;

import com.gxldcptrick.mnote.commonLib.Event;
import com.gxldcptrick.mnote.commonLib.EventListener;
import com.gxldcptrick.mnote.FXView.models.DrawingPackage;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientConnection implements AutoCloseable {

    private Socket socket;
    public int getPort(){ return socket.getPort(); }
    public Event<EventListener<DrawingEventArgs> , DrawingEventArgs> clientSendingArgs;

    public ClientConnection(Socket socket){
        this.socket = socket;
        this.clientSendingArgs = new Event<>();
    }

    @Override
    public void close() throws IOException {
        if(socket != null && !socket.isClosed()) socket.close();
    }

    public void sendPackageToClient(DrawingPackage packet) {
        try{
            var writableStream = new ObjectOutputStream(socket.getOutputStream());
            writableStream.writeObject(packet);
            writableStream.flush();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void start() {
        try(var readingStream = new ObjectInputStream(socket.getInputStream())){
            while(socket.isConnected()){
                var input = readingStream.readObject();
                if(DrawingPackage.class.isInstance(input)){
                    this.clientSendingArgs.invoke(this, new DrawingEventArgs((DrawingPackage) input));
                }
            }
        }catch(EOFException e){
            System.out.println("Client Ended by sending nothing");
        }
        catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
