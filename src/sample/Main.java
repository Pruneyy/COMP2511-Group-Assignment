/*
 * This is a test by P-Swizzle
 * This is test 2 by P-Swizzle
 * This is test 3 by P-Swizzle
 */
package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main class extends Application which brings in the functionality of JavaFX.
 * The primary objective of the main class is to launch the game.
 * @author Group 5 - Chris Armstrong, Edbert Chung, Huai Dong Loo, Pranav Singh, Utkarsh Sood.
 */
public class Main extends Application {
    /**
     * Used to set the different kinds of themes that are available.
     * enum allows for more flexibility in managing themes.
     */
    public enum Theme {
        CARS, PLANE, ANIMALS
    }

    /**
     * Representation of a grid is done through the Grid object.
     */
    public static Grid grid;
    public static Boolean isSplashLoaded = false;
    /**
     * Allows the theme to be set by the user based on their selection.
     */
    public static Theme currentTheme;

    /**
     * Construction of the user window that will be interacted with by the user.
     * @param primaryStage primary container for all stage (extends Window) styling.
     * @throws Exception exception may be thrown and is handled by the caller.
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Game.fxml"));
        Parent root = loader.load();
        currentTheme = Theme.CARS;
        primaryStage.setTitle("Grid Lock!");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.show();
    }

    /**
     * The main program that initiates this project through the launch method.
     * @param args arguments given from the command line
     */
    public static void main(String[] args) {
        launch(args);
    }
}
