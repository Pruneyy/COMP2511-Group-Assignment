package sample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Grid {

    static final int GRID_SIZE = 6;
    static final int RED_ID = 1;

    ArrayList<Vehicle> vehicles;


    public Grid() {
        this.vehicles = new ArrayList<>();
    }

    public boolean addVehicle(Vehicle vehicle) {

        // Check other vehicles for collisions
        for (Vehicle v : this.vehicles) {
            if (v.collidesWith(vehicle)) return false;
        }
        vehicles.add(vehicle);
        return true;
    }
    /** 0, 0 at top left, positive x going left and positive y going down */
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

