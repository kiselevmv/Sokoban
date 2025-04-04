import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public class StartMenu extends Application {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private double gradientOffset = 0; // Animation variable
    private Stage mainStage; // Store main stage reference

    @Override
    public void start(Stage primaryStage) {
        mainStage = primaryStage; // Store reference

        // 🌟 Animated Background
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        
        // Start background animation
        Timeline backgroundAnimation = new Timeline(new KeyFrame(Duration.millis(50), e -> drawAnimatedBackground(gc)));
        backgroundAnimation.setCycleCount(Animation.INDEFINITE);
        backgroundAnimation.play();

        // 🎮 Menu Buttons
        Button startButton = createStyledButton("Start Game");
        Button settingsButton = createStyledButton("Settings");
        Button exitButton = createStyledButton("Exit");

        // 📌 Button Actions
        startButton.setOnAction(e -> transitionToGameScene());
        settingsButton.setOnAction(e -> System.out.println("Open Settings"));
        exitButton.setOnAction(e -> primaryStage.close());

        // 📌 VBox for menu layout
        VBox menuBox = new VBox(20, startButton, settingsButton, exitButton);
        menuBox.setAlignment(Pos.CENTER);

        // 🎬 Fade-in Animation
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), menuBox);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        // 📌 StackPane to overlay buttons on animated background
        StackPane root = new StackPane(canvas, menuBox);
        Scene menuScene = new Scene(root, WIDTH, HEIGHT);

        // 🎮 Show Stage
        primaryStage.setScene(menuScene);
        primaryStage.setTitle("Animated Game Menu");
        primaryStage.show();
    }

    // 🎨 Draw Animated Background
    private void drawAnimatedBackground(GraphicsContext gc) {
        gradientOffset += 0.01;
        if (gradientOffset > 1) gradientOffset = 0;

        LinearGradient gradient = new LinearGradient(0, gradientOffset, 1, 1 - gradientOffset, true, CycleMethod.REFLECT,
                new Stop(0, Color.DARKBLUE), 
                new Stop(0.5, Color.PURPLE), 
                new Stop(1, Color.BLACK));

        gc.setFill(gradient);
        gc.fillRect(0, 0, WIDTH, HEIGHT);
    }

    // 🎮 Smooth Scene Transition to Game Scene
    private void transitionToGameScene() {
        StackPane gameRoot = new StackPane();
        Scene gameScene = new Scene(gameRoot, WIDTH, HEIGHT);
        gameRoot.setStyle("-fx-background-color: black;"); // Placeholder game scene

        // 🎬 Fade-out effect before switching
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), mainStage.getScene().getRoot());
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(e -> mainStage.setScene(gameScene)); // Switch scene after fade
        fadeOut.play();
    }

    // 🎨 Create a Styled Button
    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        button.setTextFill(Color.WHITE);
        button.setStyle("-fx-background-color: transparent; -fx-border-color: white; -fx-border-width: 2px;");
        button.setMinWidth(200);
        button.setMinHeight(50);

        // 🌟 Hover Effect
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: white; -fx-text-fill: black;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: transparent; -fx-border-color: white; -fx-text-fill: white;"));

        return button;
    }

    public static void main(String[] args) {
        launch(args);
    }
}