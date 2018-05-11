package sample;

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

    public static Coordinate fromOrientation(Vehicle.Orientation orientation, int file, int space) {
        if (orientation == Vehicle.Orientation.HORIZONTAL) {
            return new Coordinate(space, file);
        } else {
            return new Coordinate(file, space);
        }
    }
    
    public int getColIndex() {
    	return colIndex;
    }
    
    public int getRowIndex() {
    	return rowIndex;
    }
}

