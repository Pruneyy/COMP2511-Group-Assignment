package sample;

/**
 * A Service to supply puzzles
 * @author dong
 */
public interface PuzzleService {
    /** Returns a new puzzle to be solved, in the form of a Grid with cars already populated */
    Grid getNewPuzzle();
}
