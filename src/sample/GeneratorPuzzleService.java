package sample;

/**
 * An implementation of the Puzzle Services interface which will load a new puzzle from one of the
 * a random generation algorithm.
 * @author Group 5 - Chris Armstrong, Edbert Chung, Huai Dong Loo, Pranav Singh, Utkarsh Sood.
 */
public class GeneratorPuzzleService implements PuzzleService {
    private static final int STEP_ITERATIONS = 1000;
    private MoveSet moveSet;
    private int noOfCars;

    /**
     * Constructor that stores the moves made as well as the number of max cars on the maps.
     */
    public GeneratorPuzzleService() {
        this.moveSet = new MoveSet();
        this.noOfCars = 7;
        assert(this.noOfCars > 1);
    }

    /**
     * A method that will generate the end (solved) state of the puzzle.
     * @return A grid that will reversed into a start state for the puzzle.
     */
    private Grid generateEndState() {
        Grid g = new Grid();
        g.addVehicle(new Vehicle(1, Vehicle.Orientation.HORIZONTAL, 2, 4, 5));

        int i = 0;
        while (i < this.noOfCars) {
            int carLength = randomCarLength();
            if (carLength == 2) {
                int firstSpace = randomInt(0, 4);
                if (!g.addVehicle(
                      new Vehicle(i + 2,
                            randomOrientation(),
                            randomInt(0, 5),
                            firstSpace,
                            firstSpace + 1)
                )) continue;
            } else {
                int firstSpace = randomInt(0, 3);
                if (!g.addVehicle(
                      new Vehicle(i + 2,
                            randomOrientation(),
                            randomInt(0, 5),
                            firstSpace,
                            firstSpace + 2)
                )) continue;
            }
            i++;
        }
        return g;
    }

    /**
     * Generates an end state and reverses it over a number of iterations to produce a randomly generated grid.
     * @param d The difficulty selected by the user.
     * @return A grid for the UI to display.
     */
    public Grid getNewPuzzle(Difficulty d) {
        // We could just use an infinite loop, but better not - handle the null case instead
        while (true) {
            Grid g = generateEndState();
            for (int i = 0; i < STEP_ITERATIONS; i++) {
                if (this.moveVehicleAndTrack(1, -1, g)) continue;
                // No better actions to take, move a vehicle at random
                this.moveVehicleAndTrack(randomInt(2, this.noOfCars), randomInt(-1, 1), g);
            }
            if (checkValidPuzzle(g)) {
                g.getMoves().clear();
                return g;
            }
        }
    }

    /**
     * Used to solve the generated puzzle.
     */
    public void solve() {
        while (this.moveSet.undo()) continue;
    }

    private boolean moveVehicleAndTrack(int id, int steps, Grid grid) {
        if (!grid.moveVehicleBySteps(id, steps)) return false;
        this.moveSet.addMove(new Move(grid.getVehicleById(id), steps));
        return true;
    }

    /**
     * Checks if a puzzle is valid by if there is an obstruction to the goal state
     */
    private boolean checkValidPuzzle(Grid g) {
        int lastSpaceOccupied = g.getVehicleById(1).getLastSpaceOccupied();
        int[][] intGrid = g.getGridObject();
        for (int x = lastSpaceOccupied + 1; x < 5; x++) {
            if (intGrid[x][2] > 0) return true;
        }
        return false;
    }

    /**
     * Randomly returns a vehicle length of 2 or 3.
     * @return The length of the vehicle.
     */
    private int randomCarLength() {
        if (Math.random() > 0.5) return 3;
        return 2;
    }

    /**
     * Randomly selects an orientation for a vehicle.
     * @return The orientation of the vehicle.
     */
    private Vehicle.Orientation randomOrientation() {
        if (Math.random() > 0.5) {
            return Vehicle.Orientation.HORIZONTAL;
        }
        return Vehicle.Orientation.VERTICAL;
    }

    /**
     * Selects a random integer between a given range.
     * @param min The minimum number of the range.
     * @param max The maximum number of the range.
     * @return The random integer selected.
     */
    private int randomInt(int min, int max) {
        double minD = (double) min;
        double maxD = (double) max;
        return (int) Math.round(minD + (Math.random() * (maxD - minD)));
    }
}
