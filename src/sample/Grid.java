package sample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

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



}

