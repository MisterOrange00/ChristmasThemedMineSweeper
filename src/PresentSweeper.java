import java.util.Random;

public class PresentSweeper {

  private static final int SIZE = 10;

  private static final int NUM_PRESENTS = 10;

  private static final int PRESENT = -1;

  private static final int BLANK = 0;

  private int[][] grid = new int[SIZE][SIZE];

  private boolean[][] revealed = new boolean[SIZE][SIZE];

  /* game state (initialised to false by default) */
  private boolean gameOver = false;
  private boolean gameWon = false;
  private boolean gameStarted = false;
  private Random rand;

  public PresentSweeper(long seed) {
    rand = new Random(seed);
  }

  public PresentSweeper() {
    rand = new Random();
  }

  private void generateGrid(int inputX, int inputY) {

    /* set everything to BLANK and false */
    for (int y = 0; y < SIZE; y++) {
      for (int x = 0; x < SIZE; x++) {
        grid[x][y] = BLANK;
        revealed[x][y] = false;
      }
    }

    int count = 0;

    while (count < NUM_PRESENTS) {
      int randX = rand.nextInt(SIZE);
      int randY = rand.nextInt(SIZE);
      int toCheck = grid[randX][randY];
      if (toCheck == BLANK && (randX != inputX && randY != inputY)) {
        grid[randX][randY] = PRESENT;
        count++;
      }
    }
  }

  private boolean isInside(int x, int y) {
    return (x >= 0 && y >= 0) && (x < SIZE && y < SIZE);
  }

  private void calculateClues() {
    for (int y = 0; y < SIZE; y++) {
      for (int x = 0; x < SIZE; x++) {
        if (grid[x][y] == PRESENT) {
          calculateClues(x, y);
        }
      }
    }
  }

  private void calculateClues(int x, int y) {

    for (int deltaY = -1; deltaY < 2; deltaY++) {
      for (int deltaX = -1; deltaX < 2; deltaX++) {
        int newX = x + deltaX;
        int newY = y + deltaY;
        if (isInside(newX, newY) && grid[newX][newY] != PRESENT) {
          /*
           * no need to check for case newX == x and newY == y as
           * it is itself a present
           */
          grid[newX][newY] += 1;
        }
      }
    }
  }

  public void move(int x, int y) {
    if (!isInside(x, y)) {
      return;
    }
    if (!gameStarted) {
      generateGrid(x, y);
      calculateClues();
      gameStarted = true;
    }

    if (!gameOver && !revealed[x][y]){
      clearAdjacentBlanks(x, y);
    }
    updateGameStatus(x, y);
  }

  private void clearAdjacentBlanks(int x, int y) {
    if (grid[x][y] == PRESENT) {
      return;
    }
    revealed[x][y] = true;
    if (grid[x][y] != BLANK) {
      return;
    }
    for (int deltaY = -1; deltaY < 2; deltaY++) {
      for (int deltaX = -1; deltaX < 2; deltaX++) {
        int newX = x + deltaX;
        int newY = y + deltaY;
        if (isInside(newX, newY) && !revealed[newX][newY]) {
          clearAdjacentBlanks(newX, newY);
        }
      }
    }
  }

  private void updateGameStatus(int x, int y) {
    if (grid[x][y] == PRESENT) {
      lost();
    } else if (isAllButPresentRevealed()) {
      win();
    }
  }

  private void revealAll() {
    for (int y = 0; y < SIZE; y++) {
      for (int x = 0; x < SIZE; x++) {
        revealed[x][y] = true;
      }
    }
  }

  private boolean isAllButPresentRevealed() {
    for (int checkY = 0; checkY < SIZE; checkY++) {
      for (int checkX = 0; checkX < SIZE; checkX++) {
        if (grid[checkX][checkY] != PRESENT && !revealed[checkX][checkY]) {
          return false;
        }
      }
    }
    return true;
  }

  private void lost() {
    gameOver = true;
    revealAll();
  }

  private void win() {
    gameOver = true;
    gameWon = true;
    revealAll();
  }

  public String toString() {
    StringBuilder result = new StringBuilder(toStringHeader());
    for (int y = 0; y < SIZE; y++) {
      for (int x = 0; x < SIZE; x++) {
        if (x == 0) {
          result.append(y);
        }
        result.append(" ");
        int toCheck = grid[x][y];
        if (revealed[x][y]) {
          if (toCheck == BLANK) {
            result.append(" ");
          } else if (toCheck == PRESENT) {
            if (gameOver) {
              if (gameWon) {
                result.append("P");
              } else {
                result.append("Q");
              }
            }
          } else {
            result.append(toCheck);
          }
        } else {
          result.append("X");
        }
      }
      result.append("\n");
    }
    return result.toString();
  }

  private String toStringHeader() {
    StringBuilder topRowHeader = new StringBuilder("  ");
    for (int x = 0; x < SIZE; x++) {
      if (x > 0) {
        topRowHeader.append(" ");
      }
      topRowHeader.append(x);
    }
    topRowHeader.append("\n");
    return topRowHeader.toString();
  }

  public boolean isGameStarted() {
    return gameStarted;
  }

  public boolean isGameOver() {
    return gameOver;
  }

  public boolean isGameWon() {
    return gameWon;
  }
}
