package ru.itis.game;

public class Tile {
    private Snake snake;
    private int snakeTail;

    public void increaseSnakeLength(){
        snakeTail++;
        if(snakeTail >= snake.getLength()){
            snake = null;
            snakeTail = 0;
        }
    }

    public Snake getSnake() {
        return snake;
    }

    public void setSnake(Snake snake) {
        this.snake = snake;
    }

    public int getSnakeTail() {
        return snakeTail;
    }

    public void setSnakeTail(int snakeTail) {
        this.snakeTail = snakeTail;
    }
}
