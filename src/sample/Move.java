package sample;

/**
 * Represents a single move of a vehicle.
 * @author Group 5 - Chris Armstrong, Edbert Chung, Huai Dong Loo, Pranav Singh, Utkarsh Sood.
 */
public class Move {
    private Vehicle vehicle;    // The vehicle in question
    private int steps;          // Steps taken by the car (negative means moving backwards)

    /**
     * Constructor to construct the move object.
     * @param vehicle Vehicle to track next move on.
     * @param steps Number of steps the vehicle took.
     */
    public Move(Vehicle vehicle, int steps) {
        this.vehicle = vehicle;
        this.steps = steps;
    }

    /**
     * Will reverse the number of steps taken by a vehicle.
     */
    public void revert() {
        this.vehicle.move(-steps);
        this.steps = -this.steps;
    }

    /**
     * Used to get the vehicle.
     * @return The vehicle in question.
     */
    public Vehicle getVehicle() {
        return vehicle;
    }
}

