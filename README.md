# 3D Roguelike Dungeon

[![Java Version](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](#license)
[![No Dependencies](https://img.shields.io/badge/Dependencies-None-brightgreen.svg)]()

**[English](README.md) | [简体中文](README.zh-CN.md) | [繁體中文](README.zh-TW.md)**

A pure-Java, first-person 3D roguelike dungeon crawler built with **Java 17** and **Swing/AWT** — no external dependencies. Features raycasting 3D rendering, procedural dungeon generation, and classic roguelike mechanics in a standalone, cross-platform experience.

## Table of Contents

- [Screenshots](#screenshots)
- [Key Innovations](#key-innovations)
- [Features](#features)
- [Game Mechanics](#game-mechanics)
- [Technology Stack](#technology-stack)
- [Architecture & Design Patterns](#architecture--design-patterns)
- [Controls](#controls)
- [Building & Running](#building--running)
- [Project Structure](#project-structure)
- [Extending the Game](#extending-the-game)
- [Development Guidelines](#development-guidelines)
- [Troubleshooting](#troubleshooting)
- [Roadmap](#roadmap)
- [License](#license)
- [Contributors](#contributors)

## 项目介绍

这是一个基于 Java 17 + Swing/AWT 开发的第一人称 3D 地牢 Roguelike 游戏。项目通过经典 Raycasting（DDA）实现 3D 视角渲染，结合随机地图生成、回合式近战与楼层推进机制，构建出轻量但完整的地牢探索体验。  
目前包含主菜单、设置页面（鼠标灵敏度可调并持久化）、战斗与掉落系统、HUD 与小地图、以及 Game Over 流程。

## Screenshots

| Menu | In-Game (3D view) |
|------|-------------------|
| ![menu](https://github.com/user-attachments/assets/e3247e4b-844e-48b6-a09f-f2cd708d50f7) | ![ingame](https://github.com/user-attachments/assets/e7a35325-dbee-4685-a7d2-cae88a728a89) |

## Key Innovations

- **Zero-Dependency 3D Engine**: Pure Java implementation using only Swing/AWT, no external libraries needed
- **DDA Raycasting Algorithm**: Efficient distance-based rendering inspired by Wolfenstein 3D
- **Procedural Generation**: Intelligent room-corridor placement with guaranteed room connectivity
- **Scalable Enemy System**: Extensible architecture supporting multiple enemy types with distinct behaviors
- **Cross-Platform Compatibility**: Runs anywhere Java 17+ is available without compilation workarounds

## Features

### 3D Rendering
- **3D raycasting renderer** — classic DDA algorithm (Wolfenstein / Lode-style)
  - Distance-based wall shading with color gradients
  - Billboard sprite projection for enemies and items
  - Separate Z-buffer for proper sprite occlusion
  - Optimized ray-tracing with early termination

### World Generation & Progression
- **Procedural dungeon generation** — intelligent room placement with guaranteed L-shaped corridor connectivity
- **18 themed floors** — unique color palettes and particle effects per floor for visual progression
- **Persistent progression** — floor counters and score tracking across encounters

### Gameplay Systems
- **Roguelike mechanics** — permadeath with score-based persistence, floor progression, defeat consequences
- **Three enemy types** — Skeleton, Zombie, Demon (each with unique stats: HP, attack, defense, speed)
- **Item system** — Health Potion, Strength Potion, Armor Shard with automatic collection on proximity
- **Combat mechanics** — ±20% damage variance, melee attack ranges, enemy AI with chase and retreat logic

### User Interface
- **HUD system** — HP bar with color coding, floor/score display, combat message log with scrolling
- **Minimap** — real-time dungeon overlay with player position and direction indicator
- **Crosshair** — centered reticle for aiming and targeting
- **Menus** — professional title screen with controls overlay, Game Over with final score

## Game Mechanics

### Combat System
- **Damage Formula**: `final_damage = base_damage * attacker_defense_bonus * variance(±20%)`
- **Enemy AI**: Enemies detect player within render distance, chase on line-of-sight, retreat when low health
- **Player Attack**: Melee swing with directional component based on player angle
- **Defense**: Armor shards stack to reduce incoming damage

### Progression
- **Difficulty Scaling**: Each floor increases enemy stats and spawn rate
- **Item Distribution**: Items appear randomly on floors with higher spawn rates on later floors
- **Score Calculation**: Points awarded for enemy kills and floor progression

### Map Generation Algorithm
1. Place seed rooms at random positions with minimum distance spacing
2. Connect rooms with L-shaped corridors
3. Place staircase in final room
4. Validate connectivity with graph traversal

## Technology Stack

- **Language**: Java 17 (modern language features: records, sealed classes, pattern matching)
- **Graphics**: Swing (JPanel, Graphics2D) + AWT (MouseListener, KeyListener)
- **Build System**: Maven 3.x (with fallback plain `javac` support)
- **Rendering**: Pure algorithm implementation (no external graphics libraries)
- **Dependencies**: **ZERO** — all functionality built from scratch
- **Target Runtime**: JVM 17+

This zero-dependency approach demonstrates software engineering excellence and deep understanding of low-level rendering concepts, making it an ideal educational resource and portfolio piece.

## Architecture & Design Patterns

### Design Patterns Used

1. **State Machine Pattern** (`GameState` enum)
   - Manages game modes: MENU → PLAYING → GAME_OVER
   - State transitions handled in `Game.update()`

2. **Entity-Component Pattern**
   - Base `Entity` class with common properties (HP, position, angle)
   - Specialized subclasses: `Player`, `Enemy`
   - Type-specific behavior via `EnemyType` enum

3. **Strategy Pattern** (`EnemyType`)
   - Different AI behaviors encapsulated per enemy type
   - Stats configuration decoupled from behavior

4. **Factory Pattern** (`MapGenerator`)
   - Procedural generation of game world
   - Encapsulates complex room/corridor logic

5. **Observer-like Input Handling**
   - `InputHandler` maintains key state without tight coupling
   - `Game` polls input state each frame

### Layered Architecture

- **Presentation Layer**: `Game` (JPanel), `Menu`, `HUD`, `RaycastRenderer`
- **Game Logic Layer**: `Entity`, `Player`, `Enemy`, `CombatSystem`
- **Data Layer**: `DungeonMap`, `Tile`, `Item`, `ItemType`
- **Utilities**: `ColorPalette`, `MathUtils`

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
- **Java 17+** (download from [oracle.com](https://www.oracle.com/java/) or use OpenJDK)
- **Maven 3.x** (optional — can also use plain `javac`)

### Quick Start with Maven

```bash
# Clone and navigate to project
git clone https://github.com/Jerome-zjr/java-project.git
cd java-project

# Build
mvn clean package -DskipTests

# Run
java -jar target/roguelike3d.jar
```

### Building with Plain `javac`

```bash
mkdir -p build/classes
find src/main/java -name "*.java" > sources.txt
javac -d build/classes @sources.txt
jar cfe build/roguelike3d.jar com.roguelike3d.Main -C build/classes .
java -jar build/roguelike3d.jar
```

### Cross-Platform Notes
- **Windows**: Ensure Java is in PATH; use forward slashes in commands
- **macOS**: May require XQuartz for full Swing support
- **Linux**: Requires X11; test with `xhost +local:` if issues occur

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

### Adding a New Enemy Type

**File**: `src/main/java/com/roguelike3d/entity/EnemyType.java`

Add a new constant with stats:
```java
PHANTOM(3, 8, 1, 2.0f, 0xAA00AA) // name, hp, attack, defense, speed, color
```

The stats are interpreted as:
- `hp`: Health points (base value)
- `attack`: Attack damage per swing
- `defense`: Defense multiplier (damage reduction)
- `speed`: Movement speed multiplier relative to base
- `color`: RGB hex color for rendering

**File**: `src/main/java/com/roguelike3d/entity/Enemy.java`

Update the constructor to handle new type stats. Behavior is already generalized via the state machine in `Enemy.update()`.

### Adding a New Item Type

**File**: `src/main/java/com/roguelike3d/item/ItemType.java`

Add a new constant:
```java
MANA_SHARD(0x00FFFF, 15) // color, effect_value
```

**File**: `src/main/java/com/roguelike3d/item/Item.java`

Update the `collect()` method with a new case:
```java
case MANA_SHARD -> {
    // Apply mana restoration or custom effect
    player.setMana(Math.min(100, player.getMana() + 30));
}
```

### Adjusting Game Difficulty

**File**: `src/main/java/com/roguelike3d/map/MapGenerator.java`

Adjust difficulty constants:
```java
private static final int ROOM_COUNT = 8;        // More rooms = longer game
private static final int MIN_ROOM_SIZE = 5;     // Larger rooms = easier navigation
private static final int FLOOR_COUNT = 18;      // Total floors to reach
```

**File**: `src/main/java/com/roguelike3d/entity/Enemy.java`

Adjust per-floor scaling:
```java
float floorMultiplier = 1.0f + (floor * 0.1f);  // 10% increase per floor
```

### Adding Wall Textures

**File**: `src/main/java/com/roguelike3d/renderer/RaycastRenderer.java`

Replace the current `shade()` method (which uses simple color gradients) with texture mapping:

1. Load texture atlas as `BufferedImage` in constructor
2. In ray-hit calculation, determine texture coordinates (u, v)
3. Sample texture pixel and apply distance-based darkening
4. Render textured pixel instead of flat color

**Pseudocode**:
```java
int textureColor = textureAtlas.getRGB(u * textureWidth, v * textureHeight);
int shadedColor = applyDistanceDarkening(textureColor, distance);
pixel.setColor(shadedColor);
```

### Adding Sound Effects

**File**: `src/main/java/com/roguelike3d/combat/CombatSystem.java`

Use Java's `javax.sound.sampled` API:
```java
import javax.sound.sampled.*;

public void playSound(String soundFile) throws Exception {
    AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(soundFile));
    Clip clip = AudioSystem.getClip();
    clip.open(audioStream);
    clip.start();
}
```

Call in `attack()` method:
```java
playSound("sounds/slash.wav");
```

### Persistent High Score Tracking

**File**: `src/main/java/com/roguelike3d/Game.java`

In `renderGameOver()`, add file I/O:
```java
PrintWriter writer = new PrintWriter("highscores.txt", "UTF-8");
writer.println("Player: " + player.getName() + " | Score: " + player.getScore() + 
               " | Floor: " + player.getFloor());
writer.close();
```

Load and display on title screen from `Menu.render()`.

### Custom Dungeon Themes

Add configurable palettes in `ColorPalette.java`:
```java
public static final int[] LAVA_THEME = {0xFF4400, 0xFF6600, 0xFF8800, ...};
public static final int[] ICE_THEME = {0x00AAFF, 0x00CCFF, 0x00EEFF, ...};
```

Pass theme to `RaycastRenderer` based on current floor.

## Development Guidelines

### Code Style
- Use clear, descriptive variable names (`rayDistance` not `rd`)
- Keep methods focused (single responsibility)
- Use constants for magic numbers (see `MapGenerator.ROOM_COUNT`)
- Document complex algorithms (especially raycasting logic)

### Performance Considerations
- **Frame Rate**: Targets 60 FPS; profile with `System.nanoTime()`
- **Ray Casting**: ~240 rays per frame (~60 per millisecond); uses early termination
- **Memory**: ~50MB typical; avoids object allocation in game loop
- **Optimization Tips**:
  - Cache color calculations in `ColorPalette`
  - Use integer arithmetic instead of floats where possible
  - Profile with JProfiler or built-in `-prof` flags

### Testing Approach
1. **Manual Testing**: Play through multiple runs, varying floor count
2. **Edge Cases**: Test with map boundaries, collision corner cases
3. **Performance**: Monitor frame rate at 1920x1080 resolution

### Adding Tests

Create `src/test/java/` and add unit tests:
```java
public class MapGeneratorTest {
    @Test
    public void testMapConnectivity() {
        DungeonMap map = MapGenerator.generate(10);
        assertTrue(isConnected(map));
    }
}
```

Run with:
```bash
mvn test
```

## Troubleshooting

### Game Won't Start
- **Error**: "Main-Class not found"
  - **Solution**: Ensure `pom.xml` has correct `<mainClass>` entry
  
- **Error**: "NoSuchMethodError"
  - **Solution**: Check Java version is 17+: `java -version`

### Rendering Issues
- **Distorted graphics**: Try resizing window or restarting
- **Black screen on startup**: Check graphics driver; try `java -Dswing.disableBufferPerComponent=true -jar target/roguelike3d.jar`
- **Flickering/tearing**: Increase buffer size or disable vsync if available

### Performance Problems
- **Low FPS**: Reduce screen resolution or increase frame skip interval in `Game.java`
- **Memory leak**: Check that `Item` objects are properly garbage collected after collection

### Build Failures
- **Maven not found**: Ensure Maven is installed: `mvn -version`
- **Source file not found**: Verify all Java files are in `src/main/java/`

## Roadmap

### Version 1.1 (Planned)
- [ ] Configurable difficulty levels (Easy/Normal/Hard)
- [ ] Leaderboard system with persistent high scores
- [ ] Additional enemy types (Ghost, Golem)
- [ ] More item types (Speed boost, temporary shield)
- [ ] Weapon variety (sword, bow, staff)

### Version 1.2 (Planned)
- [ ] Multiplayer support (local co-op with split-screen)
- [ ] Boss encounters on milestone floors
- [ ] Spell/magic system with cooldowns
- [ ] Wall textures and advanced rendering effects
- [ ] Sound effects and ambient music
- [ ] Mod support via plugin architecture

### Version 2.0 (Future Vision)
- [ ] Network multiplayer (dungeon crawling with friends)
- [ ] Advanced AI with pathfinding (A* algorithm)
- [ ] Story campaign with NPC interactions
- [ ] Full 6-DOF movement (looking up/down)
- [ ] Lighting system with dynamic shadows
- [ ] Cross-platform compilation to native binaries (GraalVM Native Image)

## License

This project is distributed under the **MIT License**. See [LICENSE](LICENSE) file for details.

The MIT License allows free use, modification, and distribution, including in commercial projects, with the only requirement being attribution.

## Contributors

- **Jerome-zjr** - Original author and maintainer

## Acknowledgements

- Inspired by classic roguelike games (NetHack, Dungeon Crawl)
- Raycasting algorithm based on Wolfenstein 3D techniques
- Community feedback and issue reports

---

**Have questions or want to contribute?** [Open an issue](https://github.com/Jerome-zjr/java-project/issues) or submit a pull request!
