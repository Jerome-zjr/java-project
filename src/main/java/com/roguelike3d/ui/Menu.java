package com.roguelike3d.ui;

import java.awt.*;

/**
 * Title screen / main menu.
 */
public class Menu {

    private final int sw;
    private final int sh;

    public Menu(int sw, int sh) {
        this.sw = sw;
        this.sh = sh;
    }

    public void render(Graphics2D g) {
        // Background gradient
        GradientPaint bg = new GradientPaint(0, 0,     new Color( 10,  10, 30),
                                             0, sh,    new Color( 40,  10, 10));
        g.setPaint(bg);
        g.fillRect(0, 0, sw, sh);

        // Title shadow
        g.setFont(new Font("Arial", Font.BOLD, 62));
        String title = "3D ROGUELIKE";
        FontMetrics fm = g.getFontMetrics();
        int tx = (sw - fm.stringWidth(title)) / 2;
        g.setColor(new Color(100, 20, 20));
        g.drawString(title, tx + 4, sh / 3 + 4);
        // Title
        g.setColor(new Color(220, 50, 50));
        g.drawString(title, tx, sh / 3);

        // Subtitle
        g.setFont(new Font("Arial", Font.ITALIC, 22));
        fm = g.getFontMetrics();
        String sub = "Dungeon Crawler";
        g.setColor(new Color(200, 150, 90));
        g.drawString(sub, (sw - fm.stringWidth(sub)) / 2, sh / 3 + 48);

        // Menu options
        g.setFont(new Font("Arial", Font.PLAIN, 26));
        fm = g.getFontMetrics();
        int oy = sh / 2 + 20;
        drawCentered(g, fm, "Press  ENTER  to Start", new Color(220, 220, 220), oy);
        drawCentered(g, fm, "Press  ESC  to Quit",    new Color(180, 180, 180), oy + 50);
        drawCentered(g, fm, "Goal: Reach Floor 18",  new Color(200, 200, 160), oy + 95);

        // Controls
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        fm = g.getFontMetrics();
        g.setColor(new Color(130, 130, 130));
        drawCentered(g, fm, "WASD / Arrow Keys: Move & Rotate   Q / E: Strafe", sh - 55);
        drawCentered(g, fm, "SPACE: Attack   F: Descend Stairs", sh - 35);
    }

    private void drawCentered(Graphics2D g, FontMetrics fm, String s, Color c, int y) {
        g.setColor(c);
        g.drawString(s, (sw - fm.stringWidth(s)) / 2, y);
    }

    private void drawCentered(Graphics2D g, FontMetrics fm, String s, int y) {
        g.drawString(s, (sw - fm.stringWidth(s)) / 2, y);
    }
}
