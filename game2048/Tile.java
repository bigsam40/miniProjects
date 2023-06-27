package miniProjects.game2048;

import java.awt.*;

public class Tile {
    int value;

    public Tile() {
        value = 0;
    }

    public Tile(int value) {
        this.value = value;
    }

    public boolean isEmpty() {
        return value == 0;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Color getFontColor() {
        return value < 16 ? new Color(0x776e65) : new Color(0xf9f6f2);
    }

    public Color getTileColor() {
        Color color = new Color(0xff0000);
        if (value == 0) {
            color = new Color(0xcdc1b4);
        } else if (value == 2) {
            color = new Color(0xeee4da);
        } else if (value == 4) {
            color = new Color(0xede0c8);
        } else if (value == 8) {
            color = new Color(0xf2b179);
        } else if (value == 16) {
            color = new Color(0xf59563);
        } else if (value == 32) {
            color = new Color(0xf67c5f);
        } else if (value == 64) {
            color = new Color(0xf65e3b);
        } else if (value == 128) {
            color = new Color(0xedcf72);
        } else if (value == 256) {
            color = new Color(0xedcc61);
        } else if (value == 512) {
            color = new Color(0xedc850);
        } else if (value == 1024) {
            color = new Color(0xedc53f);
        } else if (value == 2048) {
            color = new Color(0xedc22e);
        }
        return color;
    }
}
