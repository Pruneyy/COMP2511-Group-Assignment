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

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        testVehicleCollisions();
//        launch(args);
    }

    public static void testVehicleCollisions() {
        System.out.println("Testing...");
        Main.grid = new Grid();
        List<Vehicle> testVehicles = Arrays.asList(
              new Vehicle(1, Vehicle.Orientation.HORIZONTAL, 2, 0, 2),
              new Vehicle(2, Vehicle.Orientation.HORIZONTAL, 2, 3, 4),
              new Vehicle(3, Vehicle.Orientation.HORIZONTAL, 2, 2, 3)
        );

        for (Vehicle v : testVehicles) {
            if (!Main.grid.addVehicle(v)) {
                System.err.println("poop");
                return;
            }
            System.out.println("Vehicle added successfully");
        }

    }

}
