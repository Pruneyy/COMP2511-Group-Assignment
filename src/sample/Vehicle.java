package sample;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Vehicle {
    public enum Orientation {
        VERTICAL, HORIZONTAL
    };

    public class Coordinate {
        /** x coord */
        int colIndex;
        /** y coord */
        int rowIndex;

        public Coordinate(int colIndex, int rowIndex) {
            this.colIndex = colIndex;
            this.rowIndex = rowIndex;
        }

        public boolean isEqual(Coordinate c) {
            boolean b = this.colIndex == c.colIndex && this.rowIndex == c.rowIndex;
            return b;
        }

        public String toString() {
            return "{" + this.colIndex + ", " + this.rowIndex + "}";
        }
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
        if (this.orientation == Orientation.VERTICAL) {
            return IntStream.range(this.firstSpaceOccupied, this.lastSpaceOccupied)
                  .boxed()
                  .map(i -> new Coordinate(this.file, i))
                  .collect(Collectors.toList());
        } else if (this.orientation == Orientation.HORIZONTAL){
            return IntStream.range(this.firstSpaceOccupied, this.lastSpaceOccupied)
                  .boxed()
                  .map(i -> new Coordinate(i, this.file))
                  .collect(Collectors.toList());
        }
        System.err.println("Unrecognised orientation");
        return new ArrayList<>();
    }

    public boolean collidesWith(Vehicle v) {
        return this.getOccupiedSpaces().stream()
              .filter(coord1 -> v.getOccupiedSpaces().stream().filter(coord2 -> coord1.isEqual(coord2)).collect(Collectors.toList()).size() > 0)
              .collect(Collectors.toList())
              .size() > 0;
    }

    public String toString() {

    }

}
