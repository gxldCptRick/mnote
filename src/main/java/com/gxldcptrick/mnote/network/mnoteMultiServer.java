package com.gxldcptrick.mnote.network;

import com.gxldcptrick.mnote.commonLib.Event;
import com.gxldcptrick.mnote.FXView.models.DrawingPackage;
import com.gxldcptrick.mnote.commonLib.EventListener;

import java.io.IOException;

public class mnoteMultiServer {
    public static void main(String[] args) { new mnoteMultiServer().start(); }
    private mnoteMultiServer() {
        connectedClients = new Event<>();
    }
    private void start() {
        try (var serverSocket = new java.net.ServerSocket(4444)) {
            while (!serverSocket.isClosed()) {

                /// creating a new client connection based on the request on the server socket.
                var clientConnection = new ClientConnection(serverSocket.accept());
                EventListener<DrawingEventArgs> callback = (sender, args) -> {
                    if(sender != clientConnection) clientConnection.sendPackageToClient(args.POINT);
                };
                /// adding the client to the send all event so that we can send the points across the wire.
                connectedClients.subscribe(callback);
                System.out.println("Client Connected on port " + clientConnection.getPort());

                /// connecting the server to the client so that the client is able to just send all the data to everyone.
                clientConnection.clientSendingArgs.subscribe((sender, args) -> this.sendDataToAll(clientConnection, args.POINT));
                Thread clientThread  = new Thread(() -> {
                    /// starting the initial connection to the client and wait for a response.
                    clientConnection.start();
                    /// removing the client from the send all data event so that it can speed up the process.
                    connectedClients.unsubscribe(callback);
                    try{
                        /// making sure to call close so that we can free up the resource if the port closed naturally.
                        clientConnection.close();
                    }catch(IOException e){
                        System.out.println("Something Went Wrong Closing The Client Connection");
                    }
                    System.out.println("ended connection to " + clientConnection.getPort());
                });

                /// spinning the new thread for the clients.
                clientThread.start();
            }
        } catch (IOException  e) {
            e.printStackTrace();
        }
    }

    private Event<EventListener<DrawingEventArgs>, DrawingEventArgs> connectedClients;
    private void sendDataToAll(ClientConnection connection , DrawingPackage aPackage) {
        connectedClients.invoke(connection, new DrawingEventArgs(aPackage));
    }
}

