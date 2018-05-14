package sample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Grid
 * Data structure representing the layout of all vehicles on the playing field
 * 0, 0 at top left, positive x going left and positive y going down
 */
public class Grid {

    static final int GRID_SIZE = 6;
    static final int RED_ID = 1;

    private ArrayList<Vehicle> vehicles;
    private ArrayList<Move> moves;

    public Grid() {
        this.vehicles = new ArrayList<>();
        this.moves = new ArrayList<>();
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

    public boolean moveVehicleBySteps(int vehicleId, int steps) {

        Vehicle vehicle = vehicles.get(vehicleId - 1);

        // Create a dummy and perform the move
        Vehicle dummyVehicle = vehicle.clone();
        dummyVehicle.move(steps);

        // Check if vehicle is valid
        if (!vehicleIsValid(dummyVehicle)) return false;

        // If no collisions, move actual vehicle forward
        this.moves.add(vehicle.move(steps));
        return true;
    }

    /** index is the first space occupied for which the vehicle is to move to */
    public boolean moveVehicleToIndex(int vehicleId, int index) {
        Vehicle v = vehicles.get(vehicleId - 1);
        int diff = index - v.getFirstSpaceOccupied();
        while (diff != 0) {
            if (diff < 0) {
                if (!moveVehicleBySteps(vehicleId, -1)) return false;
                diff += 1;
            } else {
                if (!moveVehicleBySteps(vehicleId, 1)) return false;
                diff -=1;
            }
        }
        return true;
    }

    private boolean vehicleIsValid(Vehicle vehicle) {
        if (vehicle.getFirstSpaceOccupied() < 0 || vehicle.getLastSpaceOccupied() >= GRID_SIZE) return false;
        for (Vehicle v : this.vehicles) {
            if (vehicle.getCarId() == v.getCarId()) continue;
            if (vehicle.collidesWith(v)) return false;
        }
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

    public Vehicle getVehicleById(int id) {
        for (Vehicle v : this.vehicles) {
            if (v.getCarId() == id) return v;
        }
        return null;
    }

    /** Unfortunately this prints the grid as a flipped matrix */
    public String toString() {
        int[][] grid = this.getGridObject();
        return Arrays.deepToString(grid)
              .replace("], ", "]\n")
              .replace("[[", "[")
              .replace("]]", "]");
    }


    public ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }

    public ArrayList<Move> getMoves() {
        return moves;
    }

    public Vehicle undoLastVehicleMoves() {
        if (this.moves.size() == 0) return null;
        Vehicle vehicle = this.moves.get(this.moves.size() - 1).getVehicle();
        int i;
        for (i = this.moves.size() - 1; i >= 0 && this.moves.get(i).getVehicle() == vehicle; i--) {
            this.moves.get(i).revert();
        }
        this.moves = new ArrayList<>(this.moves.subList(0, i + 1));
        return vehicle;
    }
}

