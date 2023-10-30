package com.example.cs449project;

public class GeneralGame extends Game {
    public GeneralGame() {
        super();
    }

    @Override
    public void setCellState(int row, int col) {
        // Call the superclass method first
        super.setCellState(row, col);
        boolean newSOS = checkSOS();
        if (!newSOS && !checkWinner() && !checkDraw()) {
            changeCurrentPlayer();
        }
    }

    @Override
    public boolean checkWinner() {
        if (gameOver() && getPlayers()[0].getNoOfSOS() != getPlayers()[1].getNoOfSOS()) {
            return true;
        }
        return false;
    }

    public boolean checkDraw() {
        if (gameOver() && getPlayers()[0].getNoOfSOS() == getPlayers()[1].getNoOfSOS()) {
            return true;
        }
        return false;
    }

    public boolean gameOver() {
        for (int i = 0; i < getBoardSize(); i++) {
            for (int j = 0; j < getBoardSize(); j++) {
                if (board[i][j] == null) {
                    return false;
                }
            }
        }
        return true;
    }
}
