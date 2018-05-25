package sample;

/**
 * This class stores the column and row index values to produce coordinates and work with them.
 * @author Group 5 - Chris Armstrong, Edbert Chung, Huai Dong Loo, Pranav Singh, Utkarsh Sood.
 */
public class Coordinate {
    /** x coord */
    int colIndex;
    /** y coord */
    int rowIndex;

    /**
     * Contructor used to create a coordinate object with a column and row index.
     * @param colIndex The column index of the coordinate.
     * @param rowIndex The row index of the coordinate.
     */
    public Coordinate(int colIndex, int rowIndex) {
        this.colIndex = colIndex;
        this.rowIndex = rowIndex;
    }

    /**
     * Used to check if the coordinate given to the method is the same as this object.
     * @param c The coordinate to check if it is equal.
     * @return True or false based on if c is or is not equal.
     */
    public boolean isEqual(Coordinate c) {
        boolean b = this.colIndex == c.colIndex && this.rowIndex == c.rowIndex;
        return b;
    }

    /**
     * Converts the row and column indexes to Strings.
     * @return An array with the row and column values as strings.
     */
    public String toString() {
        return "{" + this.colIndex + ", " + this.rowIndex + "}";
    }

    /**
     * Gives coordinates for a vehicle given the parameters below.
     * @param orientation The orientation of the vehicle.
     * @param file The row or column that the vehicle is in.
     * @param space The space that the vehicle is in.
     * @return The coordinate to add to the grid.
     */
    public static Coordinate fromOrientation(Vehicle.Orientation orientation, int file, int space) {
        if (orientation == Vehicle.Orientation.HORIZONTAL) {
            return new Coordinate(space, file);
        } else {
            return new Coordinate(file, space);
        }
    }

    /**
     * Gets the index value of the column on the grid for a coordinate.
     * @return The column index value.
     */
    public int getColIndex() {
    	return colIndex;
    }

    /**
     * Gets the index value of the row on the grid for a coordinate.
     * @return The row index value.
     */
    public int getRowIndex() {
    	return rowIndex;
    }
}

