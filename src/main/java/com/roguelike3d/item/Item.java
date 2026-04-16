package com.roguelike3d.item;

import com.roguelike3d.entity.Player;

/**
 * A floor item that can be walked over and automatically collected.
 */
public class Item {

    private final ItemType type;
    private final double   x;
    private final double   y;
    private boolean        picked;

    public Item(ItemType type, double x, double y) {
        this.type   = type;
        this.x      = x;
        this.y      = y;
        this.picked = false;
    }

    /**
     * Apply the item's effect to the player and mark it as collected.
     */
    public void collect(Player player) {
        if (picked) return;
        picked = true;

        switch (type) {
            case HEALTH_POTION    -> player.heal(40);
            case STRENGTH_POTION  -> player.addAttack(5);
            case ARMOR_SHARD      -> player.addDefense(2);
        }
        player.addInventoryEntry(type.name);
    }

    public ItemType getType()   { return type;   }
    public double   getX()      { return x;      }
    public double   getY()      { return y;      }
    public boolean  isPicked()  { return picked; }
}
