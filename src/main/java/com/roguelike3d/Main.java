package com.roguelike3d;

import javax.swing.SwingUtilities;

/**
 * Entry point for the 3D Roguelike Dungeon game.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Game game = new Game();
            game.start();
        });
    }
}
