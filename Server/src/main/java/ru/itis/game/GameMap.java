package ru.itis.game;

import java.nio.ByteBuffer;
import java.util.*;

public class GameMap {
    private final List<Snake> snakes;
    private final Map<Snake, Integer> orders;
    private final Tile[][] tiles;
    private int idToGive;

    private final int height;
    private final int width;

    private int foodX;
    private int foodY;
    private int foodSize;

    public static final int DEFAULT_FOOD_SIZE = 2;

    public GameMap(int height, int width) {
        this.height = height;
        this.width = width;
        tiles = new Tile[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = new Tile();
            }
        }
        idToGive = 1;
        snakes = new ArrayList<>();
        orders = new HashMap<>();
    }

    public int repairX(int x) {
        if (x < 0) return repairX(width + x);
        else {
            return x % width;
        }
    }

    public int repairY(int y) {
        if (y < 0) return repairY(height + y);
        else {
            return y % height;
        }
    }

    public void placeFood() {
        boolean ok = false;
        int iterations = 0;
        int x;
        int y;
        Random r = new Random();
        while (!ok) {
            if (iterations > 20) {
                break;
            }
            iterations++;
            x = r.nextInt(width);
            y = r.nextInt(height);
            if (getTile(x, y).getSnake() == null) {
                ok = true;
                foodX = x;
                foodY = y;
                foodSize = DEFAULT_FOOD_SIZE;
            }
        }
    }

    public void addSnake(Snake s) throws CannotPlaceSnakeException {
        boolean ok = false;
        int iteration = 0;
        int x = 0;
        int y = 0;
        Random random = new Random();
        while (!ok) {
            if (iteration > 20) {
                throw new CannotPlaceSnakeException();
            }
            x = random.nextInt(width);
            y = random.nextInt(height);
            if (tiles[x][y].getSnake() != null) {
                iteration++;
            } else {
                if (tiles[repairX(x + 1)][y].getSnake() == null) {
                    s.setDirection(Snake.RIGHT);
                    ok = true;
                } else if (tiles[x][repairY(y + 1)].getSnake() == null) {
                    s.setDirection(Snake.UP);
                    ok = true;
                } else if (tiles[repairX(x - 1)][y].getSnake() == null) {
                    s.setDirection(Snake.LEFT);
                    ok = true;
                } else if (tiles[x][repairY(y - 1)].getSnake() == null) {
                    s.setDirection(Snake.DOWN);
                    ok = true;
                } else {
                    iteration++;
                }
            }
        }
        s.setX(x);
        s.setY(y);
        tiles[x][y].setSnake(s);
        snakes.add(s);
        orders.put(s, -1);
        s.setId(idToGive++);
    }

    public void removeSnake(Snake s) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (tiles[x][y].getSnake() == s) {
                    tiles[x][y].setSnake(null);
                    tiles[x][y].setSnakeTail(0);
                }
            }
        }
        s.setAlive(false);
        snakes.remove(s);
        orders.remove(s);
    }

    public void addOrder(Snake s, int direction) {
        orders.put(s, direction);
    }

    public Tile getTile(int x, int y) {
        return tiles[repairX(x)][repairY(y)];
    }

    public void update() {
        if (foodSize == 0) {
            placeFood();
        }
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (tiles[x][y].getSnake() != null) {
                    tiles[x][y].increaseSnakeLength();
                }
            }
        }
        for (int i = 0; i < snakes.size(); i++) {
            Snake s = snakes.get(i);
            int direction = orders.get(s);
            if (direction != -1) {
                switch (direction) {
                    case Snake.LEFT:
                        s.left();
                        break;
                    case Snake.RIGHT:
                        s.right();
                        break;
                    case Snake.UP:
                        s.up();
                        break;
                    case Snake.DOWN:
                        s.down();
                        break;
                }
            }
            orders.put(s, -1);
            switch (s.getDirection()) {
                case Snake.LEFT:
                    if (getTile(s.getX() - 1, s.getY()).getSnake() != null) {
                        removeSnake(s);
                        i--;
                        break;
                    }
                    s.setX(repairX(s.getX() - 1));
                    break;
                case Snake.RIGHT:
                    if (getTile(s.getX() + 1, s.getY()).getSnake() != null) {
                        removeSnake(s);
                        i--;
                        break;
                    }
                    s.setX(repairX(s.getX() + 1));
                    break;
                case Snake.UP:
                    if (getTile(s.getX(), s.getY() - 1).getSnake() != null) {
                        removeSnake(s);
                        i--;
                        break;
                    }
                    s.setY(repairY(s.getY() - 1));
                    break;
                case Snake.DOWN:
                    if (getTile(s.getX(), s.getY() + 1).getSnake() != null) {
                        removeSnake(s);
                        i--;
                        break;
                    }
                    s.setY(repairY(s.getY() + 1));
                    break;
            }
            getTile(s.getX(), s.getY()).setSnake(s);
            if (s.getX() == foodX && s.getY() == foodY) {
                s.feed(foodSize);
                foodSize = 0;
                placeFood();
            }
        }
    }

    public byte[] toBytes() {
        int length = width * height + 20;
        ByteBuffer buffer = ByteBuffer.allocate(length);
        buffer.putInt(width).putInt(height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Snake s = tiles[x][y].getSnake();
                if (s == null) {
                    buffer.put((byte) 0);
                } else {
                    buffer.put((byte) s.getId());
                }
            }
        }
        buffer.putInt(foodX).putInt(foodY).putInt(foodSize);
        return buffer.array();
    }
}
