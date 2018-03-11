package pauline.mygame;

import java.io.Serializable;

public class LevelHandler implements Serializable {

    private int score;
    private int level;
    private int moveDelay;

    public boolean isFast;
    private int fastMoveDelay;

    private final int speedIncrease;
    private final int minMoveDelay;

    public LevelHandler() {

        score = 0;
        level = 1;
        moveDelay = 800; // initial delay between each drop of the current piece

        isFast = false;
        fastMoveDelay = 100; // drop delay when the fast drop is turned on

        speedIncrease = 50; // the drop delay decreases when the player goes on to the next level
        minMoveDelay = 100; // the drop delay cannot go lower than this threshold

    }

    public void addPoints(int points) {

        float toAdd = points;

        // bonus points for clearing more than one row at a time
        toAdd += points == 1 ? 0 : (points == 2 ? 0.5 : (points == 3 ? 1.5 : 2.5));

        score += 2 * toAdd;

        if (score >= level * 15)
            increaseLevel();

        if (score > User.bestScore)
            User.bestScore = score;

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