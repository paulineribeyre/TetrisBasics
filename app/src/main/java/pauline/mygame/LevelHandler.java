package pauline.mygame;

import android.util.Log;

public class LevelHandler {

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
        moveDelay = 800; //1200;

        isFast = false;
        fastMoveDelay = 100;

        speedIncrease = 50;
        minMoveDelay = 100;

    }

    public void addPoints(int points) {

        float toAdd = points;

        // bonus points for clearing more than one row at a time
        toAdd += points == 1 ? 0 : (points == 2 ? 0.5 : (points == 3 ? 1.5 : 2.5));

        score += 2 * toAdd;

        if (score >= level * 15) {
            Log.d("mydebug", "Level " + level);
            increaseLevel();
        }

        if (score > User.bestScore)
            User.bestScore = score;

    }

    private void increaseLevel() {
        level++;
        if (moveDelay - speedIncrease >= minMoveDelay) {
            moveDelay -= speedIncrease;
            //moveDelayBackUp = moveDelay;
        }
    }

    public void dropFastSpeed() {
        //moveDelay = 100;
        isFast = true;
    }

    public void dropNormalSpeed() {
        //moveDelay = moveDelayBackUp;
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