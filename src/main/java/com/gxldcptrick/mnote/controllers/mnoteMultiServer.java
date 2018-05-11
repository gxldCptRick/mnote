package com.gxldcptrick.mnote.controllers;

import com.gxldcptrick.mnote.models.DrawingPackage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;

public class mnoteMultiServer {
    LinkedList<Handler> handlers = new LinkedList<>();

    public static void main(String[] args) {
        new mnoteMultiServer();
    }

    public void sendDataToAll(DrawingPackage aPackage, int port) {

        for (Handler current : handlers) {
            try {

                if (current.getPort() != port && current.isConnected())
                    System.out.println("Sent package");

                    current.sendPoints(aPackage);

            } catch (IOException e) {

                e.printStackTrace();
            }

        }
    }

    public mnoteMultiServer() {
        try (java.net.ServerSocket serverSocket = new java.net.ServerSocket(4444);) {
            while (true) {
                Handler noob = new Handler(serverSocket.accept(), this);
                System.out.println("Client connected");
                handlers.add(noob);
                noob.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class Handler extends Thread {
        Socket socket;
        mnoteMultiServer parent;
        ObjectInputStream in;
        ObjectOutputStream out;
        private static int portNumbers = 0;
        private int port;


        public Handler(Socket socket, mnoteMultiServer parent) {
            this.socket = socket;
            this.parent = parent;
            this.port = portNumbers++;
        }

        public boolean isConnected() { return socket.isConnected();}
        public int getPort() {
            return port;
        }

        public void sendPoints(DrawingPackage aPackage) throws IOException {
            out.writeObject(aPackage);
        }

        @Override
        public void run() {
            try {
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());

                while (socket.isConnected()) {
                    System.out.println("Waiting for package...");
                    Object aPackage = in.readObject();
                    System.out.println("Read savable point from somewhere");
                    parent.sendDataToAll((DrawingPackage) aPackage, port);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();

            }
        }
    }
}
