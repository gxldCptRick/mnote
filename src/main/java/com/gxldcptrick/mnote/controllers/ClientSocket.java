package com.gxldcptrick.mnote.controllers;

import com.gxldcptrick.mnote.models.DrawingPackage;
import com.gxldcptrick.mnote.models.SavablePoint2D;
import com.gxldcptrick.mnote.views.DrawingBoard;

import java.io.*;
import java.net.Socket;

public class ClientSocket extends Thread {
    private SavablePoint2D savablePoint2DSent = null;
    private DrawingBoard drawingBoard;
    private SavablePoint2D savablePoint2DRead = null;
    private DrawingPackage drawingPackageSent = null;
    private DrawingPackage drawingPackageRead = null;

    static private Socket socket;
    static private ObjectOutputStream out;
    static private ObjectInputStream in;
    private boolean connected;

    public ClientSocket(DrawingBoard board) {
        this.drawingBoard = board;
        connected = true;
    }

    @Override
    public void run() {

        Runnable send = () -> {

            System.out.println("Send thread started");

            while (connected) {

                if (drawingPackageSent != null) {
                    if (out == null) {
                        System.out.println("NULL");
                    }

                    try {
                        out.writeObject(drawingPackageSent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    drawingPackageSent = null;

                }

                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Runnable read = () -> {

            System.out.println("Read thread started");

            try {

                while ((drawingPackageRead = (DrawingPackage) in.readObject()) != null) {
                    
                    drawingBoard.drawLine(drawingPackageRead);
                }

            } catch (IOException | ClassNotFoundException e) {

                e.printStackTrace();
            }

        };

        try {
            socket = new Socket("192.168.1.2", 4444);
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());


            Thread s = new Thread(send);
            Thread r = new Thread(read);
            s.start();
            r.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void killConnection() {

        this.connected = false;
    }

    public void sendObject(DrawingPackage aPackage) {
        drawingPackageSent = aPackage;
    }
}



