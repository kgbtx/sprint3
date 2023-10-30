package com.example.cs449project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private Player player1;
    private Player player2;

    @BeforeEach
    public void beforeEach() {
         player1 = new Player(Player.PlayerType.Blue);
         player2 = new Player(Player.PlayerType.Red);
    }

    @Test
    void getName() {
        assertEquals(player1.getName(), "Blue player");
        assertEquals(player2.getName(), "Red player");
    }

    @Test
    void getCurrentSymbol() {
        assertEquals(player1.getCurrentSymbol(), Player.Symbol.S.name());
        assertEquals(player2.getCurrentSymbol(), Player.Symbol.S.name());
    }

    @Test
    void setCurrentSymbol() {
        player1.setCurrentSymbol(Player.Symbol.O);
        assertEquals(player1.getCurrentSymbol(), Player.Symbol.O.name());
    }
}