package pauline.mygame;

import java.io.Serializable;

public class LevelHandler implements Serializable {

    private int score;
    private int nbOfClearedRows;
    private int level;

    private int moveDelay;
    private final int speedIncrease;
    private final int minMoveDelay;

    public boolean isFast;
    private int fastMoveDelay;

    public LevelHandler() {

        score = 0;
        nbOfClearedRows = 0;
        level = 1;
        moveDelay = 800; // initial delay between each drop of the current piece

        isFast = false;
        fastMoveDelay = 100; // drop delay when the fast drop is turned on

        speedIncrease = 50; // the drop delay decreases when the player goes on to the next level
        minMoveDelay = 100; // the drop delay cannot go lower than this threshold

    }

    public void addFastPoints(int points) {

        score += points;

        if (score > User.bestScore)
            User.bestScore = score;

        if (nbOfClearedRows >= 10 * level)
            increaseLevel();

    }

    public void addPoints(int nbOfClearedRows) {

        int base = nbOfClearedRows == 1 ? 40 : (nbOfClearedRows == 2 ? 100 : (nbOfClearedRows == 3 ? 300 : 1200));
        this.nbOfClearedRows += nbOfClearedRows;

        addFastPoints(base * level);

    }

    // when the player goes on to the next level, the drop delay decreases
    private void increaseLevel() {
        level++;
        if (moveDelay - speedIncrease >= minMoveDelay) {
            moveDelay -= speedIncrease;
        }
    }

    public void dropFastSpeed() {
        isFast = true;
    }

    public void dropNormalSpeed() {
        isFast = false;
    }

    public int getLevel() {
        return level;
    }

    public int getScore() {
        return score;
    }

    public int getMoveDelay() {
        return isFast ? fastMoveDelay : moveDelay;
    }

}