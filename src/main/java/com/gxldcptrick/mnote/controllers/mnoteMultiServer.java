package com.gxldcptrick.mnote.controllers;

import com.gxldcptrick.mnote.models.SavablePoint2D;

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

    public void sendDataToAll(SavablePoint2D point2D) {
        for (Handler current : handlers) {
            try {
                current.sendPoints(point2D);
            } catch (IOException e) {

            }
        }
    }

    public mnoteMultiServer() {
        try (java.net.ServerSocket serverSocket = new java.net.ServerSocket(4444);) {
            while (true) {
                Handler noob = new Handler(serverSocket.accept(), this);
                handlers.add(noob);
                noob.start();
            }
        } catch (IOException e) {

        }
    }

    static class Handler extends Thread {
        Socket socket;
        mnoteMultiServer parent;
        ObjectInputStream in;
        ObjectOutputStream out;


        public Handler(Socket socket, mnoteMultiServer parent) {
            this.socket = socket;
            this.parent = parent;
        }

        public void sendPoints(SavablePoint2D point2D) throws IOException {
            out.writeObject(point2D);
        }

        @Override
        public void run() {
            try {
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());

                while (true) {

                    Object point2D = in.readObject();

                    if (SavablePoint2D.class.isInstance(point2D)) {

                        parent.sendDataToAll((SavablePoint2D) point2D);

                        System.out.println("Read savable point from somewhere");

                    }

                }
            } catch (IOException e) {

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
