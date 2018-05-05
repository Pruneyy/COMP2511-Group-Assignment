package sample;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Grid
 * Data structure representing the layout of all vehicles on the playing field
 * 0, 0 at top left, positive x going left and positive y going down
 */
public class Grid {

    static final int GRID_SIZE = 6;
    static final int RED_ID = 1;

    ArrayList<Vehicle> vehicles;


    public Grid() {
        this.vehicles = new ArrayList<>();
    }

    /**
     * Adds a vehicle to the grid, if it does not collide with any other vehicles already in the grid
     * @param vehicle the vehicle to add
     * @return true if the vehicle was added
     */
    public boolean addVehicle(Vehicle vehicle) {

        // Check other vehicles for collisions
        for (Vehicle v : this.vehicles) {
            if (v.collidesWith(vehicle)) return false;
        }
        vehicles.add(vehicle);
        return true;
    }

    /**
     * Attempts to move the vehicle in the positive direction by 1, checking if the move is valid
     * @param vehicle The vehicle to move
     * @return true if move is successful
     */
    public boolean moveVehicleForward(Vehicle vehicle) {
        return moveVehicleBySteps(vehicle.getCarId(), 1);
    }

    /**
     * Attempts to move the vehicle in the negative direction by 1, checking if the move is valid
     * @param vehicle The vehicle to move
     * @return true if move is successful
     */
    public boolean moveVehicleBackward(Vehicle vehicle) {
        return moveVehicleBySteps(vehicle.getCarId(), -1);
    }

    private boolean moveVehicleBySteps(int vehicleId, int steps) {

        Vehicle vehicle = vehicles.get(vehicleId - 1);
        // Create a dummy and perform the move
        Vehicle dummyVehicle = vehicle.clone();
        dummyVehicle.move(steps);
        // Check if vehicle is out of grid
        if (dummyVehicle.getFirstSpaceOccupied() < 0 || dummyVehicle.getLastSpaceOccupied() >= GRID_SIZE) return false;
        // Check collisions with the dummy
        for (Vehicle v : this.vehicles) {
            if (vehicle == v) continue;
            if (dummyVehicle.collidesWith(v)) return false;
        }
        // If no collisions, move actual vehicle forward
        vehicle.move(steps);
        return true;
    }

    /**
     * Computes a 2d array of the layout of the grid
     * 0 is empty, while a positive integer is the id of the car
     * @return 2d array representing the layout of the grid
     */
    public int[][] getGridObject() {
        int[][] grid = new int[GRID_SIZE][GRID_SIZE];
        for (int i = 0; i < GRID_SIZE; i++) {
            Arrays.fill(grid[i], 0);
        }

        for (Vehicle v : this.vehicles) {
            for (Coordinate c : v.getOccupiedSpaces()) {
                grid[c.colIndex][c.rowIndex] = v.getCarId();
            }
        }

        return grid;
    }

    /** Unfortunately this prints the grid as a flipped matrix */
    public String toString() {
        int[][] grid = this.getGridObject();
        return Arrays.deepToString(grid)
              .replace("], ", "]\n")
              .replace("[[", "[")
              .replace("]]", "]");
    }



}

