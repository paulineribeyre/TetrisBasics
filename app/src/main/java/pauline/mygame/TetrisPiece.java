package pauline.mygame;

public class TetrisPiece {

    private int type;
    private int width, height;
    private int[][] shape;

    private int originX, originY;

    // type: int between 1 and 7
    public TetrisPiece(int _type) {
        type = (_type < 1 || _type > 7) ? 1 : _type; // type = 0 for empty cells; type = 1 to 7 for tetris shapes
        type = 1; // TODO remove
        switch (type) {
            case 1: // O
                width = 2;
                height = 2;
                shape = new int[height][width];
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        shape[y][x] = 1;
                    }
                }
                break;
        }
    }

    public int getType() {
        return type;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int[][] getShape() {
        return shape;
    }

    public int getOriginX() {
        return originX;
    }

    public void setOriginX(int originX) {
        this.originX = originX;
    }

    public int getOriginY() {
        return originY;
    }

    public void setOriginY(int originY) {
        this.originY = originY;
    }

}