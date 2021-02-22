import org.junit.Test;
import ru.itis.game.CannotPlaceSnakeException;
import ru.itis.game.GameMap;
import ru.itis.game.Snake;

public class UnitTest {
    @Test
    public void snakeLengthTest() throws CannotPlaceSnakeException {
        GameMap gameMap = new GameMap(30, 30);
        Snake snake = new Snake();
        gameMap.addSnake(snake);
        assert snake.getLength() > 0;
    }

    @Test
    public void snakeEatenTest() {
        Snake snake = new Snake();
        snake.feed(10);
        assert snake.getLength() > 10;
    }
}
