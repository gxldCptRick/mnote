package com.gxldcptrick.mnote.controllers;

import com.gxldcptrick.mnote.models.SavablePoint2D;
import com.gxldcptrick.mnote.views.DrawingBoard;

import java.io.*;
import java.net.Socket;

public class ClientSocket extends Thread {
    private SavablePoint2D savablePoint2DSent = null;
    private SavablePoint2D savablePoint2DRead = null;
    private DrawingBoard drawingBoard;

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

                if (savablePoint2DSent != null) {

                    if (out == null) {
                        System.out.println("NULL");
                    }

                    try {
                        out.writeObject(savablePoint2DSent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    savablePoint2DSent = null;

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

                while ((savablePoint2DRead = (SavablePoint2D) in.readObject()) != null) {

                    drawingBoard.drawLine(savablePoint2DRead);

                }

            } catch (IOException | ClassNotFoundException e) {

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

    public void killConnection(){

        this.connected = false;
    }

    public void sendPoint(SavablePoint2D savablePoint2D) {
        savablePoint2DSent = savablePoint2D;
    }
}
