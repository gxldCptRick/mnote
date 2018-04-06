package com.gxldcptrick.mnote.controllers;

import com.gxldcptrick.mnote.models.DrawingPackage;
import com.gxldcptrick.mnote.models.SavablePoint2D;
import com.gxldcptrick.mnote.views.DrawingBoard;

import java.io.*;
import java.net.Socket;

public class ClientSocket extends Thread {
    private DrawingPackage drawingPackageSent = null;
    private DrawingPackage drawingPackageRead = null;

    private DrawingBoard drawingBoard = new DrawingBoard();
    static private Socket socket;
    static private ObjectOutputStream out;
    static private ObjectInputStream in;

    @Override
    public void run() {

        Runnable send = () -> {
            System.out.println("Send thread started");
            while (true) {
                if (drawingPackageSent != null) {
                    if (out == null) {
                        System.out.println("NULL");
                    }
                    try {
                        out.writeObject(drawingPackageSent);
                    } catch (IOException e) {
                    }
                    drawingPackageSent = null;

                }
                try {
                    Thread.sleep(125);
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
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        };

        try {
            socket = new Socket("localhost", 4444);
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

    public void sendObject(DrawingPackage aPackage) {
        drawingPackageSent = aPackage;
    }
}
