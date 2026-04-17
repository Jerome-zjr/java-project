package com.roguelike3d.ui;

import com.roguelike3d.entity.Player;
import com.roguelike3d.map.DungeonMap;
import com.roguelike3d.map.Tile;

import java.awt.*;
import java.util.List;

/**
 * Draws the in-game heads-up display:
 * HP bar, floor/score, crosshair, combat messages, minimap and stairs hint.
 */
public class HUD {

    private static final int    BAR_W    = 180;
    private static final int    BAR_H    = 18;
    private static final int    MAP_TILE = 4;     // minimap pixel per tile
    private static final Color  MSG_COL  = new Color(255, 240, 160);
    private static final Color  BG       = new Color(0, 0, 0, 140);

    private final int sw;   // screen width
    private final int sh;   // screen height

    public HUD(int sw, int sh) {
        this.sw = sw;
        this.sh = sh;
    }

    public void render(Graphics2D g, Player player,
                       List<String> messages, DungeonMap map,
                       int stairsX, int stairsY) {
        drawHpBar(g, player);
        drawStats(g, player);
        drawCrosshair(g);
        drawMessages(g, messages);
        drawMinimap(g, map, player);
        drawStairsHint(g, player, stairsX, stairsY);
        drawControls(g);
    }

    // -------------------------------------------------------------------

    private void drawHpBar(Graphics2D g, Player player) {
        int x = 10, y = sh - 38;
        g.setColor(BG);
        g.fillRoundRect(x - 4, y - 4, BAR_W + 70, BAR_H + 8, 6, 6);

        g.setColor(Color.DARK_GRAY);
        g.fillRect(x, y, BAR_W, BAR_H);

        float pct = (float) player.getHp() / player.getMaxHp();
        Color fill = pct > 0.5f ? new Color(60, 200, 60)
                   : pct > 0.25f ? Color.YELLOW
                   : new Color(220, 40, 40);
        g.setColor(fill);
        g.fillRect(x, y, (int) (BAR_W * pct), BAR_H);

        g.setColor(Color.WHITE);
        g.drawRect(x, y, BAR_W, BAR_H);
        g.setFont(new Font("Arial", Font.BOLD, 13));
        g.drawString("HP  " + player.getHp() + "/" + player.getMaxHp(),
                x + BAR_W + 6, y + 13);
    }

    private void drawStats(Graphics2D g, Player player) {
        g.setFont(new Font("Arial", Font.BOLD, 15));
        g.setColor(BG);
        g.fillRoundRect(sw - 148, 6, 140, 52, 6, 6);
        g.setColor(Color.YELLOW);
        g.drawString("Floor : " + player.getFloor(), sw - 140, 24);
        g.drawString("Score : " + player.getScore(), sw - 140, 46);
    }

    private void drawCrosshair(Graphics2D g) {
        int cx = sw / 2, cy = sh / 2;
        g.setColor(new Color(255, 255, 255, 200));
        g.drawLine(cx - 10, cy, cx - 3, cy);
        g.drawLine(cx +  3, cy, cx + 10, cy);
        g.drawLine(cx, cy - 10, cx, cy -  3);
        g.drawLine(cx, cy +  3, cx, cy + 10);
    }

    private void drawMessages(Graphics2D g, List<String> messages) {
        g.setFont(new Font("Arial", Font.PLAIN, 13));
        int y = sh - 60;
        for (int i = messages.size() - 1; i >= 0; i--) {
            float alpha = 0.4f + 0.6f * ((float)(i + 1) / messages.size());
            Color c = new Color(MSG_COL.getRed() / 255f,
                                MSG_COL.getGreen() / 255f,
                                MSG_COL.getBlue() / 255f,
                                alpha);
            g.setColor(c);
            g.drawString(messages.get(i), 12, y);
            y -= 17;
        }
    }

    private void drawMinimap(Graphics2D g, DungeonMap map, Player player) {
        int mapW = map.getWidth();
        int mapH = map.getHeight();
        int ox   = sw - mapW * MAP_TILE - 10;
        int oy   = sh - mapH * MAP_TILE - 10;

        // Semi-transparent background
        g.setColor(new Color(0, 0, 0, 120));
        g.fillRect(ox - 1, oy - 1, mapW * MAP_TILE + 2, mapH * MAP_TILE + 2);

        for (int x = 0; x < mapW; x++) {
            for (int y = 0; y < mapH; y++) {
                Tile t = map.getTile(x, y);
                Color tc;
                switch (t) {
                    case FLOOR        -> tc = new Color( 80,  80,  80, 180);
                    case STAIRS_DOWN  -> tc = new Color(200, 180,  50, 200);
                    default           -> tc = new Color( 20,  20,  20, 180);
                }
                g.setColor(tc);
                g.fillRect(ox + x * MAP_TILE, oy + y * MAP_TILE,
                           MAP_TILE, MAP_TILE);
            }
        }

        // Player dot
        int px = ox + (int) player.getX() * MAP_TILE;
        int py = oy + (int) player.getY() * MAP_TILE;
        g.setColor(Color.CYAN);
        g.fillRect(px, py, MAP_TILE, MAP_TILE);

        // Player direction line
        g.setColor(Color.CYAN);
        g.drawLine(px + MAP_TILE / 2,
                   py + MAP_TILE / 2,
                   px + MAP_TILE / 2 + (int)(Math.cos(player.getAngle()) * 6),
                   py + MAP_TILE / 2 + (int)(Math.sin(player.getAngle()) * 6));
    }

    private void drawStairsHint(Graphics2D g, Player player, int stairsX, int stairsY) {
        double dx = player.getX() - (stairsX + 0.5);
        double dy = player.getY() - (stairsY + 0.5);
        if (Math.sqrt(dx * dx + dy * dy) < 1.5) {
            g.setFont(new Font("Arial", Font.BOLD, 20));
            String hint = "▼  Press F  to descend";
            FontMetrics fm = g.getFontMetrics();
            int tx = (sw - fm.stringWidth(hint)) / 2;
            g.setColor(new Color(0, 0, 0, 160));
            g.fillRoundRect(tx - 10, 60, fm.stringWidth(hint) + 20, 30, 6, 6);
            g.setColor(Color.YELLOW);
            g.drawString(hint, tx, 80);
        }
    }

    private void drawControls(Graphics2D g) {
        g.setFont(new Font("Arial", Font.PLAIN, 11));
        g.setColor(new Color(180, 180, 180, 140));
        g.drawString("W/↑: Fwd   S/↓: Back   A/←: Rotate L   D/→: Rotate R   Q/E: Strafe   SPACE: Attack   F: Stairs",
                10, sh - 5);
    }
}
