package com.gxldcptrick.mnote.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gxldcptrick.mnote.commonLib.Event;
import com.gxldcptrick.mnote.commonLib.EventListener;

import java.io.*;
import java.net.Socket;
import java.util.UUID;

public class ClientConnection implements AutoCloseable {

    private final ObjectMapper objectMapper;
    private final Socket socket;
    public int getPort(){ return socket.getPort(); }
    public final UUID clientID;
    private final BufferedWriter clientOutput;
    public Event<EventListener<DrawingEventArgs> , DrawingEventArgs> clientSendingArgs;

    public ClientConnection(Socket socket, UUID clientID) throws IOException{
        this.socket = socket;
        this.clientID = clientID;
        this.clientOutput = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.clientSendingArgs = new Event<>();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void close() throws IOException {
        if(socket != null && !socket.isClosed()) socket.close();
        if(clientOutput != null) clientOutput.close();
    }

    public void sendPackageToClient(DrawingPackage packet) {
        try {
            var jsonPacket = objectMapper.writeValueAsString(packet);
            this.clientOutput.write(jsonPacket);
            this.clientOutput.newLine();
            this.clientOutput.flush();
            System.out.println("Sending Packet for: " + this.clientID);
            System.out.println("Package: " + jsonPacket);
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
                    value.setClientUUID(clientID.toString());
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
