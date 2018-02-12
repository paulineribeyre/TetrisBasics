package pauline.mygame;

import android.util.Log;

import java.util.Arrays;
import java.util.Random;

public class TetrisMatrix {

    private int nbCellsX, nbCellsY; // width and height of the game window
    private int[][] array;
    private TetrisPiece currentPiece;

    public TetrisMatrix(int height, int width) {
        nbCellsX = width;
        nbCellsY = height;
        array = new int[nbCellsY][nbCellsX];
        for (int y = 0; y < nbCellsY; y ++) {
            Arrays.fill(array[y], 0);
        }

        //addNewPiece();
    }

    public TetrisMatrix() {
        this(20, 10);
    }

    public void printMatrix(int[][] array) { // TODO remove
        String str;
        for (int y = 0; y < nbCellsY; y ++) {
            str = "";
            for (int x = 0; x < nbCellsX; x++) {
                str += array[y][x] + " ";
            }
            Log.d("mydebug", "TetrisMatrix.printMatrix " + str);
        }
    }

    private void placePieceOnMatrix() {
        for (int y = 0; y < currentPiece.getHeight(); y++) {
            for (int x = 0; x < currentPiece.getWidth(); x++) {
                if (currentPiece.getShape()[y][x] == 1) {
                    //Log.d("mydebug", "TetrisMatrix.placePieceOnMatrix " + (currentPiece.getOriginY()) + " + " + y);
                    array[currentPiece.getOriginY() + y][currentPiece.getOriginX() + x] = currentPiece.getType();
                }
            }
        }
    }

    private int[][] removePieceFromMatrix(TetrisPiece p) {
        int[][] arrayWithoutPiece = new int[nbCellsY][nbCellsX];
        for (int y = 0; y < nbCellsY; y++) {
            arrayWithoutPiece[y] = array[y].clone();
            //for (int x = 0; x < nbCellsX; x++)
            //    arrayWithoutPiece[y][x] = array[y][x];
        }
        for (int y = 0; y < p.getHeight(); y++) {
            for (int x = 0; x < p.getWidth(); x++) {
                if (p.getShape()[y][x] == 1)
                    arrayWithoutPiece[p.getOriginY() + y][p.getOriginX() + x] = 0;
            }
        }
        return arrayWithoutPiece;
    }

    // TODO
    public void clearCells(int rowNumber, int numberOfRows) {

    }

    public boolean addNewPiece() {
        boolean canAdd = true;

        int[] arr = {1, 5};
        int type = arr[new Random().nextInt(arr.length)]; // TODO remove
        //type = 5;

        //int type = new Random().nextInt(8) + 1;
        TetrisPiece p = new TetrisPiece(type);
        p.setOriginX(nbCellsX / 2 - 1);
        p.setOriginY(0);
        currentPiece = p;

        if (!isCollision(currentPiece, array))
            placePieceOnMatrix();
        else
            canAdd = false;

        return canAdd;
    }

    private boolean isCollision(TetrisPiece p, int[][] array) {
        boolean collision = false;
        int newOriginX = p.getOriginX();
        int newOriginY = p.getOriginY();
        int newX, newY;
        //Log.d("mydebug", "TetrisMatrix.isCollision collision "+newOriginY);

        for (int x = 0; x < p.getWidth(); x++) {
            newX = newOriginX + x;
            for (int y = 0; y < p.getHeight(); y++) {
                newY = newOriginY + y;
                //Log.d("mydebug", "TetrisMatrix.isCollision collision "+newOriginY + " + "+ y);
                if (p.getShape()[y][x] == 1 &&
                        (newX < 0 || newX >= nbCellsX || newY >= nbCellsY || array[newY][newX] != 0)) {
                    collision = true;
                    break;
                }
            }
        }

        if (collision) Log.d("mydebug", "TetrisMatrix.isCollision collision");

        return collision;
    }

    public boolean movePiece(TetrisPiece.DIRECTION d) {
        boolean moved = false;

        TetrisPiece p = currentPiece.clone();

        switch(d){
            case DOWN:
                p.setOriginY(p.getOriginY() + 1);
                break;
            case LEFT:
                p.setOriginX(p.getOriginX() - 1);
                break;
            case RIGHT:
                p.setOriginX(p.getOriginX() + 1);
                break;
        }

        // remove the piece from its old position
        int[][] arrayWithoutPiece = removePieceFromMatrix(currentPiece);

        // do not move the piece if it collides with the game window or another piece
        if (!isCollision(p, arrayWithoutPiece)) {

            array = arrayWithoutPiece.clone();

            // move the piece to its new position
            switch(d){
                case DOWN:
                    currentPiece.setOriginY(currentPiece.getOriginY() + 1);
                    break;
                case LEFT:
                    currentPiece.setOriginX(currentPiece.getOriginX() - 1);
                    break;
                case RIGHT:
                    currentPiece.setOriginX(currentPiece.getOriginX() + 1);
                    break;
            }

            placePieceOnMatrix();
            moved = true;

        }

        return moved;
    }

    public void rotatePiece(TetrisPiece.DIRECTION d) {
        TetrisPiece p = currentPiece.clone();
        p.rotate(d);
        array = removePieceFromMatrix(currentPiece);
        if (!isCollision(p, array)) {
            //printMatrix(array);
            //printMatrix(array);
            currentPiece = p;
            placePieceOnMatrix();
        }
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