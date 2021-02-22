package ru.itis.game;

public class Snake {
    private  int id;
    private int length;
    private int direction;
    private int x;
    private int y;
    private boolean alive;

    public static final int UP = 0;
    public static final int RIGHT = 1;
    public static final int DOWN = 2;
    public static final int LEFT = 3;

    public Snake(){
        length = 2;
        alive = true;
    }

    public void feed(int points){
        length += points;
    }

    public void down(){
        if(direction == RIGHT || direction == LEFT){
            direction = DOWN;
        }
    }

    public void up(){
        if(direction == RIGHT || direction == LEFT){
            direction = UP;
        }
    }

    public void left(){
        if(direction == UP || direction == DOWN){
            direction = LEFT;
        }
    }

    public void right(){
        if(direction == UP || direction == DOWN){
            direction = RIGHT;
        }
    }

    public int getLength() {
        return length;
    }

    public int getDirection() {
        return direction;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
