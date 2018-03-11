package pauline.mygame;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;

public class TetrisMatrix implements Serializable {

    private int nbCellsX, nbCellsY; // width and height of the game window
    private int[][] array;
    private TetrisPiece currentPiece;
    private TetrisPiece nextPiece;

    public TetrisMatrix(int height, int width) {
        nbCellsY = height;
        nbCellsX = width;
        array = new int[nbCellsY][nbCellsX];
        for (int y = 0; y < nbCellsY; y ++) { // the matrix is initially empty
            Arrays.fill(array[y], 0);
        }

        nextPiece = createNewPiece();
    }

    // create a new piece of a random type
    private TetrisPiece createNewPiece() {
        int type = new Random().nextInt(7) + 1;
        TetrisPiece p = new TetrisPiece(type);
        p.setOriginX(nbCellsX / 2 - 1); // place it on the matrix, on the center horizontally
        p.setOriginY(0); // and on top vertically
        return p;
    }

    // inserts the next piece in the matrix
    public boolean addNewPiece() {
        boolean canAdd = true;

        currentPiece = nextPiece;
        if (!isCollision(currentPiece, array)) { // check if the next piece can be inserted
            placePieceOnMatrix();
            nextPiece = createNewPiece();
        }
        else
            canAdd = false;

        return canAdd;
    }

    // inserts a piece in the matrix
    private void placePieceOnMatrix() {
        for (int y = 0; y < currentPiece.getHeight(); y++) {
            for (int x = 0; x < currentPiece.getWidth(); x++) {
                if (currentPiece.getShape()[y][x] == 1) {
                    // in every tile of the matrix that the piece occupies, its type (integer) of is saved
                    array[currentPiece.getOriginY() + y][currentPiece.getOriginX() + x] = currentPiece.getType();
                }
            }
        }
    }

    // returns that state of the current matrix if a piece was not in it
    private int[][] removePieceFromMatrix(TetrisPiece p) {
        int[][] arrayWithoutPiece = new int[nbCellsY][nbCellsX];
        for (int y = 0; y < nbCellsY; y++) {
            arrayWithoutPiece[y] = array[y].clone();
        }
        for (int y = 0; y < p.getHeight(); y++) {
            for (int x = 0; x < p.getWidth(); x++) {
                if (p.getShape()[y][x] == 1)
                    arrayWithoutPiece[p.getOriginY() + y][p.getOriginX() + x] = 0;
            }
        }
        return arrayWithoutPiece;
    }

    // clears the completed rows and returns the number of rows cleared
    public int clearRows() {
        int nbOfClearedRows = 0;

        // check each row
        int x;
        boolean isCompleteRow;
        for (int y = 0; y < nbCellsY; y++) {

            // check if the row is complete
            isCompleteRow = true;
            for (x = 0; x < nbCellsX; x++) {
                if (array[y][x] == 0) { // if there is a hole, the row is not complete
                    isCompleteRow = false;
                    break;
                }
            }

            // clear a complete row
            if (x == nbCellsX && isCompleteRow) {
                for (int h = y; h > 0; h--)
                    array[h] = array[h - 1].clone(); // the rows that were on top of the cleared row drop down
                Arrays.fill(array[0], 0);
                y--;
                nbOfClearedRows++;
            }
        }

        return nbOfClearedRows;
    }

    // checks if a piece can be inserted without colliding with other pieces or being outside the bounds of the matrix
    private boolean isCollision(TetrisPiece p, int[][] array) {
        boolean collision = false;
        int newOriginX = p.getOriginX();
        int newOriginY = p.getOriginY();
        int newX, newY;

        for (int x = 0; x < p.getWidth(); x++) {
            newX = newOriginX + x;
            for (int y = 0; y < p.getHeight(); y++) {
                newY = newOriginY + y;
                if (p.getShape()[y][x] == 1 && // the piece will be inserted in this tile and
                        (newX < 0 || newX >= nbCellsX || newY >= nbCellsY || // the piece is outside the bounds of the matrix
                                array[newY][newX] != 0)) { // or this tile is already occupied
                    collision = true;
                    break;
                }
            }
        }

        return collision;
    }

    // moves a piece in a specific direction
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

            placePieceOnMatrix(); // insert the piece in the matrix
            moved = true;

        }

        return moved;
    }

    // rotates a piece to the right or to the left
    public void rotatePiece(TetrisPiece.DIRECTION d) {
        TetrisPiece p = currentPiece.clone();
        p.rotate(d);
        int[][] arrayWithoutPiece = removePieceFromMatrix(currentPiece);
            if (!isCollision(p, arrayWithoutPiece)) {
            array = arrayWithoutPiece;
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

    public TetrisPiece getCurrentPiece() {
        return currentPiece;
    }

    public TetrisPiece getNextPiece() {
        return nextPiece;
    }

}