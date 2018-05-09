package sample;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Vehicle {
    public enum Orientation {
        VERTICAL, HORIZONTAL
    };

    private int carId;
    private Orientation orientation;

    /** the row/column index of the Vehicle */
    private int file;
    private int firstSpaceOccupied;
    private int lastSpaceOccupied;

    /** Not sure if necessary, but a bonus to be able to track each invidivual vehicle's movements */
    private ArrayList<Move> moves;

    public Vehicle(int carId, Orientation orientation, int file, int firstSpaceOccupied, int lastSpaceOccupied) {
        this.carId = carId;
        this.orientation = orientation;
        this.file = file;
        this.firstSpaceOccupied = firstSpaceOccupied;
        this.lastSpaceOccupied = lastSpaceOccupied;
        this.moves = new ArrayList<>();
    }

    public List<Coordinate> getOccupiedSpaces() {
        return IntStream.range(this.firstSpaceOccupied, this.lastSpaceOccupied + 1)
              .boxed()
              .map(i -> Coordinate.fromOrientation(this.orientation, this.file, i))
              .collect(Collectors.toList());
    }

    public boolean collidesWith(Vehicle v) {
        for (Coordinate c1 : this.getOccupiedSpaces()) {
            for (Coordinate c2 : v.getOccupiedSpaces()) {
                if (c1.isEqual(c2)) return true;
            }
        }
        return false;
    }

    /** Assumes the move is valid, moves the vehicle by steps in the positive direction on the file */
    public Move move(int steps) {
        Move move = new Move(this, steps);
        this.moves.add(move);
        this.firstSpaceOccupied += steps;
        this.lastSpaceOccupied += steps;
        return move;
    }

    public Vehicle clone() {
        return new Vehicle(
              this.carId,
              this.orientation,
              this.file,
              this.firstSpaceOccupied,
              this.lastSpaceOccupied);
    }

    public String toString() {
        return carId + " " + this.getOccupiedSpaces().toString();
    }

    public int getCarId() {
        return carId;
    }

    public int getFirstSpaceOccupied() {
        return firstSpaceOccupied;
    }

    public int getLastSpaceOccupied() {
        return lastSpaceOccupied;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public int getLength() {
        return this.getLastSpaceOccupied() - this.getFirstSpaceOccupied() + 1;
    }
}
