package project.tetris.model.board;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import project.tetris.model.helper.Position;
import project.tetris.model.tetromino.TetrominoInformation;
import project.tetris.model.tetromino.*;

import java.util.*;

public class Board {
    // constants
    public static final int WIDTH = 15;
    public static final int HEIGHT = 22;
    public static final int BRICK_SIZE = 29;

    // board
    private int[][] tetrisBoard;
    // current tetromino on the board
    private TetrominoInformation currentTetromino;
    // current score
    private IntegerProperty score;

    public Board() {
        tetrisBoard = new int[HEIGHT][WIDTH];
        score = new SimpleIntegerProperty(0);
    }

    public void setCurrentTetromino(TetrominoInformation tetromino) {
        this.currentTetromino = tetromino;
    }

    // function that merges current tetromino to a copy of the current board
    // and returns the updated board
    public void mergeBrickToBackground() {
        int[][] tetromino = currentTetromino.getTetromino();
        Position tetrominoPos = currentTetromino.getPosition();

        int[][] copy = copy(tetrisBoard);
        for (int i = 0; i < tetromino.length; i++) {
            for (int j = 0; j < tetromino[i].length; j++) {
                int targetX = tetrominoPos.getXPos() + j;
                int targetY = tetrominoPos.getYPos() + i;
                if (tetromino[i][j] != 0) {
                    copy[targetY - 1][targetX] = tetromino[i][j];
                }
            }
        }
        tetrisBoard = copy;
    }

    // neat copy 2D: https://stackoverflow.com/questions/5617016/how-do-i-copy-a-2-dimensional-array-in-java
    public int[][] copy(int[][] original) {
        return Arrays.stream(original).map(int[]::clone).toArray(int[][]::new);
    }


    // checks if the position is within the board
    private boolean outOfBounds(int[][] tetrisBoard, int targetX, int targetY) {
        return targetX < 0 || targetY >= tetrisBoard.length || targetX >= tetrisBoard[targetY].length;
    }

    private boolean outOfBoardBorder(int[][] tetromino, Position currentPos) {
        for (int i = 0; i < tetromino.length; i++) {
            for (int j = 0; j < tetromino[i].length; j++) {
                int targetX = currentPos.getXPos() + j;
                int targetY = currentPos.getYPos() + i;

                if (tetromino[i][j] != 0 &&
                        (outOfBounds(tetrisBoard, targetX, targetY) ||
                                tetrisBoard[targetY][targetX] != 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    // move tetromino down the board
    public boolean moveTetrominoDown() {
        Position currentPos = currentTetromino.getPosition();

        Position newPos = new Position(currentPos.getXPos(), currentPos.getYPos() + 1);
        currentTetromino.setTetrominoPosition(newPos);

        boolean anyConflict = outOfBoardBorder(currentTetromino.getTetromino(), newPos);

        if (anyConflict) {
            return false;
        } else {
            currentTetromino.setTetrominoPosition(newPos);
            return true;
        }
    }

    public IntegerProperty getScore() {
        return score;
    }

    public int[][] getTetrisBoard() {
        return tetrisBoard;
    }

}
