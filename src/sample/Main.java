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

import java.util.Arrays;
import java.util.List;

public class Main extends Application {

    public static Grid grid;
    public static Boolean isSplashLoaded = false;
    public static Boolean isVictoryLoaded = false;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Grid Lock!");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.show();
    }


    public static void main(String[] args) {
        //testVehicleCollisions();
        //testPuzzleGenerator();
        launch(args);
		//FilePuzzleService test = new FilePuzzleService();
		//test.getNewPuzzle(PuzzleService.Difficulty.EASY);
		
    }

    /**
     * Informal testing on grid/vehicle methods
     * Assumes asserts are on and working
     * You might need to ensure that the VM argument -ea is used when running the program for these to report errors
     */
    public static void testVehicleCollisions() {
        System.out.println("testing Grid.addVehicle");
        Main.grid = new Grid();
        List<Vehicle> testVehicles = Arrays.asList(
              new Vehicle(1, Vehicle.Orientation.HORIZONTAL, 2, 0, 2),
              new Vehicle(2, Vehicle.Orientation.HORIZONTAL, 2, 3, 4),
              new Vehicle(3, Vehicle.Orientation.HORIZONTAL, 2, 2, 3)
        );

        assert(Main.grid.addVehicle(testVehicles.get(0)) == true);      // Vehicle 1 is free to be added
        assert(Main.grid.addVehicle(testVehicles.get(1)) == true);      // Vehicle 2 is free to be added
        assert(Main.grid.addVehicle(testVehicles.get(2)) == false);     // Vehicle 3 would be blocked by 1 and 2

        System.out.println("testing Grid.moveVehicleForward");
        assert(Main.grid.moveVehicleForward(testVehicles.get(0)) == false);     // Vehicle 1 is blocked by vehicle 2
        assert(Main.grid.moveVehicleForward(testVehicles.get(1)) == true);      // Vehicle 2 is free to move away from vehicle 1
        assert(Main.grid.moveVehicleForward(testVehicles.get(1)) == false);     // Vehicle 2 cannot move past the edge of the grid

        System.out.println("All tests pass!");
    }

    public static void testPuzzleGenerator() {
        System.out.println("testing Grid.addVehicle");
        GeneratorPuzzleService puzzleGenerator = new GeneratorPuzzleService();
        Main.grid = puzzleGenerator.getNewPuzzle(PuzzleService.Difficulty.EASY);
        if (Main.grid == null) {
            System.err.println("The generator failed to generate a puzzle, try using less vehicles or something");
            return;
        }
        System.out.println("Puzzle generated! (Keep in mind the output is flipped on stdout because of the print function");
        System.out.println(Main.grid);
        System.out.println("Undoing moves...");
        puzzleGenerator.solve();
        System.out.println("Puzzle solved!");
        System.out.println(Main.grid);
    }


}
