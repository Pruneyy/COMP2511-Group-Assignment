package sample;

/**
 * Represents a single move of a vehicle
 * @author dong
 */
public class Move {
    private Vehicle vehicle;    // The vehicle in question
    private int steps;          // Steps taken by the car (negative means moving backwards)

    /**
     * @param vehicle Vehicle to track next move on
     * @param steps number of steps the vehicle took
     */

    public Move(Vehicle vehicle, int steps) {
        this.vehicle = vehicle;
        this.steps = steps;
    }

    public void revert() {
        this.vehicle.move(-steps);
        this.steps = -this.steps;
    }
}

