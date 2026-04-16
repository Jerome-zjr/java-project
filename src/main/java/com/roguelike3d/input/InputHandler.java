package com.roguelike3d.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

/**
 * Tracks keyboard state: which keys are currently held and which were
 * pressed for the first time this frame.
 */
public class InputHandler extends KeyAdapter {

    private final Set<Integer> held      = new HashSet<>();
    private final Set<Integer> justDown  = new HashSet<>();

    @Override
    public void keyPressed(KeyEvent e) {
        if (!held.contains(e.getKeyCode())) {
            justDown.add(e.getKeyCode());
        }
        held.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        held.remove(e.getKeyCode());
    }

    /** True while the key is held down. */
    public boolean isHeld(int keyCode) {
        return held.contains(keyCode);
    }

    /** True only on the first frame the key was pressed. */
    public boolean wasJustPressed(int keyCode) {
        return justDown.contains(keyCode);
    }

    /** Must be called once per frame after processing input. */
    public void endFrame() {
        justDown.clear();
    }
}
