package pauline.mygame;

import java.util.Arrays;

public class TetrisMatrix {

    private static int nbCellsX ;
    private static int nbCellsY;
    private int[][] array;
    private TetrisPiece currentPiece;

    public TetrisMatrix(int height, int width) {
        nbCellsX = width;
        nbCellsY = height;
        array = new int[nbCellsY][nbCellsX];
        for (int i = 0; i < nbCellsY; i ++) {
            Arrays.fill(array[i], 0);
        }

        currentPiece = addNewPiece(1);
    }

    public TetrisMatrix() {
        this(20, 10);
    }

    // TODO
    public void clearCells(int rowNumber, int numberOfRows) {

    }

    public TetrisPiece addNewPiece(int type) {
        TetrisPiece p = new TetrisPiece(type);
        p.setOriginX(0);
        p.setOriginY(nbCellsX / 2 - 1);
        for (int i = 0; i < p.getHeight(); i++) {
            for (int j = 0; j < p.getWidth(); j++) {
                if (p.getShape()[i][j] == 1)
                    array[p.getOriginX()][p.getOriginY() + j] = type;
            }
        }
        return p;
    }

    // TODO change to any horizontal/vertical movement
    public void dropPiece() {
        dropPiece(currentPiece);
    }

    public void dropPiece(TetrisPiece p) {
        for (int i = 0; i < p.getHeight(); i++) {
            for (int j = 0; j < p.getWidth(); j++) {
                if (p.getShape()[i][j] == 1)
                    array[p.getOriginX()][p.getOriginY() + j] = 0;
            }
        }
        p.setOriginY(p.getOriginY() + 1);
        for (int i = 0; i < p.getHeight(); i++) {
            for (int j = 0; j < p.getWidth(); j++) {
                if (p.getShape()[i][j] == 1)
                    array[p.getOriginX()][p.getOriginY() + j] = p.getType();
            }
        }
    }

    public static int getNbCellsX() {
        return nbCellsX;
    }

    public static int getNbCellsY() {
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