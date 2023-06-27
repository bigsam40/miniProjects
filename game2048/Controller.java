package miniProjects.game2048;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Controller extends KeyAdapter {
    private static final int WINNING_TILE = 2048;
    private final Model model;
    private final View view;

    public Controller(Model model) {
        this.model = model;
        view = new View(this);
    }

    public void resetGame() {
        view.isGameLost = false;
        view.isGameWon = false;
        model.maxTile = 0;
        model.score = 0;
        model.resetGameTiles();
    }

    public View getView() {
        return view;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            resetGame();
        }
        if (!model.canMove()) view.isGameLost = true;
        if (!view.isGameWon && !view.isGameLost) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                model.left();
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                model.right();
            } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                model.up();
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                model.down();
            } else if (e.getKeyCode() == KeyEvent.VK_Z) {
                model.rollback();
            } else if (e.getKeyCode() == KeyEvent.VK_R) {
                model.randomMove();
            } else if (e.getKeyCode() == KeyEvent.VK_A) {
                model.autoMove();
            }
        }
        if (model.maxTile == WINNING_TILE) view.isGameWon = true;
        view.repaint();
    }

    public Tile[][] getGameTiles() {
        return model.getGameTiles();
    }

    public int getScore() {
        return model.score;
    }
}
