package com.roguelike3d.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void nextFloorIncrementsFloorAndAddsScore() {
        Player player = new Player(0, 0);
        assertEquals(1, player.getFloor());
        assertEquals(0, player.getScore());

        player.nextFloor();
        assertEquals(2, player.getFloor());
        assertEquals(100, player.getScore());

        player.addScore(50);
        assertEquals(150, player.getScore());
    }

    @Test
    void inventoryIsMutableListOwnedByPlayer() {
        Player player = new Player(0, 0);
        assertTrue(player.getInventory().isEmpty());

        player.addInventoryEntry("Test Item");
        assertEquals(1, player.getInventory().size());
        assertEquals("Test Item", player.getInventory().get(0));
    }
}

