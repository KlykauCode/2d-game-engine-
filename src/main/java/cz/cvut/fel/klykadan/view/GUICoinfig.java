package cz.cvut.fel.klykadan.view;

public class GUICoinfig {
    private int tileSize = 48;
    private int maxScreenCol = 20;
    private int maxScreenRow = 12;
    private int screenWidth = tileSize * maxScreenCol;
    private int screenHeight = tileSize * maxScreenRow;
    private int FPS = 60;
    private int LevelSizeX = 60;
    private int LevelSizeY = 40;
    private int LevelWidth = LevelSizeX * tileSize;
    private int LevelHeight = LevelSizeY * tileSize;


    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public int getFPS(){
        return FPS;
    }

    public int getTileSize() {
        return tileSize;
    }

    public int getMaxScreenCol() {
        return maxScreenCol;
    }

    public int getMaxScreenRow() {
        return maxScreenRow;
    }

    public int getLevelSizeX() {
        return LevelSizeX;
    }

    public int getLevelSizeY() {
        return LevelSizeY;
    }

    public int getLevelWidth() {
        return LevelWidth;
    }

    public int getLevelHeight() {
        return LevelHeight;
    }
}
