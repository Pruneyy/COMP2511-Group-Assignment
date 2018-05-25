package sample;

/**
 * Used to set the difficulty of the game that will be passed to the puzzle generator.
 * enum allows for more flexibility in managing difficulties.
 * @author Group 5 - Chris Armstrong, Edbert Chung, Huai Dong Loo, Pranav Singh, Utkarsh Sood.
 */
public interface PuzzleService {
    enum Difficulty {
        EASY, MEDIUM, HARD, DEMO
    };

    /**
     * Returns a new puzzle to be solved, in the form of a Grid with cars already populated.
     * @param d The difficulty set by the user.
     * @return A grid representation of the puzzle that is adapted to the UI.
     */
    Grid getNewPuzzle(Difficulty d);
}
