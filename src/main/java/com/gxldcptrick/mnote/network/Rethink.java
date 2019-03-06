package com.gxldcptrick.mnote.network;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gxldcptrick.mnote.events.CanvasEvents;
import com.gxldcptrick.mnote.events.CanvasToolbarEvents;
import com.gxldcptrick.mnote.events.RethinkEvents;
import com.gxldcptrick.mnote.models.Brush;
import com.gxldcptrick.mnote.models.DrawingPackage;
import com.rethinkdb.RethinkDB;
import com.rethinkdb.gen.exc.ReqlDriverError;
import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Cursor;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Rethink extends Thread{
    private static final RethinkDB r = RethinkDB.r;
    private Connection conn;
    private String linesTable;
    private String sessionsTable;
    private String dbName;
    private String host;
    private int port;
    private String lineId;
    private String clientId;
    private boolean connectedToRethinkDB;
    private boolean sessionHost;
    private Cursor changeCursor;
    private Cursor linesCursor;
    private String sessionId;
    private List<String> listOfClients;
    private Brush brush;

    private Rethink(){
        this.host = "localhost";
        this.port = 28015;
        this.linesTable = "lines";
        this.sessionsTable = "sessions";
        this.dbName = "mnote";
        this.connectedToRethinkDB = false;
        this.sessionHost = false;
        listOfClients = new ArrayList<>();
    }

    public Rethink(String sessionId, String clientId){
        this();
        this.clientId = clientId;
        this.sessionId = sessionId;
        try{
            this.conn = r.connection().hostname(host).port(port).db(dbName).connect();
            connectedToRethinkDB = true;
            listOfClients.add(clientId);
            brush = new Brush();
            System.out.println("Set up brush " + brush.getCurrentWidth());
            subscribeToCanvasToolBarEvents();
        }
        catch(ReqlDriverError e){
            new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK).showAndWait();
        }
        System.out.println(this.getThreadGroup().getName() + "Thread name");
    }

    public void startSession(){
        Long count = r.table(sessionsTable).getAll(this.sessionId)
                .optArg("index", "sessionId").count().run(conn);

        if (count == 0){
            r.table(sessionsTable)
                    .insert(r.hashMap("sessionId", this.sessionId)
                            .with("listOfClients", r.array(clientId)))
                    .run(conn);

            sessionHost = true;
            RethinkEvents.getInstance().getOnline().onNext(connectedToRethinkDB);
            RethinkEvents.getInstance().getSessionId().onNext(sessionId);
            emitLinesBeingDrawn();
            this.start();
        }
        else{
            new Alert(Alert.AlertType.ERROR, "Session ID already exists try a different one!", ButtonType.OK).showAndWait();
        }
    }

    public void joinSession(){
        Long count = r.table(sessionsTable).getAll(this.sessionId)
                .optArg("index", "sessionId").count().run(conn);
        if (count == 1){
            r.table(sessionsTable)
                    .getAll(this.sessionId)
                    .optArg("index", "sessionId")
                    .update(row -> r.hashMap("listOfClients",
                            row.g("listOfClients").append(clientId)))
                    .run(conn);
            RethinkEvents.getInstance().getOnline().onNext(connectedToRethinkDB);
            RethinkEvents.getInstance().getSessionId().onNext(sessionId);
            emitLinesBeingDrawn();
            this.start();
        }
        else{
            new Alert(Alert.AlertType.ERROR, "Session ID doesn't exists!", ButtonType.OK).showAndWait();
        }
    }

    public boolean connectedToRethink(){
        return connectedToRethinkDB;
    }

    @Override
    public void run() {
        super.run();
        subscribeToCanvasEvents();
        subscribeToCanvasToolBarEvents();
        subscribeToClientEvents();
        monitorClientsConnected();
    }

    private void emitLinesBeingDrawn() {
        new Thread(() ->{
            ObjectMapper mapper = new ObjectMapper();
            DrawingPackage drawingPackage;
            linesCursor = r.table(linesTable)
                    .changes()
                    .g("new_val")
                    .run(conn);

            for (Object line : linesCursor) {
                try {
                    System.out.println("-------------------Before sending drawing package-----------------");
                    drawingPackage = mapper.readValue(toJson(line.toString()), DrawingPackage.class);
                    System.out.println("-------------------After sending drawing package-----------------");
                    RethinkEvents.getInstance().getLinesBeingDrawn().onNext(drawingPackage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(line);
            }
        }).start();
    }

    private void monitorClientsConnected() {
        String json;
        String tempClient;
        changeCursor = r.table(sessionsTable)
                .filter(session -> session.g("sessionId").eq(sessionId))
                .g("listOfClients")
                .changes()
                .run(conn);

        for(Object change : changeCursor){
            System.out.println(change);
            json = toJson(change.toString());
            System.out.println(json);
            try {
                final JsonNode clients = new ObjectMapper().readTree(json).get("new_val");
                for (JsonNode client : clients) {
                    tempClient = client.toString().replaceAll("\"", "");
                    if (!listOfClients.contains(tempClient)){
                        listOfClients.add(tempClient);
                        System.out.println("New Client connected!");
                        RethinkEvents.getInstance().getNewClientConnected().onNext(tempClient);
                    }
                }
            }
            catch(IOException e){
                System.out.println(e.getMessage());
            }
        }
    }

    public String toJson(String jsonString){
        jsonString = jsonString.replaceAll("([\\w]+)[ ]*=", "\"$1\" =");
        jsonString = jsonString.replaceAll("=[ ]*([\\w-]+)", "= \"$1\"");
        jsonString = jsonString.replaceAll("(?!\")(\\b[0-9a-f]{8}\\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\\b[0-9a-f]{12}\\b)(?!\")", "\"$1\"");
        jsonString = jsonString.replaceAll("=", ":");
        jsonString = jsonString.replaceAll("\"(\\d+)\"", "$1");
        jsonString = jsonString.replaceAll("\"(\\bnull\\b|\\btrue\\b)\"", "$1");

        return jsonString;
    }

    private void subscribeToClientEvents() {
        RethinkEvents.getInstance().getDisconnect().subscribe(
                this::disconnect,
                System.out::println,
                System.out::println);
    }

    private void disconnect(WindowEvent windowEvent) {
        close();
    }

    public void close(){
        System.out.println("Disconnecting client");
        changeCursor.close();
        linesCursor.close();
        System.out.println("Delete session id row");
        RethinkEvents.getInstance().getSessionId().onNext("");

        if (sessionHost){
            r.table(sessionsTable)
                    .getAll(this.sessionId)
                    .optArg("index", "sessionId")
                    .delete()
                    .run(conn);
        }
        else{
            var result = r.table(sessionsTable)
                    .getAll(this.sessionId)
                    .optArg("index", "sessionId")
                    .update(
                            doc -> r.hashMap("listOfClients",
                                    doc.g("listOfClients")
                                            .difference(r.array(clientId)))
                    ).run(conn);
            System.out.println(result);
        }

        conn.close();
        connectedToRethinkDB = false;

        RethinkEvents.getInstance().getOnline().onNext(connectedToRethinkDB);
    }

    @SuppressWarnings("Duplicates")
    private void subscribeToCanvasEvents() {
        CanvasEvents.getInstance().getMouseDownEvents().subscribe(
                this::startLine,
                System.out::println,
                System.out::println);
        CanvasEvents.getInstance().getMouseDragEvents().subscribe(
                this::writeLine,
                System.out::println,
                System.out::println);
        CanvasEvents.getInstance().getMouseUpEvents().subscribe(
                this::endLine,
                System.out::println,
                System.out::println);
    }

    @SuppressWarnings("Duplicates")
    private  void subscribeToCanvasToolBarEvents() {
        CanvasToolbarEvents.getInstance().getClearCanvas().subscribe(
                this::deleteAllUserLines,
                System.out::println,
                System.out::println);

        CanvasToolbarEvents.getInstance().getChangedColor().subscribe(brush::setColor);
        CanvasToolbarEvents.getInstance().getChangedLineSize().subscribe(brush::setCurrentWidth);
        CanvasToolbarEvents.getInstance().getDeletingLine().subscribe(actionEvent -> System.out.println("Need to make this work!!!! "));
        CanvasToolbarEvents.getInstance().getChangeSpecialEfects().subscribe(specialEffect -> {
            brush.setEffect(specialEffect);
            System.out.println("Rethinkd brush effect " + specialEffect.lineEffect.toString());
        });
    }

    private void startLine(MouseEvent mouseEvent) {
        Map insert = r.table(linesTable)
                .insert(r.hashMap("clientId", clientId)
                        .with("coordinates", r.array(r.hashMap("x", mouseEvent.getX()).with("y", mouseEvent.getY())))
                        .with("brush", brush))
                .run(conn);
        System.out.println("Sending StartLine to Rethinkdb");

        ArrayList generated_keys = (ArrayList)insert.get("generated_keys");
        lineId = generated_keys.get(0).toString();
    }

    public void writeLine(MouseEvent mouseEvent) {
        System.out.println("Called writeLine");
        drawLine(mouseEvent);
    }

    private void endLine(MouseEvent mouseEvent) {
        System.out.println("Called end line");
        drawLine(mouseEvent);
    }

    private void drawLine(MouseEvent mouseEvent){
        Map status = r.table(linesTable).get(lineId)
                .update(row -> r.hashMap("coordinates", row.g("coordinates")
                        .append(r.hashMap("x", mouseEvent.getX())
                                .with("y", mouseEvent.getY()))))
                .run(conn);

        if ((long)status.get("replaced") == 1L){
            System.out.println("Successfully sent and added point to line");
        }
    }

    private void deleteAllUserLines(ActionEvent actionEvent) {
        System.out.println("Deleted all lines from rethinkDB");
        Map map = r.table(linesTable).getAll(clientId)
                .optArg("index", "clientId").delete().run(conn);
        if (map != null){
            for(var status : map.keySet())
                System.out.println(status + ": " + map.get(status));
        }
        else
            System.out.println("Map is null");

    }
}
