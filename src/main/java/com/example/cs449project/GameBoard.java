package com.example.cs449project;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
public class GameBoard extends Application {
    private Game game;
    private Scene scene;
    private BorderPane rootBorderPane;
    private GridPane gameBoard;
    private HBox headingHBox;
    private HBox footerHBox;
    private VBox[] playerSymbolsBoxes;
    private BorderPane borderPane;

    private boolean modeSelected = false;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        game = new SimpleGame();
        rootBorderPane = createLayout();
        scene = new Scene(rootBorderPane, 720, 560);

        primaryStage.setScene(scene);
        primaryStage.setTitle("SOS Game");
        primaryStage.show();
    }

    private BorderPane createLayout() {
        borderPane = new BorderPane();
        borderPane.setPadding(new Insets(20));

        createHeading();
        borderPane.setCenter(createGameBoard());
        createPlayersVBoxes();
        createFooter();

        return borderPane;
    }

    private void createHeading() {
        headingHBox = new HBox(30);
        headingHBox.getChildren().clear();
        headingHBox.setAlignment(Pos.CENTER);

        Label gameLabel = new Label("SOS");
        ToggleGroup gameModeToggleGroup = new ToggleGroup();
        RadioButton simpleGameModeRadioButton = createRadioButton(Game.GameMode.Simple, false, gameModeToggleGroup);
        RadioButton generalGameModeRadioButton = createRadioButton(Game.GameMode.General, false, gameModeToggleGroup);

        TextField boardSizeTextField = createBoardSizeTextField();

        headingHBox.getChildren().addAll(gameLabel, simpleGameModeRadioButton, generalGameModeRadioButton, boardSizeTextField);

        borderPane.setTop(headingHBox);
    }

    private RadioButton createRadioButton(Game.GameMode mode, boolean selected, ToggleGroup toggleGroup) {
        RadioButton radioButton = new RadioButton(mode.name() + " game");
        radioButton.setUserData(mode);
        radioButton.setToggleGroup(toggleGroup);
        radioButton.setSelected(selected);

        radioButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                Game.GameMode selectedMode = (Game.GameMode) toggleGroup.getSelectedToggle().getUserData();

                game.setGameMode(selectedMode);
                modeSelected = true;
                if (selectedMode == Game.GameMode.Simple) {
                    game = new SimpleGame();
                } else if (selectedMode == Game.GameMode.General) {
                    game = new GeneralGame();
                }

                // Update player symbols when changing game mode
                createPlayersVBoxes();

                // Update the game board and footer
                updateGameBoard();
                createFooter();
            }
        });

        return radioButton;
    }

    private TextField createBoardSizeTextField() {
        TextField boardSizeTextField = new TextField();
        boardSizeTextField.setPrefColumnCount(2);
        boardSizeTextField.setText(String.valueOf(game.getBoardSize()));

        boardSizeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            boardSizeTextField.selectAll();
            try {
                game.setBoardSize(Integer.parseInt(newValue));
                updateGameBoard();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        return boardSizeTextField;
    }

    private GridPane createGameBoard() {
        gameBoard = new GridPane();
        gameBoard.setAlignment(Pos.CENTER);

        updateGameBoard();

        return gameBoard;
    }

    private void updateGameBoard() {
        gameBoard.getChildren().clear();
        int size = game.getBoardSize();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                CustomTextField cell = createCell(i, j);
                gameBoard.add(cell, i, j);
            }
        }
    }

    private CustomTextField createCell(int i, int j) {
        CustomTextField cell = new CustomTextField();
        cell.setEditable(false);
        cell.setPrefColumnCount(1);
        cell.setAlignment(Pos.CENTER);

        cell.setOnMouseClicked(event -> handleCellClick(i, j));

        Cell c = game.getCellState(i, j);
        if (c != null) {
            cell.setText(c.getSymbol().toString());
            for (CellLine line : c.getLines()) {
                cell.activateLine(line.getDirection(), line.getColor());
            }
        }

        return cell;
    }

    private void handleCellClick(int i, int j) {
        if (game.isGameStarted() && game.makeMove(i, j)) {
            game.setCellState(i, j);
            updateGameBoard();
            if (game.checkWinner()) {
                AlertUtil.showAlert(AlertType.INFORMATION, "Game Over", "The Winner is " + game.getWinner().getName());
                restartGame();
            } else if (game.checkDraw()) {
                AlertUtil.showAlert(AlertType.INFORMATION, "Game Over", "The Game is a Draw");
                restartGame();
            }

            createFooter();
        } else if (!game.isGameStarted()) {
            AlertUtil.showAlert(AlertType.WARNING, "Game Not Started", "Select a Game Mode and Press Start Game to Play!");
        }
    }

    private void createPlayersVBoxes() {
        playerSymbolsBoxes = new VBox[game.getPlayers().length];

        for (int i = 0; i < game.getPlayers().length; i++) {
            playerSymbolsBoxes[i] = new VBox(10);
            Player player = game.getPlayers()[i];

            Label playerLabel = new Label(player.getName());

            ToggleGroup playerToggleGroup = new ToggleGroup();
            RadioButton playerSRadioButton = createPlayerRadioButton(player, Player.Symbol.S, playerToggleGroup);
            playerSRadioButton.setSelected(true);
            RadioButton playerORadioButton = createPlayerRadioButton(player, Player.Symbol.O, playerToggleGroup);

            VBox playerBox = new VBox(10, playerLabel, playerSRadioButton, playerORadioButton);
            playerSymbolsBoxes[i].getChildren().add(playerBox);
        }

        borderPane.setLeft(playerSymbolsBoxes[0]);
        borderPane.setRight(playerSymbolsBoxes[1]);
    }

    private RadioButton createPlayerRadioButton(Player player, Player.Symbol symbol, ToggleGroup toggleGroup) {
        RadioButton radioButton = new RadioButton(symbol.name());
        radioButton.setToggleGroup(toggleGroup);
        radioButton.setUserData(symbol);

        radioButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                player.setCurrentSymbol((Player.Symbol) radioButton.getUserData());
            }
        });

        return radioButton;
    }

    private void createFooter() {
        footerHBox = new HBox(50);
        footerHBox.getChildren().clear();
        footerHBox.setAlignment(Pos.CENTER);

        VBox gameControlButtonsVBox = new VBox(10);
        gameControlButtonsVBox.setAlignment(Pos.CENTER);

        Button startGameButton = createGameControlButton("Start Game", event -> {
            if (modeSelected) {
                game.setGameStarted(true);
            } else {
                AlertUtil.showAlert(AlertType.WARNING, "Select A Mode", "Please select a game mode!");
            }
        });
        Button cancelGameButton = createGameControlButton("Cancel Game", event -> {
            restartGame();
        });

        Label currentPlayerLabel = new Label("Current turn: " + game.getCurrentPlayer().getName());
        gameControlButtonsVBox.getChildren().addAll(startGameButton, cancelGameButton);

        footerHBox.getChildren().addAll(currentPlayerLabel, gameControlButtonsVBox);
        borderPane.setBottom(footerHBox);
    }

    private Button createGameControlButton(String text, EventHandler<ActionEvent> handler) {
        Button button = new Button(text);
        button.setOnAction(handler);
        return button;
    }

    private void restartGame() {
        game.setGameStarted(false);
        game.resetGame();
        modeSelected = false;
        createHeading();
        createPlayersVBoxes();
        updateGameBoard();
        createFooter();
    }
    public class AlertUtil {
        public static void showAlert(AlertType alertType, String title, String message) {
            Alert alert = new Alert(alertType);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }
    }

}
