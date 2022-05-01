package com.example.finalproj;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.util.Arrays;
import java.util.Random;

public class SnakeAPP extends Application {
    //Important variables
    //Area of the canvas
    private static final int WIDTH = 20;
    private static final int HEIGHT = 20;
    //For each grid, their size should be defined
    private static final int GRID_SIZE = 40;
    private static final int CANVAS_WIDTH = WIDTH * GRID_SIZE;
    private static final int CANVAS_HEIGHT = HEIGHT * GRID_SIZE;
    //Define the game
    //Speed of moving
    //speed of frames
    private static int speed;
    //Food targets
    private static Point food = new Point(-1, -1);
    //Maximum of length of snack
    private static Point[] snake = new Point[1000];
    //Current length
    private static int snakeLength = 0;

    //Orientation
    enum Direction {
        UP, LEFT, DOWN, RIGHT
    }

    private static Direction direction;
    //Random generator
    private static Random random = new Random();
    //End sign
    private static boolean gameOver;

    //Initialize the game
    private static void newGame() {
        speed = 3;
        //In the begining, the length of snack is 3
        Arrays.fill(snake, null);
        snakeLength = 0;
        snake[snakeLength++] = new Point(WIDTH / 2, HEIGHT / 2);
        snake[snakeLength++] = new Point(WIDTH / 2, HEIGHT / 2);
        snake[snakeLength++] = new Point(WIDTH / 2, HEIGHT / 2);
        //Generate food target
        newFood();
        direction = Direction.LEFT;
        //If the game continues...
        gameOver = false;
    }

    //Generate the food target randomly
    private static void newFood() {
        //1.The food should be within the canvas
        //2.The food should not within the snack body
        int x, y;
        do {
            x = random.nextInt(WIDTH);
            y = random.nextInt(HEIGHT);
        } while (isCollision(x, y));

        food.x = x;
        food.y = y;
    }

    private static boolean isCollision(int x, int y) {
        //Loop over the snack
        for (int i = 0; i < snakeLength; i++) {
            Point point = snake[i];
            if (point.x == x && point.y == y) {
                return true;
            }
        }
        return false;
    }

    //Movement in every frames
    private static void frame() {
        //Move the snack nody
        for (int i = snakeLength - 1; i >= 1; i--) {
            //Change to previous location
            snake[i].x = snake[i - 1].x;
            snake[i].y = snake[i - 1].y;
        }
        //Move head
        Point head = snake[0];
        switch (direction) {
            case UP:
                head.y--;
                break;
            case DOWN:
                head.y++;
                break;
            case LEFT:
                head.x--;
                break;
            case RIGHT:
                head.x++;
                break;
        }
        //Judge if out of canvas
        if (head.x < 0 || head.x >= WIDTH || head.y < 0 || head.y >= HEIGHT) {
            gameOver = true;
            return;
        }
        //Judge if cross the body
        for (int i = 1; i < snakeLength; i++) {
            Point point = snake[i];
            if (head.x == point.x && head.y == point.y) {
                gameOver = true;
                return;
            }
        }
        //Judge if touch the food target
        // If touchs the snack...
        // 1.increase the length by 1    2.re-generate the food    3. increase the speed
        if (head.x == food.x && head.y == food.y) {
            snake[snakeLength++] = new Point(-1, -1);
            newFood();
            speed++;
        }
    }

    //Render every frame
    private static void render(GraphicsContext gc) {
        //1.Draw the canvas
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        //2.Draw the snack
        for (int i = 0; i < snakeLength; i++) {
            Point point = snake[i];
            gc.setFill(Color.GREEN);
            gc.fillRect(point.x * GRID_SIZE + 1, point.y * GRID_SIZE + 1, GRID_SIZE - 2, GRID_SIZE - 2);
        }
        //3.Draw the food
        gc.setFill(Color.YELLOW);
        gc.fillOval(food.x * GRID_SIZE + 1, food.y * GRID_SIZE + 1, GRID_SIZE, GRID_SIZE);
        //3.End game, end canvas
        if (gameOver) {
            gc.setFill(Color.RED);
            gc.setFont(new Font(40));
            gc.fillText("GAMEOVER. Press R to restart", 200, 200);
        }

        //SCOORE!!!
        gc.setFill(Color.AQUAMARINE);
        gc.setFont(new Font(20));
        gc.fillText("Current score:   "+ (speed-3)+" points", 40, 40);
        //Authors
        gc.setFill(Color.AQUA);
        gc.setFont(new Font(15));
        gc.fillText("The first iteration    Authors：Stevens students ", 20, 740);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Initilize the game
        newGame();
        Pane pane = new Pane();
        Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        pane.getChildren().add(canvas);

        Scene scene = new Scene(pane);

        final GraphicsContext gc = canvas.getGraphicsContext2D();
        AnimationTimer timer = new AnimationTimer() {
            long lastTiick;

            @Override
            public void handle(long now) {
                if (gameOver) {
                    return;
                }
                if (lastTiick == 0 || now - lastTiick > 1e9 / speed) {
                    lastTiick = now;
                    frame();
                    render(gc);
                }
            }
        };
        timer.start();
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case W:
                    case UP:
                        if (direction != Direction.DOWN) {
                            direction = Direction.UP;
                        }
                        break;
                    case A:
                    case LEFT:
                        if (direction != Direction.RIGHT) {
                            direction = Direction.LEFT;
                        }
                        break;
                    case S:
                    case DOWN:
                        if (direction != Direction.UP) {
                            direction = Direction.DOWN;
                        }
                        break;
                    case D:
                    case RIGHT:
                        if (direction != Direction.LEFT) {
                            direction = Direction.RIGHT;
                        }
                        break;
                    case R:
                        if (gameOver) {
                            newGame();
                        }
                        break;
                }
            }
        });
        primaryStage.setScene(scene);
        primaryStage.setTitle("Greedy snack");
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
