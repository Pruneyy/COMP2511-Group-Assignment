package sample;

/**
 * Generates a puzzle to be solved
 * @author dong
 */
public class GeneratorPuzzleService implements PuzzleService {
    private static final int GENERATOR_ITERATIONS = 100;
    private static final int STEP_ITERATIONS = 1000;
    private MoveSet moveSet; //TODO (dong): Keep track of moves when generating the puzzle
    private int noOfCars;

    public GeneratorPuzzleService() {
        this.moveSet = new MoveSet();
        this.noOfCars = 7;
        assert(this.noOfCars > 1);
    }

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

    public Grid getNewPuzzle(Difficulty d) {
        // We could just use an infinite loop, but better not - handle the null case instead
        int stop = 0;
        while (stop < GENERATOR_ITERATIONS) {
            Grid g = generateEndState();
            for (int i = 0; i < STEP_ITERATIONS; i++) {
                if (this.moveVehicleAndTrack(1, -1, g)) continue;

                // No better actions to take, move a vehicle at random
                this.moveVehicleAndTrack(randomInt(2, this.noOfCars), randomInt(-1, 1), g);
//                if (Math.random() < 0.5) g.moveVehicleForward(g.getVehicleById(randomInt(2, this.noOfCars)));
//                g.moveVehicleBackward(g.getVehicleById(randomInt(2, this.noOfCars)));
            }
            if (checkValidPuzzle(g)) return g;
            stop++;
        }
        return null;
    }

    public void solve() {
        while (this.moveSet.undo()) continue;
    }

    private boolean moveVehicleAndTrack(int id, int steps, Grid grid) {
        if (!grid.moveVehicleBySteps(id, steps)) return false;
        this.moveSet.addMove(new Move(grid.getVehicleById(id), steps));
        return true;
    }

    /** A puzzle is valid if there is an obstruction to the goal state */
    private boolean checkValidPuzzle(Grid g) {
        int lastSpaceOccupied = g.getVehicleById(1).getLastSpaceOccupied();
        int[][] intGrid = g.getGridObject();
        for (int x = lastSpaceOccupied + 1; x < 5; x++) {
            if (intGrid[x][2] > 0) return true;
        }
        return false;
    }

    private int randomCarLength() {
        if (Math.random() > 0.5) return 3;
        return 2;
    }

    private Vehicle.Orientation randomOrientation() {
        if (Math.random() > 0.5) {
            return Vehicle.Orientation.HORIZONTAL;
        }
        return Vehicle.Orientation.VERTICAL;
    }

    private int randomInt(int min, int max) {
        double minD = (double) min;
        double maxD = (double) max;
        return (int) Math.round(minD + (Math.random() * (maxD - minD)));
    }
}
