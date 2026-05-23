package com.roguelike3d.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EntityTest {

    private static final class TestEntity extends Entity {
        TestEntity(double x, double y, int maxHp, int attack, int defense) {
            super(x, y, maxHp, attack, defense);
        }
    }

    @Test
    void takeDamageReducesHpAndCanKill() {
        TestEntity e = new TestEntity(0, 0, 10, 0, 3);

        e.takeDamage(1); // min 1 damage
        assertEquals(9, e.getHp());
        assertTrue(e.isAlive());

        e.takeDamage(3); // 3 - def(3) => min 1
        assertEquals(8, e.getHp());

        e.takeDamage(10); // 10 - 3 = 7
        assertEquals(1, e.getHp());

        e.takeDamage(10);
        assertEquals(0, e.getHp());
        assertFalse(e.isAlive());
    }

    @Test
    void healCapsAtMaxHpAndDoesNotRevive() {
        TestEntity e = new TestEntity(0, 0, 10, 0, 0);
        e.takeDamage(100);
        assertEquals(0, e.getHp());
        assertFalse(e.isAlive());

        e.heal(100);
        assertEquals(10, e.getHp());
        assertFalse(e.isAlive());
    }

    @Test
    void statModifiersNeverGoNegative() {
        TestEntity e = new TestEntity(0, 0, 10, 1, 1);

        e.addAttack(-10);
        e.addDefense(-10);

        assertEquals(0, e.getAttack());
        assertEquals(0, e.getDefense());
    }
}

