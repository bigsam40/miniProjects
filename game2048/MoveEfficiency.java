package miniProjects.game2048;

import org.jetbrains.annotations.NotNull;

public class MoveEfficiency implements Comparable<MoveEfficiency> {
    private final int numberOfEmptyTiles;
    private final int score;
    private final Move move;

    public MoveEfficiency(int numberOfEmptyTiles, int score, Move move) {
        this.numberOfEmptyTiles = numberOfEmptyTiles;
        this.score = score;
        this.move = move;
    }

    @Override
    public int compareTo(@NotNull MoveEfficiency o) {
        if (this.numberOfEmptyTiles > o.numberOfEmptyTiles)
            return 1;
        else if (this.numberOfEmptyTiles == o.numberOfEmptyTiles) {
            if (this.score > o.score) {
                return 1;
            } else if (this.score == o.score) {
                return 0;
            }
        }
        return -1;
    }

    public Move getMove() {
        return move;
    }
}
