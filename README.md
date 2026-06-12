# 3D Roguelike Dungeon

A pure-Java, first-person 3D roguelike dungeon crawler built with **Java 17** and **Swing/AWT** — no external dependencies.

## 项目介绍

这是一个基于 Java 17 + Swing/AWT 开发的第一人称 3D 地牢 Roguelike 游戏。项目通过经典 Raycasting（DDA）实现 3D 视角渲染，结合随机地图生成、回合式近战与楼层推进机制，构建出轻量但完整的地牢探索体验。  
目前包含主菜单、设置页面（鼠标灵敏度可调并持久化）、战斗与掉落系统、HUD 与小地图、以及 Game Over 流程。

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
- **Three enemy types** — Skeleton, Zombie, Demon (each with unique stats and speed)
- **Item pickups** — Health Potion, Strength Potion, Armor Shard
- **HUD** — HP bar, floor/score display, combat message log, minimap with player direction, crosshair
- **Title screen** and **Game Over** screen

## Controls

| Key | Action |
|-----|--------|
| `SPACE` / Mouse Click (Menu) | Start game |
| `S` (Menu) | Open settings |
| `←` / `A` (Settings) | Decrease mouse sensitivity |
| `→` / `D` (Settings) | Increase mouse sensitivity |
| `W` / `↑` | Move forward |
| `S` / `↓` | Move backward |
| `A` / `←` | Rotate left |
| `D` / `→` | Rotate right |
| `Q` | Strafe left |
| `E` | Strafe right |
| `SPACE` | Attack (melee swing) |
| `F` | Descend stairs (when standing on `▼`) |
| `ESC` | Back to menu / quit (depends on current screen) |
| `ENTER` | Return to menu on Game Over |

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