package sample;

/**
 * Used to set the difficulty of the game that will be passed to the puzzle generator.
 * enum allows for more flexibility in managing difficulties.
 * @author dong
 */
public interface PuzzleService {
    enum Difficulty {
        EASY, MEDIUM, HARD, DEMO
    };

    /** Returns a new puzzle to be solved, in the form of a Grid with cars already populated */
    Grid getNewPuzzle(Difficulty d);
}
