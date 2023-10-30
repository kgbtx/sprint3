package com.example.cs449project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

public class SimpleGameTest {
    private Game game;

    @BeforeEach
    public void setUp() {
        game = new SimpleGame();
    }

    @Test
    public void testSetCellState() {
        game.setGameMode(Game.GameMode.Simple);
        Player player1 = game.getPlayers()[0];
        Player player2 = game.getPlayers()[1];
        game.setGameStarted(true);
        game.setCellState(0, 0);

        // After setting a cell, the current player should change
        assertEquals(player2, game.getCurrentPlayer());
    }

    @Test
    public void testCheckWinner() {
        game.setGameMode(Game.GameMode.Simple);
        Player player1 = game.getPlayers()[0];
        Player player2 = game.getPlayers()[1];

        // Test when no player has won; a draw
        assertFalse(game.checkWinner());

        // Test when a player wins
        player1.incrementNoOfSOS();
        assertTrue(game.checkWinner());
        assertEquals(player1.getName(), game.getWinner().getName());
    }

    @Test
    public void testCheckDraw() {
        game.setGameMode(Game.GameMode.Simple);

        // Test when the game is not a draw
        assertFalse(game.checkDraw());

        // Test when the game is a draw
        for (int i = 0; i < game.getBoardSize(); i++) {
            for (int j = 0; j < game.getBoardSize(); j++) {
                game.setCellState(i, j);
            }
        }
        assertTrue(game.checkDraw());
    }

    @Test
    public void testGameOver() {
        game.setGameMode(Game.GameMode.Simple);

        // Test when the game is not over
        assertFalse(game.gameOver());

        // Test when the game is over as a draw
        for (int i = 0; i < game.getBoardSize(); i++) {
            for (int j = 0; j < game.getBoardSize(); j++) {
                game.setCellState(i, j);
            }
        }
        assertTrue(game.gameOver());

        // Test when the game is over as a win
        Player player1 = game.getPlayers()[0];
        player1.incrementNoOfSOS();
        assertTrue(game.gameOver());
    }
}
