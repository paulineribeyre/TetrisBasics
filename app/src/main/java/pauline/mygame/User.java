package pauline.mygame;

public class User {

    // game settings
    public static boolean useRandomColors = true;
    public static boolean touchToMove = true;
    public static int nbCellsX = 10;
    public static int nbCellsY = 20;

    // game statistics
    public static int bestScore = 0;
    public static TetrisMatrix currentGame;

    // score progress
    public static LevelHandler levelHandler;

    public User(){
        startNewGame();
    }

    public static void startNewGame() {
        currentGame = new TetrisMatrix(nbCellsY, nbCellsX); // create the matrix *after* initializing the number of cells
        levelHandler = new LevelHandler();
    }

}