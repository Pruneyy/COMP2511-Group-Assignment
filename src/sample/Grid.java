package sample;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Data structure representing the layout of all vehicles on the playing field.
 * 0, 0 at top left, positive x going left and positive y going down.
 * @author Group 5 - Chris Armstrong, Edbert Chung, Huai Dong Loo, Pranav Singh, Utkarsh Sood.
 */
public class Grid {

    static final int GRID_SIZE = 6;
    static final int RED_ID = 1;

    private ArrayList<Vehicle> vehicles;
    private ArrayList<Move> moves;

    /**
     * Constructor used to construct a Grid object containing an array list of vehicles and an array
     * list of moves made.
     */
    public Grid() {
        this.vehicles = new ArrayList<>();
        this.moves = new ArrayList<>();
    }

    /**
     * Adds a vehicle to the grid, if it does not collide with any other vehicles already in the grid.
     * @param vehicle The vehicle to add.
     * @return True if the vehicle was added.
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
     * Attempts to move the vehicle in the positive direction by 1, checking if the move is valid.
     * @param vehicle The vehicle to move.
     * @return True if move is successful.
     */
    public boolean moveVehicleForward(Vehicle vehicle) {
        return moveVehicleBySteps(vehicle.getCarId(), 1);
    }

    /**
     * Attempts to move the vehicle in the negative direction by 1, checking if the move is valid.
     * @param vehicle The vehicle to move.
     * @return True if move is successful.
     */
    public boolean moveVehicleBackward(Vehicle vehicle) {
        return moveVehicleBySteps(vehicle.getCarId(), -1);
    }

    /**
     * Moves the vehicle by a certan number of steps and checks if the move is valid.
     * If it is then the vehicle is moved there.
     * @param vehicleId The vehicle to move.
     * @param steps The number of steps to move the vehicle.
     * @return True if move is successful.
     */
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

    /**
     * The index is the first space for which the vehicle is to move to.
     * @param vehicleId The vehicle to to move.
     * @param index The index to move the vehicle to.
     * @return True if the move is successful.
     */
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

    /**
     * Checks if the position of the car is valif to avoid collisions.
     * @param vehicle The vehicles being checked.
     * @return True if there are no collisions.
     */
    private boolean vehicleIsValid(Vehicle vehicle) {
        if (vehicle.getFirstSpaceOccupied() < 0 || vehicle.getLastSpaceOccupied() >= GRID_SIZE) return false;
        for (Vehicle v : this.vehicles) {
            if (vehicle.getCarId() == v.getCarId()) continue;
            if (vehicle.collidesWith(v)) return false;
        }
        return true;
    }

    /**
     * Computes a 2d array of the layout of the grid.
     * 0 is empty, while a positive integer is the id of the car.
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

    /**
     * Gives the vehicle that has ben requested by th caller.
     * @param id The id of the veicle wanted.
     * @return The vehicle requested if it is in the vehicle list otherwise null.
     */
    public Vehicle getVehicleById(int id) {
        for (Vehicle v : this.vehicles) {
            if (v.getCarId() == id) return v;
        }
        return null;
    }

    /**
     * Prints the grid as a flipped matrix after converting it to String format.
     * @return An array matrix as a string.
     */
    public String toString() {
        int[][] grid = this.getGridObject();
        return Arrays.deepToString(grid)
              .replace("], ", "]\n")
              .replace("[[", "[")
              .replace("]]", "]");
    }

    /**
     * Gets the vehicle array list.
     * @return An array list of vehicles.
     */
    public ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }

    /**
     * Gets the moves array list.
     * @return An array list of moves.
     */
    public ArrayList<Move> getMoves() {
        return moves;
    }

    /**
     * Will find the state of the vehicles that was last moved by checking the moves array list
     * and restore it to that position on the grid.
     * @return The vehicle state before the most recent move.
     */
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

