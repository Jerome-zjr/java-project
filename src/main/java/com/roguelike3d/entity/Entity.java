package com.roguelike3d.entity;

/**
 * Base class for every living thing in the dungeon.
 */
public abstract class Entity {

    protected double x;
    protected double y;
    protected int    hp;
    protected int    maxHp;
    protected int    attack;
    protected int    defense;
    protected boolean alive;

    protected Entity(double x, double y, int maxHp, int attack, int defense) {
        this.x       = x;
        this.y       = y;
        this.maxHp   = maxHp;
        this.hp      = maxHp;
        this.attack  = attack;
        this.defense = defense;
        this.alive   = true;
    }

    // ----- Damage / healing -----

    public void takeDamage(int rawDamage) {
        int actual = Math.max(1, rawDamage - defense);
        hp -= actual;
        if (hp <= 0) { hp = 0; alive = false; }
    }

    public void heal(int amount) {
        hp = Math.min(maxHp, hp + amount);
    }

    // ----- Stat modifiers (for item effects) -----

    public void addAttack(int amount)  { attack  = Math.max(0, attack  + amount); }
    public void addDefense(int amount) { defense = Math.max(0, defense + amount); }

    // ----- Getters / setters -----

    public boolean isAlive()  { return alive;   }
    public double  getX()     { return x;       }
    public double  getY()     { return y;       }
    public int     getHp()    { return hp;      }
    public int     getMaxHp() { return maxHp;   }
    public int     getAttack(){ return attack;  }
    public int     getDefense(){ return defense; }

    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
}
