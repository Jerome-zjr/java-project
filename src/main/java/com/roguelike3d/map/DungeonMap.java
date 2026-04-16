package com.roguelike3d.map;

/**
 * Stores the grid of tiles for one dungeon floor.
 * X is the column (east), Y is the row (south).
 */
public class DungeonMap {

    private final int width;
    private final int height;
    private final Tile[][] tiles;

    public DungeonMap(int width, int height) {
        this.width  = width;
        this.height = height;
        this.tiles  = new Tile[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = Tile.WALL;
            }
        }
    }

    public Tile getTile(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) return Tile.WALL;
        return tiles[x][y];
    }

    public void setTile(int x, int y, Tile tile) {
        if (x >= 0 && x < width && y >= 0 && y < height) tiles[x][y] = tile;
    }

    public boolean isWalkable(int x, int y) {
        return getTile(x, y).isWalkable();
    }

    public int getWidth()  { return width;  }
    public int getHeight() { return height; }
}
