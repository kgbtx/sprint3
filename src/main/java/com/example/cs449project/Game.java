package com.example.cs449project;

import javafx.scene.paint.Color;
import java.util.HashSet;
import java.util.Set;

public class Game {
    final static int NO_OF_PLAYERS = 2;
    static int INITIAL_BOARD_SIZE = 4;
    final static int MIN_BOARD_SIZE = 3;
    final static int MAX_BOARD_SIZE = 12;

    public enum GameMode {
        Simple,
        General
    }
    private Player[] players;
    private Player currentPlayer;
    private GameMode gameMode;
    private int boardSize;
    protected Cell[][] board;
    private boolean isGameStarted;

    private Set<SOSCombination> sosCombinations;

    public Game() {
        startGame();
    }

    public void startGame() {
        players = new Player[NO_OF_PLAYERS];
        players[0] = new Player(Player.PlayerType.Blue);
        players[1] = new Player(Player.PlayerType.Red);
        currentPlayer = players[0];
        gameMode = GameMode.Simple;
        boardSize = INITIAL_BOARD_SIZE;
        board = new Cell[INITIAL_BOARD_SIZE][INITIAL_BOARD_SIZE];
        isGameStarted = false;
        sosCombinations = new HashSet<>();
    }

    public void setGameMode(GameMode type) {
        gameMode = type;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public Player[] getPlayers() {
        return players;
    }
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public void setBoardSize(int boardSize) {
        if (boardSize >= MIN_BOARD_SIZE && boardSize <= MAX_BOARD_SIZE) {
            this.boardSize = boardSize;
            this.board = new Cell[boardSize][boardSize];
        }
    }

    public boolean makeMove(int row, int col) {
        boolean isValidMove = false;
        if (row >= 0 && row < this.boardSize && col >= 0 && col < this.boardSize) {
            isValidMove = this.board[row][col] == null;
        }
        return isValidMove;
    }

    public void changeCurrentPlayer() {
        this.currentPlayer = this.currentPlayer == players[0] ? players[1] : players[0];
    }

    public boolean isGameStarted() {
        return isGameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        isGameStarted = gameStarted;
    }

    public Cell getCellState(int row, int col) {
        if (this.board[row][col] != null) {
            return this.board[row][col];
        }
        return null;
    }

    public void setCellState(int row, int col) {
        this.board[row][col] = new Cell();
        this.board[row][col].setSymbol(Player.Symbol.valueOf(currentPlayer.getCurrentSymbol()));
    }

    public void resetGame() {
        startGame();
    }

    public boolean checkSOS() {
        Color color = currentPlayer.getPlayerColor();
        boolean newSOS = false;
        if (isGameStarted()) {
            for (int row = 0; row < boardSize; row++) {
                for (int col = 0; col < boardSize; col++) {
                    int x1 = row, y1 = col;
                    Cell x = getCellState(x1, y1);
                    String symbol;
                    if (x != null) {
                        symbol = x.getSymbol().toString();

                        if (symbol != null) {
                            int x2, y2, x3, y3;
                            // Check for SOS horizontally to the right
                            if (y1 + 2 < getBoardSize()) {
                                x2 = x1; y2 = y1 + 1;
                                x3 = x1; y3 = y1 + 2;
                                Cell y = getCellState(x2, y2);
                                Cell z = getCellState(x3, y3);
                                if (y != null & z != null) {
                                    SOSCombination sos = new SOSCombination(x1, y1, x2, y2, x3, y3);
                                    if (sosCombinations.contains(sos)) {
                                        System.out.println("Already taken!");
                                    } else {
                                        String ySymbol = y.getSymbol().toString();
                                        String zSymbol = z.getSymbol().toString();
                                        if (symbol.equals(String.valueOf(Player.Symbol.S)) &&
                                                ySymbol.equals(String.valueOf(Player.Symbol.O)) &&
                                                zSymbol.equals(String.valueOf(Player.Symbol.S))) {
                                            x.addLine(new CellLine(color, CustomTextField.Direction.VERTICAL));
                                            y.addLine(new CellLine(color, CustomTextField.Direction.VERTICAL));
                                            z.addLine(new CellLine(color, CustomTextField.Direction.VERTICAL));
                                            sosCombinations.add(sos);
                                            currentPlayer.incrementNoOfSOS();
                                            newSOS = true;
                                        }
                                    }
                                }
                            }

                            // Check for SOS vertically down
                            if (x1 + 2 < getBoardSize()) {
                                x2 = x1 + 1; y2 = y1;
                                x3 = x1 + 2; y3 = y1;
                                Cell y = getCellState(x2, y2);
                                Cell z = getCellState(x3, y3);
                                if (y != null & z != null) {
                                    SOSCombination sos = new SOSCombination(x1, y1, x2, y2, x3, y3);
                                    if (sosCombinations.contains(sos)) {
                                        System.out.println("Already taken!");
                                    } else {
                                        String ySymbol = y.getSymbol().toString();
                                        String zSymbol = z.getSymbol().toString();
                                        if (symbol.equals(String.valueOf(Player.Symbol.S)) &&
                                                ySymbol.equals(String.valueOf(Player.Symbol.O)) &&
                                                zSymbol.equals(String.valueOf(Player.Symbol.S))) {
                                            x.addLine(new CellLine(color, CustomTextField.Direction.HORIZONTAL));
                                            y.addLine(new CellLine(color, CustomTextField.Direction.HORIZONTAL));
                                            z.addLine(new CellLine(color, CustomTextField.Direction.HORIZONTAL));
                                            sosCombinations.add(sos);
                                            currentPlayer.incrementNoOfSOS();
                                            newSOS = true;
                                        }
                                    }
                                }
                            }

                            // Check for SOS diagonally (top-left to bottom-right)
                            if (x1 + 2 < getBoardSize() && y1 + 2 < getBoardSize()) {
                                x2 = x1 + 1; y2 = y1 + 1;
                                x3 = x1 + 2; y3 = y1 + 2;
                                Cell y = getCellState(x2, y2);
                                Cell z = getCellState(x3, y3);
                                if (y != null & z != null) {
                                    SOSCombination sos = new SOSCombination(x1, y1, x2, y2, x3, y3);
                                    if (sosCombinations.contains(sos)) {
                                        System.out.println("Already taken!");
                                    } else {
                                        String ySymbol = y.getSymbol().toString();
                                        String zSymbol = z.getSymbol().toString();
                                        if (symbol.equals(String.valueOf(Player.Symbol.S)) &&
                                                ySymbol.equals(String.valueOf(Player.Symbol.O)) &&
                                                zSymbol.equals(String.valueOf(Player.Symbol.S))) {
                                            x.addLine(new CellLine(color, CustomTextField.Direction.TOPDOWNDIAGONAL));
                                            y.addLine(new CellLine(color, CustomTextField.Direction.TOPDOWNDIAGONAL));
                                            z.addLine(new CellLine(color, CustomTextField.Direction.TOPDOWNDIAGONAL));
                                            sosCombinations.add(sos);
                                            currentPlayer.incrementNoOfSOS();
                                            newSOS = true;
                                        }
                                    }
                                }
                            }


                            // Check for SOS diagonally (top-right to bottom-left)
                            if (x1 + 2 < getBoardSize() && y1 - 2 >= 0) {
                                x2 = x1 + 1; y2 = y1 - 1;
                                x3 = x1 + 2; y3 = y1 - 2;
                                Cell y = getCellState(x2, y2);
                                Cell z = getCellState(x3, y3);
                                if (y != null & z != null) {
                                    SOSCombination sos = new SOSCombination(x1, y1, x2, y2, x3, y3);
                                    if (sosCombinations.contains(sos)) {
                                        System.out.println("Already taken!");
                                    } else {
                                        String ySymbol = y.getSymbol().toString();
                                        String zSymbol = z.getSymbol().toString();
                                        if (symbol.equals(String.valueOf(Player.Symbol.S)) &&
                                                ySymbol.equals(String.valueOf(Player.Symbol.O)) &&
                                                zSymbol.equals(String.valueOf(Player.Symbol.S))) {
                                            x.addLine(new CellLine(color, CustomTextField.Direction.BOTTOMUPDIAGONAL));
                                            y.addLine(new CellLine(color, CustomTextField.Direction.BOTTOMUPDIAGONAL));
                                            z.addLine(new CellLine(color, CustomTextField.Direction.BOTTOMUPDIAGONAL));
                                            sosCombinations.add(sos);
                                            currentPlayer.incrementNoOfSOS();
                                            newSOS = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return newSOS; // No SOS formation found around the given cell
    }

    public boolean checkWinner() {
        return false;
    }

    public boolean checkDraw() {
        return false;
    }

    public boolean gameOver() {
        return false;
    }
    public Player getWinner() {
        Player winner = new Player();
        if (players[0].getNoOfSOS() > players[1].getNoOfSOS()) {
            winner = players[0];
        } else if (players[0].getNoOfSOS() != players[1].getNoOfSOS()) {
            winner = players[1];
        }
        return winner;
    }
}

