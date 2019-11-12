import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PresentSweeperTest {

  private PresentSweeper presentSweeper = new PresentSweeper(10);

  @Test
  public void gameDoesNotStartUntilFirstMove() {
    assertFalse(presentSweeper.isGameStarted());
    assertFalse(presentSweeper.isGameOver());
    presentSweeper.move(0, 0);
    assertTrue(presentSweeper.isGameStarted());
    assertFalse(presentSweeper.isGameOver());
  }

  @Test
  public void gameCanBeLost() {
    presentSweeper.move(0, 0);
    presentSweeper.move(1, 4);
    assertTrue(presentSweeper.isGameOver());
  }

  @Test
  public void gameCanBeWon() {
    for (int x = 0; x < 10; x++) {
      for (int y = 0; y < 10; y++) {
        if ((x == 1 && y == 4) ||
            (x == 1 && y == 8) ||
            (x == 3 && y == 8) ||
            (x == 3 && y == 9) ||
            (x == 5 && y == 8) ||
            (x == 5 && y == 9) ||
            (x == 6 && y == 3) ||
            (x == 6 && y == 6) ||
            (x == 7 && y == 8) ||
            (x == 8 && y == 2)) {
          continue;
        }
        presentSweeper.move(x, y);
      }
    }
    assertTrue(presentSweeper.isGameOver());
  }
}
