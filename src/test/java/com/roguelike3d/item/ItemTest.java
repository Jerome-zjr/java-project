package com.roguelike3d.item;

import com.roguelike3d.entity.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    @Test
    void healthPotionHealsAndIsIdempotent() {
        Player player = new Player(0, 0);
        player.takeDamage(80);
        int hpAfterDamage = player.getHp();
        assertTrue(hpAfterDamage < player.getMaxHp());

        Item potion = new Item(ItemType.HEALTH_POTION, 0.5, 0.5);
        potion.collect(player);

        assertTrue(potion.isPicked());
        assertTrue(player.getHp() > hpAfterDamage);
        assertTrue(player.getHp() <= player.getMaxHp());
        assertEquals(1, player.getInventory().size());
        assertEquals(ItemType.HEALTH_POTION.name, player.getInventory().get(0));

        int hpAfterFirstCollect = player.getHp();
        potion.collect(player);
        assertEquals(hpAfterFirstCollect, player.getHp());
        assertEquals(1, player.getInventory().size());
    }

    @Test
    void strengthPotionIncreasesAttack() {
        Player player = new Player(0, 0);
        int atkBefore = player.getAttack();

        Item potion = new Item(ItemType.STRENGTH_POTION, 0.5, 0.5);
        potion.collect(player);

        assertEquals(atkBefore + ItemType.STRENGTH_POTION.attackBonus, player.getAttack());
        assertEquals(ItemType.STRENGTH_POTION.name, player.getInventory().get(0));
    }

    @Test
    void armorShardIncreasesDefense() {
        Player player = new Player(0, 0);
        int defBefore = player.getDefense();

        Item shard = new Item(ItemType.ARMOR_SHARD, 0.5, 0.5);
        shard.collect(player);

        assertEquals(defBefore + 2, player.getDefense());
        assertEquals(ItemType.ARMOR_SHARD.name, player.getInventory().get(0));
    }
}

