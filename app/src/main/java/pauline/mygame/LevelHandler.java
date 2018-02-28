package pauline.mygame;

import android.util.Log;

public class LevelHandler {

    public float points = 0;
    private int level = 1;
    private int moveDelay = 800; //1200;

    public boolean isFast = false;
    private int fastMoveDelay = 100; //1200;
    //private int moveDelayBackUp = moveDelay;

    private final int speedIncrease = 50;
    private final int minMoveDelay = 100;

    public void addPoints(int points) {
        this.points += points;

        // bonus points for clearing more than one row at a time
        this.points += points == 1 ? 0 : (points == 2 ? 0.5 : (points == 3 ? 1.5 : 2.5));

        if (this.points % (5 * level) == 0) {
            Log.d("mydebug", "Level " + level);
            increaseLevel();
        }
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

    public int getMoveDelay() {
        return isFast ? fastMoveDelay : moveDelay;
    }

}