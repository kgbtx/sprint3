package com.example.cs449project;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class GeneralGameTest {
    private Game game;

    @Before
    public void setUp() {
        game = new GeneralGame();
    }

    @Test
    public void testSetCellState() {
        game.setGameMode(Game.GameMode.General);
        Player player1 = game.getPlayers()[0];
        Player player2 = game.getPlayers()[1];
        game.setGameStarted(true);
        game.setCellState(0, 0);

        // After setting a cell, the current player should change
        assertEquals(player2, game.getCurrentPlayer());
    }

    @Test
    public void testCheckWinner() {
        game.setGameMode(Game.GameMode.General);
        Player player1 = game.getPlayers()[0];
        Player player2 = game.getPlayers()[1];

        // Test when no player has won
        assertFalse(game.checkWinner());

        // Test when a player wins
        player1.incrementNoOfSOS();
        assertFalse(game.checkWinner());
        assertEquals(player1, game.getCurrentPlayer());
    }

    @Test
    public void testCheckDraw() {
        game.setGameMode(Game.GameMode.General);

        // Test when the game is not a draw
        assertFalse(game.checkDraw());

        // Test when the game is a draw; no winner
        for (int i = 0; i < game.getBoardSize(); i++) {
            for (int j = 0; j < game.getBoardSize(); j++) {
                game.setCellState(i, j);
            }
        }
        assertTrue(game.checkDraw());
    }

    @Test
    public void testGameOver() {
        game.setGameMode(Game.GameMode.General);

        // Test when the game is not over
        assertFalse(game.gameOver());

        // Test when the game is over
        for (int i = 0; i < game.getBoardSize(); i++) {
            for (int j = 0; j < game.getBoardSize(); j++) {
                game.setCellState(i, j);
            }
        }
        assertTrue(game.gameOver());
    }
}
