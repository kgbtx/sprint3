package com.example.cs449project;

public class SimpleGame extends Game {
    public SimpleGame() {
        super();
    }

    @Override
    public void setCellState(int row, int col) {
        // Call the superclass method first
        super.setCellState(row, col);
        checkSOS();
        if (!checkWinner() && !checkDraw()) {
            changeCurrentPlayer();
        }
    }

    @Override
    public boolean checkWinner() {
        for (Player player: getPlayers()) {
            if (player.getNoOfSOS() > 0) {
                return true;
            }
        }
        changeCurrentPlayer();
        return false;
    }
    @Override
    public boolean checkDraw() {
        if (!checkWinner()) {
            for (int i = 0; i < getBoardSize(); i++) {
                for (int j = 0; j < getBoardSize(); j++) {
                    if (board[i][j] == null) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean gameOver() {
        return checkWinner() || checkDraw();
    }
}
