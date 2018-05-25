package sample;

import java.util.ArrayList;

/**
 * Repesents the ordered set of movements, from multiple vehicle.
 * @author Group 5 - Chris Armstrong, Edbert Chung, Huai Dong Loo, Pranav Singh, Utkarsh Sood.
 */
public class MoveSet {
    private ArrayList<Move> moves;
    private int currentIndex;

    /**
     * Creates a new array list of Move objects.
     */
    public MoveSet() {
        this.moves = new ArrayList<>();
        this.currentIndex = -1;
    }

    /**
     * Adds a new move to the array list of moves.
     * @param m an object containing the vehicle and the number of steps taken to reach its current position.
     */
    public void addMove(Move m) {
        this.moves.add(m);
        this.currentIndex += 1;
    }

    /**
     * Returns a vehicle to its last position by reversing the last move made in the move array lis.
     * @return parameter to acknowledge that a move has been undone.
     */
    public boolean undo() {
        if (this.currentIndex < 0) return false;
        this.moves.get(this.currentIndex).revert();
        this.currentIndex -= 1;
        return true;
    }

    public boolean redo() {
        if (this.currentIndex >= this.moves.size() - 1) return false;
        this.moves.get(this.currentIndex + 1).revert();
        this.currentIndex += 1;
        return true;
    }
}

