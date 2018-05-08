package sample;

import java.util.ArrayList;

/**
 * Represents an ordered set of movements, from multiple vehicles
 */
public class MoveSet {
    private ArrayList<Move> moves;
    private int currentIndex;

    public MoveSet() {
        this.moves = new ArrayList<>();
        this.currentIndex = -1;
    }

    public void addMove(Move m) {
        this.moves.add(m);
        this.currentIndex += 1;
    }

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

