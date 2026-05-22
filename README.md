# 3D Roguelike Dungeon

A pure-Java, first-person 3D roguelike dungeon crawler built with **Java 17** and **Swing/AWT** — no external dependencies.

## Screenshots

| Menu | In-Game (3D view) |
|------|-------------------|
| ![menu](https://github.com/user-attachments/assets/e3247e4b-844e-48b6-a09f-f2cd708d50f7) | ![ingame](https://github.com/user-attachments/assets/e7a35325-dbee-4685-a7d2-cae88a728a89) |

## Features

- **3D raycasting renderer** — classic DDA algorithm (Wolfenstein / Lode-style)
  - Distance-based wall shading
  - Billboard sprite projection for enemies and items
  - Separate Z-buffer for sprite occlusion
- **Procedural dungeon generation** — random room placement with L-shaped corridors
- **Roguelike mechanics** — permadeath, floor progression, score tracking
- **18 themed floors** — unique color palettes and particle effects per floor
- **Three enemy types** — Skeleton, Zombie, Demon (each with unique stats and speed)
- **Item pickups** — Health Potion, Strength Potion, Armor Shard
- **HUD** — HP bar, floor/score display, combat message log, minimap with player direction, crosshair
- **Title screen** and **Game Over** screen

## Controls

| Key | Action |
|-----|--------|
| `W` / `↑` | Move forward |
| `S` / `↓` | Move backward |
| `A` / `←` | Rotate left |
| `D` / `→` | Rotate right |
| `Q` | Strafe left |
| `E` | Strafe right |
| `SPACE` | Attack (melee swing) |
| `F` | Descend stairs (when standing on `▼`) |
| `ENTER` | Start game / confirm |
| `ESC` | Quit |

## Building & Running

### Requirements
- Java 17+
- Maven 3.x (optional — can also use plain `javac`)

### With Maven
```bash
mvn clean package -DskipTests
java -jar target/roguelike3d.jar
```

### With plain javac
```bash
mkdir -p build/classes
find src/main/java -name "*.java" > sources.txt
javac -d build/classes @sources.txt
jar cfe build/roguelike3d.jar com.roguelike3d.Main -C build/classes .
java -jar build/roguelike3d.jar
```

## Project Structure

```
src/main/java/com/roguelike3d/
├── Main.java                   Entry point
├── Game.java                   Game loop, state machine, JPanel
├── GameState.java              Enum: MENU / PLAYING / GAME_OVER
├── input/
│   └── InputHandler.java       Keyboard state (held + just-pressed)
├── map/
│   ├── Tile.java               FLOOR / WALL / STAIRS_DOWN
│   ├── DungeonMap.java         Grid storage
│   └── MapGenerator.java       Procedural room+corridor generator
├── entity/
│   ├── Entity.java             Base class (HP, attack, defense)
│   ├── Player.java             Player (angle, floor, score, inventory)
│   ├── Enemy.java              Enemy AI (chase + attack timers)
│   └── EnemyType.java          SKELETON / ZOMBIE / DEMON
├── combat/
│   └── CombatSystem.java       Damage formula with ±20 % variance
├── item/
│   ├── Item.java               Floor pickup, auto-collected on walk-over
│   └── ItemType.java           HEALTH_POTION / STRENGTH_POTION / ARMOR_SHARD
├── renderer/
│   └── RaycastRenderer.java    DDA raycasting walls + sprite billboard
└── ui/
    ├── HUD.java                HP bar, minimap, messages, crosshair
    └── Menu.java               Title screen
```

## Extending the Game

| Goal | Where to change |
|------|----------------|
| Add enemy type | New constant in `EnemyType.java` |
| Add item type | New constant in `ItemType.java` + case in `Item.collect()` |
| Change map size | Constants in `MapGenerator.java` |
| Add wall textures | `RaycastRenderer` — replace `shade()` with `BufferedImage` lookup |
| Add sound | `CombatSystem.attack()` / `Item.collect()` |
| Persistent high-score | `Game.renderGameOver()` — write `player.getScore()` to file |
