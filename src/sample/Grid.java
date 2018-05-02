package sample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Grid {

    static final int GRID_SIZE = 6;
    static final int RED_ID = 1;

    ArrayList<Vehicle> vehicles;

    /** 0, 0 at top left, positive x going left and positive y going down */
    int grid[][] = new int[GRID_SIZE][GRID_SIZE];

    public Grid() {
        for (int i = 0; i < GRID_SIZE; i++) {
            Arrays.fill(grid[i], 0);
        }
        this.vehicles = new ArrayList<>();
    }

    public boolean addVehicle(Vehicle vehicle) {
        if (this.vehicles.stream()
              .filter(v -> vehicle.collidesWith(v))
              .collect(Collectors.toList())
              .size() > 0) return false;
        vehicles.add(vehicle);
        return true;
    }



}
