package sample;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * A Vehicle object that stores all the main attributes of a vehicle and has methods to check for collisions and more.
 * @author Group 5 - Chris Armstrong, Edbert Chung, Huai Dong Loo, Pranav Singh, Utkarsh Sood.
 */
public class Vehicle {
    public enum Orientation {
        VERTICAL, HORIZONTAL
    };

    private int carId;
    private Orientation orientation;

    /** The row/column index of the Vehicle */
    private int file;
    private int firstSpaceOccupied;
    private int lastSpaceOccupied;

    private ArrayList<Move> moves;

    /**
     * Constructor used to create a new vehicle object.
     * @param carId The ID number of the new vehicle.
     * @param orientation The orientation of the new vehicle.
     * @param file The row or column on the grid of the new vehicle.
     * @param firstSpaceOccupied The first space the new vehicle will occupy.
     * @param lastSpaceOccupied The last space the new vehicle will occupy.
     */
    public Vehicle(int carId, Orientation orientation, int file, int firstSpaceOccupied, int lastSpaceOccupied) {
        this.carId = carId;
        this.orientation = orientation;
        this.file = file;
        this.firstSpaceOccupied = firstSpaceOccupied;
        this.lastSpaceOccupied = lastSpaceOccupied;
        this.moves = new ArrayList<>();
    }

    /**
     * Gets all of the spaces that are occupied by vehicles.
     * @return A list of Coordinates that are occupied.
     */
    public List<Coordinate> getOccupiedSpaces() {
        return IntStream.range(this.firstSpaceOccupied, this.lastSpaceOccupied + 1)
              .boxed()
              .map(i -> Coordinate.fromOrientation(this.orientation, this.file, i))
              .collect(Collectors.toList());
    }

    /**
     * Checks to see if a vehicle has a collision with another one when it is placed.
     * @param v The vehicle being checked.
     * @return False if there are no collisions in its placement.
     */
    public boolean collidesWith(Vehicle v) {
        for (Coordinate c1 : this.getOccupiedSpaces()) {
            for (Coordinate c2 : v.getOccupiedSpaces()) {
                if (c1.isEqual(c2)) return true;
            }
        }
        return false;
    }

    /**
     * Assumes the move is valid, moves the vehicle by steps in the positive direction on the file (row or column).
     * @param steps The number of steps to move the vehicle down the file.
     * @return A Move object with the details of the move executed.
     */
    public Move move(int steps) {
        Move move = new Move(this, steps);
        this.moves.add(move);
        this.firstSpaceOccupied += steps;
        this.lastSpaceOccupied += steps;
        return move;
    }

    /**
     * Makes a copy of this instance of the vehicle.
     * @return A copy of the vehicle.
     */
    public Vehicle clone() {
        return new Vehicle(
              this.carId,
              this.orientation,
              this.file,
              this.firstSpaceOccupied,
              this.lastSpaceOccupied);
    }

    /**
     * Converts the occupied spaces for a particular vehicle as a String.
     * @return A string with the occupied spaces of a vehicle.
     */
    public String toString() {
        return carId + " " + this.getOccupiedSpaces().toString();
    }

    /**
     * Gets the ID of the vehicle.
     * @return The ID of the vehicle.
     */
    public int getCarId() {
        return carId;
    }

    /**
     * Gets the first space occupied by a vehicle.
     * @return The first space occupied by a vehicle.
     */
    public int getFirstSpaceOccupied() {
        return firstSpaceOccupied;
    }

    /**
     * Gets the last space occupied by a vehicle.
     * @return The last space occupied by a vehicle.
     */
    public int getLastSpaceOccupied() {
        return lastSpaceOccupied;
    }

    /**
     * Gets the orientation of a vehicle.
     * @return The orientation of a vehicle.
     */
    public Orientation getOrientation() {
        return orientation;
    }

    /**
     * Calculates the length of the vehicle.
     * @return The length of the vehicle.
     */
    public int getLength() {
        return this.getLastSpaceOccupied() - this.getFirstSpaceOccupied() + 1;
    }
}
