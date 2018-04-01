package com.gxldcptrick.mnote.controllers;

import com.gxldcptrick.mnote.models.SavablePoint2D;
import com.gxldcptrick.mnote.views.DrawingBoard;

import java.io.*;
import java.net.Socket;

public class ClientSocket extends Thread {
    SavablePoint2D savablePoint2DSent = null;
    DrawingBoard drawingBoard = new DrawingBoard();

    @Override
    public void run() {
        try (
                Socket socket = new Socket("localhost", 4444);
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ) {

            while (true) {
                SavablePoint2D point2D;





                //comment from here

                try {
                    point2D = (SavablePoint2D) in.readObject();
                    System.out.println(point2D.get2DPoint().getX());
                    drawingBoard.drawLine(point2D);

                } catch (IOException e) {

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                //To here
                //Uncomment block below



/*
                if (savablePoint2DSent != null) {
                    System.out.println("Shoudl send somethings");
                    out.writeObject(savablePoint2DSent);
                    savablePoint2DSent = null;
                }
                try {
                    Thread.sleep(125);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


*/


            }

        } catch (IOException e) {

        }
    }

    public void sendObject(SavablePoint2D savablePoint2D) {
        savablePoint2DSent = savablePoint2D;
    }
}
