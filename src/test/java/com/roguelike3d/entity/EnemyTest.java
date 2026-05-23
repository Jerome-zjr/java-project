package com.roguelike3d.entity;

import com.roguelike3d.map.DungeonMap;
import com.roguelike3d.map.Tile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnemyTest {

    @Test
    void enemyMovesTowardPlayerWhenInChaseRange() {
        DungeonMap map = openFloorMap(20, 20);
        Player player = new Player(10, 10);
        Enemy enemy = new Enemy(1, 10, EnemyType.SKELETON);

        double moveInterval = 1.0 / EnemyType.SKELETON.speed;
        enemy.update(moveInterval, player, map);

        assertEquals(2.0, enemy.getX());
        assertEquals(10.0, enemy.getY());
    }

    @Test
    void enemyFallsBackToOtherAxisWhenBlocked() {
        DungeonMap map = openFloorMap(20, 20);
        map.setTile(2, 10, Tile.WALL); // block x-step

        Player player = new Player(10, 11);
        Enemy enemy = new Enemy(1, 10, EnemyType.SKELETON);

        double moveInterval = 1.0 / EnemyType.SKELETON.speed;
        enemy.update(moveInterval, player, map);

        assertEquals(1.0, enemy.getX(), 0.0);
        assertEquals(11.0, enemy.getY(), 0.0);
    }

    @Test
    void enemyAttacksOnlyAfterAttackTimerAndInRange() {
        DungeonMap map = openFloorMap(20, 20);
        Player player = new Player(10, 10);
        Enemy enemy = new Enemy(9, 10, EnemyType.SKELETON); // dist = 1.0

        assertEquals(0, enemy.update(0.3, player, map));
        assertEquals(EnemyType.SKELETON.attack, enemy.update(0.4, player, map));
        assertEquals(0, enemy.update(0.1, player, map));
    }

    @Test
    void deadEnemyDoesNothing() {
        DungeonMap map = openFloorMap(10, 10);
        Player player = new Player(5, 5);
        Enemy enemy = new Enemy(4, 5, EnemyType.SKELETON);

        enemy.takeDamage(10_000);
        assertFalse(enemy.isAlive());
        assertEquals(0, enemy.update(10, player, map));
    }

    private static DungeonMap openFloorMap(int w, int h) {
        DungeonMap map = new DungeonMap(w, h);
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                map.setTile(x, y, Tile.FLOOR);
            }
        }
        return map;
    }
}

