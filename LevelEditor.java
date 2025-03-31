import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LevelEditor extends Application {
    private static final int ROWS = 20;
    private static final int COLS = 30;
    private static final int TILE_SIZE = 40;

    enum TileType {
        FLOOR, WALL, PLAYER, CRATE, TARGET, CRATE_ON_TARGET
    }

    private TileType[][] grid = new TileType[ROWS][COLS];

    private Image playerImage = new Image(getClass().getResourceAsStream("/resources/Character4.png"));
    private Image wallImage = new Image(getClass().getResourceAsStream("/resources/Wall_Brown.png"));
    private Image floorImage = new Image(getClass().getResourceAsStream("/resources/Ground_Concrete.png"));
    private Image crateImage = new Image(getClass().getResourceAsStream("/resources/CrateDark_Blue.png"));
    private Image targetImage = new Image(getClass().getResourceAsStream("/resources/EndPoint_Red.png"));
    private Image crateOnTargetImage = new Image(getClass().getResourceAsStream("/resources/Crate_Yellow.png"));

    GridPane board = new GridPane();

    @Override
    public void start(Stage primaryStage) {    

        Scene scene = new Scene(board, COLS * TILE_SIZE, ROWS * TILE_SIZE);
        
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                grid[row][col] = TileType.FLOOR;
            }
        }

        primaryStage.setScene(scene);
        // Обработчик кликов по GridPane
        scene.setOnMouseClicked(this::handleClick);
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
                    case CRATE_ON_TARGET -> tileView.setImage(crateOnTargetImage);
                }
                board.add(tileView, row, col);
            }
        }
    }

    private void handleClick(MouseEvent event) {
        int col = (int) (event.getX() / TILE_SIZE);
        int row = (int) (event.getY() / TILE_SIZE);
        System.out.println("Clicked on cell: (" + row + ", " + col + ")");
        grid[row][col] = TileType.WALL;
        drawGrid();
    }

    public static void main(String[] args) {
        launch(args);
    }
}