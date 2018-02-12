package pauline.mygame;

import android.util.Log;

public class LevelHandler {

    private int points = 0;
    private int level = 1;
    private int moveDelay = 1200;

    private final int speedIncrease = 50;
    private final int minMoveDelay = 100;

    public void addPoints(int points) {
        this.points += points;
        if (this.points % 5 * level == 0) {
            Log.d("mydebug", "Level " + level);
            increaseLevel();
        }
    }

    private void increaseLevel() {
        level++;
        if (moveDelay - speedIncrease >= minMoveDelay)
            moveDelay -= speedIncrease;
    }

    public int getLevel() {
        return level;
    }

    public int getMoveDelay() {
        return moveDelay;
    }

}