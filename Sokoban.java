import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Sokoban extends Application {
    private static final int TILE_SIZE = 40;
    private static final int GRID_WIDTH = 19;
    private static final int GRID_HEIGHT = 11;

    enum TileType {
        FLOOR, WALL, PLAYER, CRATE, TARGET, CRATE_ON_TARGET
    }

    private TileType[][] grid = new TileType[GRID_HEIGHT][GRID_WIDTH];
    private boolean[][] isTarget = new boolean[GRID_HEIGHT][GRID_WIDTH];

    private int playerX, playerY; // Initial player position
    GridPane root = new GridPane();

    private Image playerImage = new Image(getClass().getResourceAsStream("/resources/Character4.png"));
    private Image wallImage = new Image(getClass().getResourceAsStream("/resources/Wall_Brown.png"));
    private Image floorImage = new Image(getClass().getResourceAsStream("/resources/Ground_Concrete.png"));
    private Image crateImage = new Image(getClass().getResourceAsStream("/resources/CrateDark_Blue.png"));
    private Image targetImage = new Image(getClass().getResourceAsStream("/resources/EndPoint_Red.png"));
    private Image crateOnTargetImage = new Image(getClass().getResourceAsStream("/resources/Crate_Yellow.png"));

    @Override
    public void start(Stage primaryStage) {
        
        // initializeGrid(); // Set up initial level

        Scene scene = new Scene(root, GRID_WIDTH * TILE_SIZE, GRID_HEIGHT * TILE_SIZE);
        scene.setOnKeyPressed(event -> handleMovement(event.getCode()));
        primaryStage.setTitle("Sokoban");
        primaryStage.setScene(scene);
        primaryStage.show();

        // drawGrid();
        loadLevel("levels/level2.txt"); // ✅ Load level on start
    }


    // Draw the grid on screen
    private void drawGrid() {
        root.getChildren().clear();
        for (int y = 0; y < GRID_HEIGHT; y++) {
            for (int x = 0; x < GRID_WIDTH; x++) {
                ImageView tileView = new ImageView();
                tileView.setFitWidth(TILE_SIZE);
                tileView.setFitHeight(TILE_SIZE);

                if (grid[y][x] == TileType.CRATE && isTarget[y][x]) {
                    tileView.setImage(crateOnTargetImage); // New image for crates on targets
                    // [TODO] It is still impossible to move crate on the target
                } else {
                    switch (grid[y][x]) {
                        case FLOOR -> tileView.setImage(floorImage);
                        case WALL -> tileView.setImage(wallImage);
                        // case PLAYER -> tileView.setImage(playerImage);
                        case CRATE -> tileView.setImage(crateImage);
                        case TARGET -> tileView.setImage(targetImage);
                        case CRATE_ON_TARGET -> tileView.setImage(crateOnTargetImage);
                    }
                }
                root.add(tileView, x, y);
            }
        }
        ImageView tileView = new ImageView();
        tileView.setFitWidth(TILE_SIZE);
        tileView.setFitHeight(TILE_SIZE);
        // tileView above was inside for loop. It was local variable of loop.
        tileView.setImage(playerImage);
        root.add(tileView, playerX, playerY);
        // We just put player tile above the grid at the end of draw board cycle. 
        // So it does not "shade" tiles below the player.
    }

    // Handle player movement
    private void handleMovement(KeyCode key) {
        int newX = playerX;
        int newY = playerY;

        switch (key) {
            case UP -> newY--;
            case W -> newY--;
            case DOWN -> newY++;
            case X -> newY++;
            case LEFT -> newX--;
            case S -> newX--;
            case RIGHT -> newX++;
            case D -> newX++;
            default -> { return; }
        }

        // Check if the move is valid
        if (grid[newY][newX] == TileType.FLOOR || grid[newY][newX] ==  TileType.TARGET) { //isTarget[newY][newX] 
            // If new position is FLOOR or TARGET
            grid[playerY][playerX] = isTarget[playerY][playerX] ? TileType.TARGET : TileType.FLOOR;
            // If player is ABOVE target we could remove the target after walk above it.
            // [TODO] do not use PLAYER tile. Just draw player tile above tiles at the end of draw
            // Clear old position
            playerX = newX;
            playerY = newY;
            grid[playerY][playerX] = TileType.PLAYER; // Move player
        } 
        else if (grid[newY][newX] == TileType.CRATE) {
            int crateNewX = newX + (newX - playerX);
            int crateNewY = newY + (newY - playerY);

            if (grid[crateNewY][crateNewX] == TileType.FLOOR || isTarget[crateNewY][crateNewX]) {
                // Remove the player from the old position properly
                grid[playerY][playerX] = isTarget[playerY][playerX] ? TileType.TARGET : TileType.FLOOR;
                // Move crate
                grid[newY][newX] = isTarget[newY][newX] ? TileType.TARGET : TileType.FLOOR; // Restore target if needed
                grid[crateNewY][crateNewX] = TileType.CRATE; // Move crate
                playerX = newX;
                playerY = newY;
                grid[playerY][playerX] = TileType.PLAYER;
            }
        }
        checkWinCondition(); // ✅ Check if all crates are on targets
        drawGrid();
    }

    private void checkWinCondition() {
    for (int y = 0; y < GRID_HEIGHT; y++) {
        for (int x = 0; x < GRID_WIDTH; x++) {
            if (isTarget[y][x] && grid[y][x] != TileType.CRATE) {
                return; // ✅ Not all crates are on targets yet
            }
        }
    }
    // While we have at least one crate not on target - game continues

    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("Congratulations!");
    alert.setHeaderText(null);
    alert.setContentText("🎉 You won the game! 🎉");
    alert.showAndWait();

    System.out.println("🎉 YOU WIN! 🎉"); // ✅ Show win message (can be a pop-up later)
    }

    private void loadLevel(String fileName) {
    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
        List<String> lines = new ArrayList<>();
        String line;
        
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        
        int rows = lines.size();
        int cols = lines.get(0).length();
        System.out.println("Cols: " + cols + ". Rows: " + rows);
        
        grid = new TileType[rows][cols]; // Resize grid
        isTarget = new boolean[rows][cols]; // Reset target positions
        
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                char c = lines.get(y).charAt(x);
                switch (c) {
                    case '#' -> grid[y][x] = TileType.WALL;
                        
                    case '.' -> {
                        grid[y][x] = TileType.TARGET;
                        isTarget[y][x] = true;
                    }
                    case '$' -> grid[y][x] = TileType.CRATE;
                    case '@' -> {
                        grid[y][x] = TileType.PLAYER;
                        playerX = x;
                        playerY = y;
                    }
                    default -> grid[y][x] = TileType.FLOOR;
                }
            }
        }
        
        System.out.println("File read is finished");
        drawGrid(); // Refresh screen

        } catch (IOException e) {
           System.err.println("Error loading level: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}


/*
    // Initialize the game grid
    private void initializeGrid() {
        for (int y = 0; y < GRID_HEIGHT; y++) {
            for (int x = 0; x < GRID_WIDTH; x++) {
                grid[y][x] = TileType.FLOOR;
                isTarget[y][x] = false; // No target by default
            }
        }

        // Add walls
        for (int i = 0; i < GRID_WIDTH; i++) {
            grid[0][i] = TileType.WALL;
            grid[GRID_HEIGHT - 1][i] = TileType.WALL;
        }
        for (int i = 0; i < GRID_HEIGHT; i++) {
            grid[i][0] = TileType.WALL;
            grid[i][GRID_WIDTH - 1] = TileType.WALL;
        }

        // Add player
        grid[playerY][playerX] = TileType.PLAYER;

        // Add crates and targets
        grid[4][4] = TileType.CRATE;
        grid[5][5] = TileType.CRATE;

        grid[6][6] = TileType.TARGET;
        grid[7][3] = TileType.TARGET;

        isTarget[6][6] = true;
        isTarget[7][3] = true;

    }
*/