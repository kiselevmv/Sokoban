import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;

public class LevelEditor extends Application {
    private static final int ROWS = 16;
    private static final int COLS = 20;
    private static final int TILE_SIZE = 40;

    enum TileType {
        FLOOR, WALL, PLAYER, CRATE, TARGET, CRATE_ON_TARGET
    }

    private TileType[][] grid = new TileType[ROWS][COLS];

    TileType activeTileType = TileType.WALL;

    // Define image for all TileTypes
    Image playerImage = new Image(getClass().getResourceAsStream("/resources/Character4.png"));
    Image wallImage = new Image(getClass().getResourceAsStream("/resources/Wall_Brown.png"));
    Image floorImage = new Image(getClass().getResourceAsStream("/resources/Ground_Concrete.png"));
    Image crateImage = new Image(getClass().getResourceAsStream("/resources/CrateDark_Blue.png"));
    Image targetImage = new Image(getClass().getResourceAsStream("/resources/EndPoint_Red.png"));
    Image crateOnTargetImage = new Image(getClass().getResourceAsStream("/resources/Crate_Yellow.png"));

    GridPane board = new GridPane();

    // Icon size
    int iconSize = 48;

    // Create buttons with large icons
    Button floorButton = createIconButton(floorImage, iconSize, "FLOOR");
    Button wallButton = createIconButton(wallImage, iconSize, "WALL");
    Button crateButton = createIconButton(crateImage, iconSize, "CRATE");
    Button playerButton = createIconButton(playerImage, iconSize, "PLAYER");
    Button targetButton = createIconButton(targetImage, iconSize, "TARGET");

    @Override
    public void start(Stage primaryStage) {

        // Create initial field, filled with FLOOR tiles
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                grid[row][col] = TileType.FLOOR;
            }
        }

        floorButton.setOnAction(event -> handleFloorTile());
        wallButton.setOnAction(event -> handleWallTile());
        crateButton.setOnAction(event -> handleCrateTile());
        playerButton.setOnAction(event -> handlePlayerTile());
        targetButton.setOnAction(event -> handleTargetTile());

        // Create toolbar and add buttons
        ToolBar toolBar = new ToolBar(floorButton, wallButton, crateButton, playerButton, targetButton);

        // Layout
        BorderPane root = new BorderPane();
        root.setTop(toolBar);   // ToolBar at the top
        root.setCenter(board);  // GridPane below

        Scene scene = new Scene(root, COLS * TILE_SIZE, ROWS * TILE_SIZE);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Level editor");
        primaryStage.show();

        drawGrid();
    }

    private void drawGrid() {
        board.getChildren().clear();
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                ImageView tileView = new ImageView();
                tileView.setFitWidth(TILE_SIZE);
                tileView.setFitHeight(TILE_SIZE);

                switch (grid[row][col]) {
                    case FLOOR -> tileView.setImage(floorImage);
                    case WALL -> tileView.setImage(wallImage);
                    case CRATE -> tileView.setImage(crateImage);
                    case TARGET -> tileView.setImage(targetImage);
                    case PLAYER -> tileView.setImage(playerImage);
                    case CRATE_ON_TARGET -> tileView.setImage(crateOnTargetImage);
                }
                
                final int clickedRow = row;
                final int clickedCol = col;

                // Add an handler for each ImageView
                // We have to use "final" variables for the reason I do not know
                tileView.setOnMouseClicked(event -> handleImageClick(event, clickedRow, clickedCol));

                board.add(tileView, col, row);
            }
        }
    }

    private void handleImageClick(MouseEvent event, int row, int col) {
        System.out.println("Clicked on ImageView at cell: (" + row + ", " + col + ")");
        grid[row][col] = activeTileType;
        drawGrid();
    }

    // Helper method to create a button with an icon
    private Button createIconButton(Image image, int size, String actionName) {
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(size);
        imageView.setFitHeight(size);

        Button button = new Button("", imageView); // Empty text, only icon
        button.setStyle("-fx-background-color: transparent;"); // Optional: removes button background
        button.setOnAction(event -> System.out.println(actionName + " button clicked!"));

        return button;
    }
    
    private void handleFloorTile() {
        activeTileType = TileType.FLOOR;
    }

    private void handleWallTile() {
        activeTileType = TileType.WALL;
    }

    private void handlePlayerTile() {
        activeTileType = TileType.PLAYER;
    }

    private void handleCrateTile () {
        activeTileType = TileType.CRATE;
    }

    private void handleTargetTile() {
        activeTileType = TileType.TARGET;
    }

    public static void main(String[] args) {
        launch(args);
    }
}