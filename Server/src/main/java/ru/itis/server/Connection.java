package ru.itis.server;

import ru.itis.game.CannotPlaceSnakeException;
import ru.itis.game.Snake;

import java.io.IOException;
import java.net.Socket;

public class Connection {
    private Socket s;
    private Server server;
    private ProtocolInputStream in;
    private ProtocolOutputStream out;
    private Snake snake;

    public Connection(Socket s, Server server) {
        this.s = s;
        this.server = server;
        try {
            in = new ProtocolInputStream(s.getInputStream());
            out = new ProtocolOutputStream(s.getOutputStream());
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        snake = new Snake();
        try {
            server.getGameMap().addSnake(snake);
        } catch (CannotPlaceSnakeException e) {
            e.printStackTrace();
            closeConnection();
        }
        Thread connectionThread = new Thread(() -> {
            Message m = null;
            try {
                m = in.readMessage();
            } catch (IOException e) {
                //ignore
            }
            while (m != null && snake.isAlive()) {
                if (m.getType() == Protocol.ORDER) {
                    int direction = m.getData()[0];
                    server.getGameMap().addOrder(snake, direction);
                }
                try {
                    m = in.readMessage();
                } catch (IOException e) {
                    //ignore
                }
            }
            if(s.isConnected()){
                try {
                    out.writeMessage(new Message(Protocol.LOSE));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            closeConnection();
        });
        connectionThread.start();
    }

    public void closeConnection(){
        try {
            s.close();
        } catch (IOException ioException) {
            //ignore
        }
        server.getGameMap().removeSnake(snake);
        server.removeConnection(this);
    }

    public void sendMessage(Message m) throws IOException {
        out.writeMessage(m);
    }
}
