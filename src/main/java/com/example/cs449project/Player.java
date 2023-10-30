package com.example.cs449project;

import javafx.scene.paint.Color;

public class Player {
    public enum PlayerType {
        Blue,
        Red;
    }

    public enum Symbol {
        S,
        O
    }
    private PlayerType type;
    private Symbol currentSymbol;
    private int noOfSOS;

    public Player() {

    }
    public Player(PlayerType type) {
        this.type = type;
        this.currentSymbol = Symbol.S;
        this.noOfSOS = 0;
    }

    public String getName() {
        return this.type.name() + " player";
    }

    public String getCurrentSymbol() {
        return this.currentSymbol.name();
    }

    public void setCurrentSymbol(Symbol symbol) {
        this.currentSymbol = symbol;
    }

    public Color getPlayerColor() {
        if (type == PlayerType.Red) {
            return Color.RED;
        } else if (type == PlayerType.Blue) {
            return Color.BLUE;
        }
        return Color.TRANSPARENT;
    }

    public int getNoOfSOS() {
        return noOfSOS;
    }

    public void setNoOfSOS(int noOfSOS) {
        this.noOfSOS = noOfSOS;
    }

    public void incrementNoOfSOS() {
        this.noOfSOS++;
    }

}
