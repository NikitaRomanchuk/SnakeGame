package ru.itis.client;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class GameMap {
    private Tile[][] tiles;

    private int height;
    private int width;

    private int foodX;
    private int foodY;
    private int foodSize;
    private Map<Integer, Color> idToColorMap;

    public GameMap(int height, int width) {
        this.height = height;
        this.width = width;
        tiles = new Tile[width][height];
        idToColorMap = new HashMap<>();
        for(int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                tiles[x][y] = new Tile();
                tiles[x][y].setSnakeId(0);
            }
        }
    }

    public void update(byte[] data){
        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.getInt();
        buffer.getInt();
        int snakeId;
        int iter = 0;
        for(int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                snakeId = buffer.get();
                tiles[x][y].setSnakeId(snakeId);
                if(snakeId != 0 && !idToColorMap.containsKey(snakeId)){
                    idToColorMap.put(snakeId, Color.color(Math.random(), Math.random(), Math.random()));
                }
            }
        }
        foodX = buffer.getInt();
        foodY = buffer.getInt();
        foodSize = buffer.getInt();
    }

    public void draw(Canvas canvas){

        int h = (int) (canvas.getHeight() / height);
        int w = (int) (canvas.getWidth() / width);
        GraphicsContext context = canvas.getGraphicsContext2D();
        context.setFill(Color.WHITE);
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for(int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                if(tiles[x][y].getSnakeId() != 0) {
                    context.setFill(idToColorMap.get(tiles[x][y].getSnakeId()));
                    context.fillRect(x * w, y * h, w, h);
                }
            }
        }
        context.setFill(Color.BLACK);
        context.fillRect(foodX * w + 5, foodY * h + 5, w - 10, h - 10);
        context.save();
    }
}
