package ru.itis.server;

import ru.itis.game.GameMap;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private List<Connection> connections;
    private GameMap gameMap;
    private ServerSocket serverSocket;
    private boolean exit;


    public static final int HEIGHT = 30;
    public static final int WIDTH = 30;

    public void init(){
        connections = new ArrayList<>();
        gameMap = new GameMap(HEIGHT, WIDTH);
        exit = false;
        try {
            serverSocket = new ServerSocket(1001);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void start(){
        Thread connectionAcceptThread = new Thread(() -> {
            while (!exit) {
                Socket s;
                try {
                    s = serverSocket.accept();
                } catch (IOException e) {
                    throw new IllegalArgumentException(e);
                }
                connections.add(new Connection(s, this));
            }
        });
        connectionAcceptThread.start();
        while (!exit){
            gameMap.update();
            sendMapData();
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    private void sendMapData() {
        byte[] data = gameMap.toBytes();
        Connection c;
        for(int i = 0; i < connections.size(); i++){
            c = connections.get(i);
            try {
                c.sendMessage(new Message(Protocol.MAP_DATA, data));
            } catch (IOException ioException) {
                c.closeConnection();
            }
        }
    }

    public void removeConnection(Connection c){
        connections.remove(c);
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.init();
        server.start();
    }

    public GameMap getGameMap() {
        return gameMap;
    }
}
