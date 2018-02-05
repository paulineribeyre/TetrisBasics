package pauline.mygame;

import android.util.Log;

import java.util.Arrays;

public class TetrisMatrix {

    private int nbCellsX; // width of the game window
    private int nbCellsY; // height of the game window
    private int[][] array;
    private TetrisPiece currentPiece;

    public TetrisMatrix(int height, int width) {
        nbCellsX = width;
        nbCellsY = height;
        array = new int[nbCellsY][nbCellsX];
        for (int y = 0; y < nbCellsY; y ++) {
            Arrays.fill(array[y], 0);
        }

        addNewPiece(1);
    }

    public TetrisMatrix() {
        this(20, 10);
    }

    private void placePiece(TetrisPiece p) {
        for (int y = 0; y < p.getHeight(); y++) {
            for (int x = 0; x < p.getWidth(); x++) {
                if (p.getShape()[y][x] == 1)
                    array[p.getOriginY() + y][p.getOriginX() + x] = p.getType();
            }
        }
    }

    // TODO
    public void clearCells(int rowNumber, int numberOfRows) {

    }

    public void addNewPiece(int type) {
        TetrisPiece p = new TetrisPiece(type);
        p.setOriginX(nbCellsX / 2 - 1);
        p.setOriginY(0);
        placePiece(p);
        currentPiece = p;
    }

    // TODO change to any horizontal/vertical collision
    private boolean isCollision() {
        boolean collision = false;
        int posY = currentPiece.getOriginY() + currentPiece.getHeight();
        if (posY >= nbCellsY || array[posY][currentPiece.getOriginX()] != 0)
            collision = true;
        if (collision) Log.d("mydebug", "TetrisMatrix.isCollision collision");
        return collision;
    }

    // TODO change to any horizontal/vertical movement
    public boolean dropPiece() {

        boolean dropped = false;

        // do not move the piece outside of the game window
        if (!isCollision()) {

            // remove the piece from its old position
            // TODO function
            for (int y = 0; y < currentPiece.getHeight(); y++) {
                for (int x = 0; x < currentPiece.getWidth(); x++) {
                    if (currentPiece.getShape()[y][x] == 1)
                        array[currentPiece.getOriginY() + y][currentPiece.getOriginX() + x] = 0;
                }
            }

            // move the piece to its new position
            currentPiece.setOriginY(currentPiece.getOriginY() + 1);
            placePiece(currentPiece);

            dropped = true;

        }

        return dropped;

    }

    public int getNbCellsX() {
        return nbCellsX;
    }

    public int getNbCellsY() {
        return nbCellsY;
    }

    public int[][] getArray() {
        return array;
    }

    public void setArray(int[][] array) {
        this.array = array;
    }

    public TetrisPiece getCurrentPiece() {
        return currentPiece;
    }
}