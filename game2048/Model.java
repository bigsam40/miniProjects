package miniProjects.game2048;

import java.util.*;

public class Model {
    private static final int FIELD_WIDTH = 4;
    private final Stack<Tile[][]> previousStates = new Stack<>();
    private final Stack<Integer> previousScores = new Stack<>();
    int score;
    int maxTile;
    private Tile[][] gameTiles;
    private boolean isSaveNeeded = true;

    public Model() {
        resetGameTiles();
    }

    public Tile[][] getGameTiles() {
        return gameTiles;
    }

    void resetGameTiles() {
        gameTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                gameTiles[i][j] = new Tile();
            }
        }
        previousStates.clear();
        previousScores.clear();
        addTile();
        addTile();
        score = 0;
        maxTile = 0;
    }

    private void saveState(Tile[][] gameTiles) {
        previousScores.push(score);
        Tile[][] tilesToSave = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                tilesToSave[i][j] = new Tile(gameTiles[i][j].value);
            }
        }
        previousStates.push(tilesToSave);
        isSaveNeeded = false;
    }

    public void rollback() {
        if (!previousScores.isEmpty() && !previousStates.isEmpty()) {
            score = previousScores.pop();
            gameTiles = previousStates.pop();
        }
    }

    public boolean canMove() {
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                if (gameTiles[i][j].value == 0) return true;
                if (i < FIELD_WIDTH - 1)
                    if (gameTiles[i][j].value == gameTiles[i + 1][j].value) return true;
                if (j < FIELD_WIDTH - 1)
                    if (gameTiles[i][j].value == gameTiles[i][j + 1].value) return true;
            }
        }
        return false;
    }

    private void addTile() {
        List<Tile> emptyTiles = getEmptyTiles();
        if (!emptyTiles.isEmpty()) {
            Tile tileToAdd = emptyTiles.get((int) (emptyTiles.size() * Math.random()));
            tileToAdd.setValue(Math.random() < 0.9 ? 2 : 4);
        }
    }

    private List<Tile> getEmptyTiles() {
        List<Tile> emptyTiles = new ArrayList<>();
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                if (gameTiles[i][j].isEmpty()) {
                    emptyTiles.add(gameTiles[i][j]);
                }
            }
        }
        return emptyTiles;
    }

    private boolean compressTiles(Tile[] tiles) {
        boolean isChanged = false;
        int counter = 0;
        for (int i = 0; i < tiles.length; i++) {
            if (tiles[i].value > 0) {
                tiles[counter].value = tiles[i].value;
                if (counter != i) {
                    tiles[i].value = 0;
                    isChanged = true;
                }
                counter++;
            }
        }
        return isChanged;
    }

    private boolean mergeTiles(Tile[] tiles) {
        boolean isChanged = false;
        for (int i = 0; i < tiles.length - 1; i++) {
            if ((tiles[i].value == tiles[i + 1].value) && tiles[i].value != 0) {
                tiles[i].value = tiles[i].value * 2;
                tiles[i + 1].value = 0;
                isChanged = true;
                score += tiles[i].value;
                if (maxTile < tiles[i].value) {
                    maxTile = tiles[i].value;
                }
            }
        }
        if (isChanged) compressTiles(tiles);
        return isChanged;
    }

    public void randomMove() {
        autoMove();
    }

    public void autoMove() {
        PriorityQueue<MoveEfficiency> priorityQueue = new PriorityQueue<>(4, Collections.reverseOrder());
        priorityQueue.offer(getMoveEfficiency(this::left));
        priorityQueue.offer(getMoveEfficiency(this::up));
        priorityQueue.offer(getMoveEfficiency(this::down));
        priorityQueue.offer(getMoveEfficiency(this::right));
        if (priorityQueue.peek() != null) {
            priorityQueue.peek().getMove().move();
        }
    }

    boolean hasBoardChanged() {
        Tile[][] lastMoveTiles = previousStates.peek();
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                if (lastMoveTiles[i][j].value != gameTiles[i][j].value) return true;
            }
        }
        return false;
    }

    MoveEfficiency getMoveEfficiency(Move move) {
        MoveEfficiency moveEfficiency = new MoveEfficiency(-1, 0, move);
        move.move();
        if (hasBoardChanged()) {
            moveEfficiency = new MoveEfficiency(getEmptyTiles().size(), score, move);
        }
        rollback();
        return moveEfficiency;
    }

    void left() {
        if (isSaveNeeded) {
            saveState(gameTiles);
        }
        boolean isChanged = false;
        for (Tile[] tilesRow : gameTiles) {
            if (compressTiles(tilesRow) | mergeTiles(tilesRow)) isChanged = true;
        }
        if (isChanged) addTile();
        isSaveNeeded = true;
    }

    void down() {
        saveState(gameTiles);
        rotateClockwise();
        left();
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
    }

    void right() {
        saveState(gameTiles);
        rotateClockwise();
        rotateClockwise();
        left();
        rotateClockwise();
        rotateClockwise();
    }

    void up() {
        saveState(gameTiles);
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
        left();
        rotateClockwise();
    }

    private void rotateClockwise() {
        Tile[][] tempGameField = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for (int i = 0; i < FIELD_WIDTH; i++) {
            tempGameField[i] = Arrays.copyOf(gameTiles[i], gameTiles[i].length);
        }
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                gameTiles[j][gameTiles.length - 1 - i] = tempGameField[i][j];
            }
        }
    }
}
