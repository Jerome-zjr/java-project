package com.roguelike3d.renderer;

import com.roguelike3d.entity.Enemy;
import com.roguelike3d.entity.Player;
import com.roguelike3d.item.Item;
import com.roguelike3d.map.DungeonMap;
import com.roguelike3d.map.Tile;
import com.roguelike3d.theme.FloorTheme;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Classic DDA raycasting renderer (Wolfenstein / Lode-style).
 *
 * Coordinate convention:
 *   - Player position (x, y): x is column (east), y is row (south)
 *   - Angle 0 = facing +X; angle π/2 = facing +Y (south)
 *
 * The camera plane is perpendicular to the direction vector and scaled by
 * tan(FOV/2), giving the standard Lode raycasting projection.
 */
public class RaycastRenderer {

    // ---- tuneable constants -----------------------------------------------
    private static final double FOV            = Math.PI / 3.0;  // 60°
    private static final double MAX_DEPTH      = 24.0;           // tiles
    private static final double SHADE_DISTANCE = 12.0;           // full dark at this dist
    private static final double WALL_PATTERN_STRENGTH = 0.08;
    // -----------------------------------------------------------------------

    private final int      w;
    private final int      h;
    private final double[] zBuf;   // per-column depth for sprite occlusion

    public RaycastRenderer(int screenWidth, int screenHeight) {
        this.w    = screenWidth;
        this.h    = screenHeight;
        this.zBuf = new double[screenWidth];
    }

    // =======================================================================
    // Main render entry point
    // =======================================================================

    public void render(Graphics2D g,
                       DungeonMap map, Player player,
                       List<Enemy> enemies, List<Item> items,
                       FloorTheme theme) {

        // 1. Background (ceiling + floor)
        Color ceilCol = theme != null ? theme.ceiling() : new Color(25, 25, 75);
        Color floorCol = theme != null ? theme.floor() : new Color(45, 45, 45);
        g.setColor(ceilCol);
        g.fillRect(0, 0, w, h / 2);
        g.setColor(floorCol);
        g.fillRect(0, h / 2, w, h / 2);

        // Pre-compute direction and camera-plane vectors
        double angle  = player.getAngle();
        double dirX   = Math.cos(angle);
        double dirY   = Math.sin(angle);
        double halfFov = Math.tan(FOV / 2.0);
        double planeX = -dirY * halfFov;
        double planeY =  dirX * halfFov;

        // 2. Walls – one DDA ray per screen column
        for (int col = 0; col < w; col++) {
            castWallRay(g, map, player, dirX, dirY, planeX, planeY, col, theme);
        }

        // 3. Sprites (enemies then items, far-to-near)
        renderSprites(g, map, player, dirX, dirY, planeX, planeY, enemies, items, theme);
    }

    // =======================================================================
    // Wall raycasting
    // =======================================================================

    private void castWallRay(Graphics2D g, DungeonMap map, Player player,
                              double dirX, double dirY,
                              double planeX, double planeY,
                              int col, FloorTheme theme) {
        double camX    = 2.0 * col / w - 1.0;   // −1 … +1
        double rayDirX = dirX + planeX * camX;
        double rayDirY = dirY + planeY * camX;

        int mapX = (int) player.getX();
        int mapY = (int) player.getY();

        // Avoid division-by-zero
        double deltaDistX = (rayDirX == 0) ? Double.MAX_VALUE : Math.abs(1.0 / rayDirX);
        double deltaDistY = (rayDirY == 0) ? Double.MAX_VALUE : Math.abs(1.0 / rayDirY);

        int    stepX;
        double sideDistX;
        if (rayDirX < 0) {
            stepX     = -1;
            sideDistX = (player.getX() - mapX) * deltaDistX;
        } else {
            stepX     =  1;
            sideDistX = (mapX + 1.0 - player.getX()) * deltaDistX;
        }

        int    stepY;
        double sideDistY;
        if (rayDirY < 0) {
            stepY     = -1;
            sideDistY = (player.getY() - mapY) * deltaDistY;
        } else {
            stepY     =  1;
            sideDistY = (mapY + 1.0 - player.getY()) * deltaDistY;
        }

        // DDA march
        Tile hitTile = Tile.WALL;
        int  side    = 0;
        int  steps   = 0;
        while (steps++ < (int) MAX_DEPTH * 2) {
            if (sideDistX < sideDistY) {
                sideDistX += deltaDistX;
                mapX      += stepX;
                side       = 0;
            } else {
                sideDistY += deltaDistY;
                mapY      += stepY;
                side       = 1;
            }
            hitTile = map.getTile(mapX, mapY);
            if (hitTile.isOpaque()) break;
        }

        double perpDist = (side == 0)
                ? sideDistX - deltaDistX
                : sideDistY - deltaDistY;
        perpDist = Math.max(0.001, perpDist);

        zBuf[col] = perpDist;

        int lineH  = (int) (h / perpDist);
        int top    = Math.max(0,     h / 2 - lineH / 2);
        int bottom = Math.min(h - 1, h / 2 + lineH / 2);

        // Choose base colour, darken NS faces and by distance
        Color base = theme != null ? theme.wallBase() : new Color(130, 95, 65);
        if (side == 1) base = base.darker();
        double pattern = ((mapX + mapY) & 1) == 0 ? 1.0 : 1.0 - WALL_PATTERN_STRENGTH;
        double brightness = Math.max(0.15, 1.0 - perpDist / SHADE_DISTANCE) * pattern;
        Color wall = shade(base, brightness);

        g.setColor(wall);
        g.drawLine(col, top, col, bottom);
    }

    // =======================================================================
    // Sprite rendering
    // =======================================================================

    private record Sprite(double deltaX, double deltaY, double dist, Color color, boolean small) {}

    private void renderSprites(Graphics2D g, DungeonMap map, Player player,
                                 double dirX,  double dirY,
                                 double planeX, double planeY,
                                 List<Enemy> enemies, List<Item> items,
                                 FloorTheme theme) {

        List<Sprite> sprites = new ArrayList<>();

        for (Enemy e : enemies) {
            if (!e.isAlive()) continue;
            double dx = e.getX() - player.getX();
            double dy = e.getY() - player.getY();
            sprites.add(new Sprite(dx, dy,
                    Math.sqrt(dx * dx + dy * dy),
                    applyTint(e.getType().color, theme != null ? theme.enemyTint() : e.getType().color, 0.35),
                    false));
        }
        for (Item it : items) {
            if (it.isPicked()) continue;
            double dx = it.getX() - player.getX();
            double dy = it.getY() - player.getY();
            sprites.add(new Sprite(dx, dy,
                    Math.sqrt(dx * dx + dy * dy),
                    applyTint(it.getType().color, theme != null ? theme.itemTint() : it.getType().color, 0.35),
                    true));
        }
        int[] stairs = findStairs(map);
        if (stairs != null) {
            double sx = stairs[0] + 0.5 - player.getX();
            double sy = stairs[1] + 0.5 - player.getY();
            sprites.add(new Sprite(sx, sy,
                    Math.sqrt(sx * sx + sy * sy),
                    theme != null ? theme.stairs() : new Color(220, 190, 70),
                    false));
        }

        // Sort far → near (painter's order for z-buffer test per column)
        sprites.sort(Comparator.comparingDouble(s -> -s.dist()));

        for (Sprite sp : sprites) {
            drawSprite(g, dirX, dirY, planeX, planeY, sp);
        }
    }

    private int[] findStairs(DungeonMap map) {
        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                if (map.getTile(x, y) == Tile.STAIRS_DOWN) return new int[] { x, y };
            }
        }
        return null;
    }

    private void drawSprite(Graphics2D g,
                             double dirX,  double dirY,
                             double planeX, double planeY,
                             Sprite sp) {
        if (sp.dist() < 0.1) return;

        // Transform sprite into camera space
        double invDet    = 1.0 / (planeX * dirY - dirX * planeY);
        double transformX = invDet * ( dirY * sp.deltaX() - dirX  * sp.deltaY());
        double transformY = invDet * (-planeY * sp.deltaX() + planeX * sp.deltaY());

        if (transformY <= 0.1) return;   // behind or too close

        int screenX = (int) ((w / 2.0) * (1 + transformX / transformY));

        int spriteH = (int) Math.abs(h / transformY);
        int spriteW = spriteH;
        if (sp.small()) { spriteH /= 2; spriteW /= 2; }

        int drawTop  = h / 2 - spriteH / 2;
        int drawLeft = screenX - spriteW / 2;

        double brightness = Math.max(0.15, 1.0 - sp.dist() / SHADE_DISTANCE);
        Color  col        = shade(sp.color(), brightness);

        for (int sx = Math.max(0, drawLeft);
             sx < Math.min(w, drawLeft + spriteW); sx++) {

            if (zBuf[sx] > transformY) {          // visible through walls?
                int top    = Math.max(0,     drawTop);
                int bottom = Math.min(h - 1, drawTop + spriteH);
                g.setColor(col);
                g.drawLine(sx, top, sx, bottom);
            }
        }
    }

    // =======================================================================
    // Utility
    // =======================================================================

    private static Color shade(Color c, double brightness) {
        return new Color(
                (int) Math.min(255, c.getRed()   * brightness),
                (int) Math.min(255, c.getGreen() * brightness),
                (int) Math.min(255, c.getBlue()  * brightness));
    }

    private static Color applyTint(Color base, Color tint, double amount) {
        double inv = 1.0 - amount;
        return new Color(
                (int) Math.min(255, base.getRed() * inv + tint.getRed() * amount),
                (int) Math.min(255, base.getGreen() * inv + tint.getGreen() * amount),
                (int) Math.min(255, base.getBlue() * inv + tint.getBlue() * amount));
    }
}
