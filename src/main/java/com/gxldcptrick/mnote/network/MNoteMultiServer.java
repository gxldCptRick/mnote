package com.gxldcptrick.mnote.network;

import com.gxldcptrick.mnote.commonLib.Event;
import com.gxldcptrick.mnote.commonLib.EventListener;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MNoteMultiServer {
    private boolean online;
    private ClientConnection currentClientSending;
    private Map<ClientConnection, List<DrawingEventArgs>> collectionOfLinesForClient;
    private final int NumberOfAllowableUsers;

    private DrawingPackage drawingPackage;

    public static void main(String[] args) { new MNoteMultiServer(10).start(); }
    public MNoteMultiServer(){ this(5); }
    public MNoteMultiServer(int numberOfAllowableUsers) {
        this.NumberOfAllowableUsers = numberOfAllowableUsers;
        packetSendingEvent = new Event<>();
        collectionOfLinesForClient = new ConcurrentHashMap<>();
    }
    public void Stop(){
        online = false;
    }
    public void start() {
        online = true;
        try (var serverSocket = new java.net.ServerSocket(4444)) {
            System.out.println("Listening On port: " + serverSocket.getLocalPort());
            ExecutorService clientConnnectionService = Executors.newFixedThreadPool(this.NumberOfAllowableUsers);
            do {
                if(packetSendingEvent.getSize() < this.NumberOfAllowableUsers){
                    /// creating a new client connection based on the request on the server socket.
                    var clientConnection = new ClientConnection(serverSocket.accept(), UUID.randomUUID());
                    drawingPackage = new DrawingPackage();
                    drawingPackage.setClientUUID(clientConnection.clientID.toString());
                    clientConnection.sendPackageToClient(drawingPackage);
                    if(!online) break;
                    EventListener<DrawingEventArgs> callback = (sender, args) -> {
                        if(sender != clientConnection) clientConnection.sendPackageToClient(args.POINT);
                    };
                    /// adding the client to the send all event so that we can send the points across the wire.
                    packetSendingEvent.subscribe(callback);
                    System.out.println("Client Connected on port " + clientConnection.getPort() +
                            " With ID OF: " + clientConnection.clientID);
                    /// connecting the server to the client so that the client is able to just send all the data to everyone.
                    clientConnection.clientSendingArgs.subscribe((sender, args) -> this.sendDataToAll(clientConnection, args));
                    /// adding the new connection to the executor service.
                    clientConnnectionService.submit(() -> {
                        /// starting the initial connection to the client and wait for a response.
                        clientConnection.start();
                        /// removing the client from the send all data event so that it can speed up the process.
                        packetSendingEvent.unsubscribe(callback);
                        try{
                            /// making sure to call close so that we can free up the resource if the port closed naturally.
                            clientConnection.close();
                        }catch(IOException e){
                            System.out.println("Something Went Wrong Closing The Client Connection");
                        }
                        System.out.println("ended connection to " + clientConnection.clientID + " on port: " + clientConnection.getPort());
                    });
                }

            }while (online);
        } catch (IOException  e) {
            e.printStackTrace();
        }
    }

    private Event<EventListener<DrawingEventArgs>, DrawingEventArgs> packetSendingEvent;

    private void sendDataToAll(ClientConnection connection , DrawingEventArgs aPackage) {
            packetSendingEvent.invoke(connection, aPackage);
    }
}

