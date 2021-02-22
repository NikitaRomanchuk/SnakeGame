package ru.itis.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class App extends Application {
    private Socket socket;

    private ProtocolInputStream in;
    private ProtocolOutputStream out;

    private Canvas canvas;
    private  GameMap gameMap;

    public static final int UP = 0;
    public static final int RIGHT = 1;
    public static final int DOWN = 2;
    public static final int LEFT = 3;

    public static final int HEIGHT = 30;
    public static final int WIDTH = 30;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        socket = new Socket(InetAddress.getLocalHost(), 1001);
        in = new ProtocolInputStream(socket.getInputStream());
        out = new ProtocolOutputStream(socket.getOutputStream());
        Parent root = FXMLLoader.load(getClass().getResource("/gameStage.fxml"));
        canvas = (Canvas) root.lookup("#gameCanvas");
        Scene scene = new Scene(root);
        scene.setOnKeyPressed(e -> {
            try {
                if (e.getCode() == KeyCode.W) {
                    out.writeMessage(new Message(Protocol.ORDER, new byte[]{(byte) UP}));
                }
                if (e.getCode() == KeyCode.A) {
                    out.writeMessage(new Message(Protocol.ORDER, new byte[]{(byte) LEFT}));
                }
                if (e.getCode() == KeyCode.S) {
                    out.writeMessage(new Message(Protocol.ORDER, new byte[]{(byte) DOWN}));
                }
                if (e.getCode() == KeyCode.D) {
                    out.writeMessage(new Message(Protocol.ORDER, new byte[]{(byte) RIGHT}));
                }

            }
            catch (IOException ioException){
                throw new IllegalArgumentException(ioException);
            }
        });
        gameMap = new GameMap(HEIGHT, WIDTH);
        Thread drawThread = new Thread(() -> {
            Message m;
            try {
                m = in.readMessage();
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
            while (m != null) {
                if(m.getType() == Protocol.MAP_DATA){
                    gameMap.update(m.getData());
                    gameMap.draw(canvas);
                }
                if(m.getType() == Protocol.LOSE){
                    try {
                        socket.close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    System.exit(0);
                }
                try {
                    m = in.readMessage();
                } catch (IOException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });
        drawThread.setDaemon(true);
        drawThread.start();
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
