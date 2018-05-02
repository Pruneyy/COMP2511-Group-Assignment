package sample;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Vehicle {
    public enum Orientation {
        VERTICAL, HORIZONTAL
    };

    public int getCarId() {
        return carId;
    }

    private int carId;
    private Orientation orientation;
    /** the row/column index of the Vehicle */
    private int file;
    private int firstSpaceOccupied;
    private int lastSpaceOccupied;

    public Vehicle(int carId, Orientation orientation, int file, int firstSpaceOccupied, int lastSpaceOccupied) {
        this.carId = carId;
        this.orientation = orientation;
        this.file = file;
        this.firstSpaceOccupied = firstSpaceOccupied;
        this.lastSpaceOccupied = lastSpaceOccupied;
        System.out.println(this.getOccupiedSpaces());
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

    public String toString() {
        return carId + " " + this.getOccupiedSpaces().toString();
    }

}
