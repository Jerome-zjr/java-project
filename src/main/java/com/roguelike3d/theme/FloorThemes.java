package com.roguelike3d.theme;

import java.awt.Color;

public final class FloorThemes {

    public static final int MAX_FLOOR = 18;

    private static final FloorTheme[] THEMES = {
            new FloorTheme("Dusty Crypt",
                    new Color(130, 95, 65), new Color(45, 45, 45), new Color(25, 25, 75),
                    new Color(220, 190, 70), new Color(200, 200, 200), new Color(220, 140, 120),
                    new Color(140, 140, 140), 60, 14, 1, 3),
            new FloorTheme("Moss Grotto",
                    new Color(70, 90, 60), new Color(35, 50, 35), new Color(20, 35, 25),
                    new Color(160, 190, 90), new Color(80, 140, 80), new Color(120, 180, 120),
                    new Color(90, 140, 90), 70, 16, 1, 3),
            new FloorTheme("Ember Forge",
                    new Color(120, 60, 45), new Color(60, 30, 25), new Color(40, 15, 15),
                    new Color(240, 160, 60), new Color(200, 80, 60), new Color(240, 140, 80),
                    new Color(255, 120, 70), 90, 26, 1, 4),
            new FloorTheme("Frost Vault",
                    new Color(90, 110, 140), new Color(45, 55, 70), new Color(20, 30, 60),
                    new Color(190, 210, 240), new Color(140, 180, 220), new Color(170, 210, 240),
                    new Color(180, 220, 255), 80, 18, 1, 3),
            new FloorTheme("Abyssal Depths",
                    new Color(60, 50, 90), new Color(25, 20, 40), new Color(15, 15, 30),
                    new Color(170, 120, 220), new Color(120, 80, 200), new Color(150, 110, 220),
                    new Color(120, 100, 200), 70, 15, 1, 3),
            new FloorTheme("Sunbleached Ruins",
                    new Color(150, 130, 90), new Color(80, 70, 45), new Color(70, 60, 40),
                    new Color(240, 210, 140), new Color(190, 150, 110), new Color(210, 170, 120),
                    new Color(220, 190, 130), 85, 20, 1, 3),
            new FloorTheme("Shattered Marble",
                    new Color(120, 120, 130), new Color(55, 55, 60), new Color(35, 35, 40),
                    new Color(210, 210, 220), new Color(160, 160, 170), new Color(200, 190, 180),
                    new Color(180, 180, 190), 60, 12, 1, 2),
            new FloorTheme("Blood Cathedral",
                    new Color(120, 40, 40), new Color(50, 20, 20), new Color(30, 10, 10),
                    new Color(200, 80, 80), new Color(180, 60, 60), new Color(220, 100, 90),
                    new Color(200, 70, 70), 90, 22, 1, 3),
            new FloorTheme("Arcane Lab",
                    new Color(60, 90, 110), new Color(30, 45, 55), new Color(20, 30, 45),
                    new Color(120, 200, 220), new Color(90, 160, 180), new Color(160, 120, 220),
                    new Color(140, 200, 220), 75, 17, 1, 3),
            new FloorTheme("Toxic Sewers",
                    new Color(90, 90, 50), new Color(45, 45, 25), new Color(25, 30, 20),
                    new Color(160, 180, 80), new Color(120, 160, 60), new Color(170, 200, 90),
                    new Color(140, 190, 80), 95, 24, 1, 3),
            new FloorTheme("Crystal Cavern",
                    new Color(70, 120, 130), new Color(35, 70, 80), new Color(20, 45, 55),
                    new Color(170, 230, 240), new Color(120, 190, 200), new Color(190, 240, 250),
                    new Color(160, 230, 240), 80, 18, 1, 4),
            new FloorTheme("Storm Keep",
                    new Color(80, 80, 100), new Color(40, 40, 55), new Color(20, 20, 35),
                    new Color(150, 150, 180), new Color(120, 120, 150), new Color(180, 180, 200),
                    new Color(130, 130, 160), 70, 15, 1, 3),
            new FloorTheme("Sunken Temple",
                    new Color(50, 90, 90), new Color(25, 45, 45), new Color(15, 30, 35),
                    new Color(120, 180, 180), new Color(80, 150, 150), new Color(140, 200, 200),
                    new Color(110, 170, 170), 80, 16, 1, 3),
            new FloorTheme("Obsidian Rift",
                    new Color(70, 60, 55), new Color(30, 25, 20), new Color(15, 15, 15),
                    new Color(210, 140, 60), new Color(150, 90, 60), new Color(220, 160, 80),
                    new Color(210, 140, 70), 90, 23, 1, 3),
            new FloorTheme("Verdant Hollow",
                    new Color(60, 100, 70), new Color(30, 55, 35), new Color(20, 40, 25),
                    new Color(150, 220, 120), new Color(90, 170, 110), new Color(170, 230, 140),
                    new Color(120, 200, 130), 80, 18, 1, 3),
            new FloorTheme("Umbral Library",
                    new Color(80, 60, 100), new Color(35, 25, 50), new Color(20, 15, 35),
                    new Color(180, 140, 220), new Color(140, 90, 200), new Color(200, 160, 230),
                    new Color(170, 130, 220), 70, 15, 1, 3),
            new FloorTheme("Iron Citadel",
                    new Color(90, 95, 100), new Color(45, 50, 55), new Color(25, 30, 35),
                    new Color(180, 190, 200), new Color(130, 140, 150), new Color(200, 210, 220),
                    new Color(150, 160, 170), 75, 16, 1, 3),
            new FloorTheme("Celestial Gate",
                    new Color(110, 100, 120), new Color(55, 50, 70), new Color(20, 20, 50),
                    new Color(255, 220, 130), new Color(170, 150, 220), new Color(240, 210, 150),
                    new Color(230, 210, 150), 110, 22, 1, 4)
    };

    private FloorThemes() {
    }

    public static FloorTheme forFloor(int floor) {
        int clamped = Math.max(1, Math.min(MAX_FLOOR, floor));
        return THEMES[clamped - 1];
    }
}
