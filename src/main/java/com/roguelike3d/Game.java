package com.roguelike3d;

import com.roguelike3d.combat.CombatSystem;
import com.roguelike3d.entity.Enemy;
import com.roguelike3d.entity.EnemyType;
import com.roguelike3d.entity.Player;
import com.roguelike3d.input.InputHandler;
import com.roguelike3d.item.Item;
import com.roguelike3d.item.ItemType;
import com.roguelike3d.map.DungeonMap;
import com.roguelike3d.map.MapGenerator;
import com.roguelike3d.map.Tile;
import com.roguelike3d.renderer.RaycastRenderer;
import com.roguelike3d.ui.HUD;
import com.roguelike3d.ui.Menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Core game class – owns the JFrame, drives the game loop, and ties all
 * modules together.
 *
 * Architecture overview
 * ─────────────────────
 *  Game (game-loop + state machine)
 *   ├── InputHandler          keyboard state
 *   ├── MapGenerator          procedural dungeon
 *   ├── RaycastRenderer       3-D wall + sprite rendering
 *   ├── HUD                   overlay (HP, minimap, messages …)
 *   └── Menu                  title screen
 */
public class Game extends JPanel implements Runnable {

    // ── screen ─────────────────────────────────────────────────────────────
    public static final int SW = 800;
    public static final int SH = 600;
    private static final int TARGET_FPS = 60;

    // ── modules ────────────────────────────────────────────────────────────
    private final InputHandler    input;
    private final RaycastRenderer renderer;
    private final HUD             hud;
    private final Menu            menu;
    private final MapGenerator    mapGen;
    private final Random          rng;

    // ── game state ─────────────────────────────────────────────────────────
    private GameState    state;
    private Player       player;
    private DungeonMap   map;
    private List<Enemy>  enemies;
    private List<Item>   items;
    private List<String> messages;

    // cooldown for player swing (seconds)
    private double attackCooldown;
    private boolean attackQueued;
    private static final double ATTACK_CD           = 0.45;
    /** How close the player must be to the tile centre to trigger a wall slide. */
    private static final double COLLISION_MARGIN    = 0.28;
    /** Distance (in tiles) within which an item is auto-collected. */
    private static final double ITEM_PICKUP_RANGE   = 0.80;
    /** Enemy type tier advances every N floors. */
    private static final int    FLOORS_PER_ENEMY_TIER = 3;

    // ── Swing ──────────────────────────────────────────────────────────────
    private JFrame frame;
    private final Object stateLock = new Object();

    // =======================================================================

    public Game() {
        setPreferredSize(new Dimension(SW, SH));
        setBackground(Color.BLACK);
        setFocusable(true);

        input    = new InputHandler();
        renderer = new RaycastRenderer(SW, SH);
        hud      = new HUD(SW, SH);
        menu     = new Menu(SW, SH);
        mapGen   = new MapGenerator();
        rng      = new Random();
        messages = new ArrayList<>();
        state    = GameState.MENU;

        addKeyListener(input);
    }

    public void start() {
        frame = new JFrame("3D Roguelike Dungeon");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);

        new Thread(this, "game-loop").start();
    }

    // =======================================================================
    // Game loop
    // =======================================================================

    @Override
    public void run() {
        long prev = System.nanoTime();
        final double NS_PER_FRAME = 1_000_000_000.0 / TARGET_FPS;

        while (!Thread.currentThread().isInterrupted()) {
            long now   = System.nanoTime();
            double dt  = (now - prev) / 1_000_000_000.0;
            prev       = now;

            // Cap delta so a freeze doesn't produce a huge jump
            dt = Math.min(dt, 0.1);

            update(dt);
            repaint();

            // Pace to target FPS
            long elapsed   = System.nanoTime() - now;
            long sleepMs   = ((long) NS_PER_FRAME - elapsed) / 1_000_000;
            if (sleepMs > 1) {
                try { Thread.sleep(sleepMs); }
                catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            input.endFrame();
        }
    }

    // =======================================================================
    // Update
    // =======================================================================

    private void update(double dt) {
        synchronized (stateLock) {
            switch (state) {
                case MENU     -> updateMenu();
                case PLAYING  -> updatePlaying(dt);
                case GAME_OVER -> updateGameOver();
            }
        }
    }

    private void updateMenu() {
        if (input.wasJustPressed(KeyEvent.VK_ENTER)) newGame();
        if (input.wasJustPressed(KeyEvent.VK_ESCAPE)) System.exit(0);
    }

    private void updateGameOver() {
        if (input.wasJustPressed(KeyEvent.VK_ENTER)) state = GameState.MENU;
    }

    private void updatePlaying(double dt) {
        if (!player.isAlive()) { state = GameState.GAME_OVER; return; }

        handleMovement(dt);
        handleAttack(dt);
        checkItemPickup();
        checkStairs();
        updateEnemies(dt);
        trimMessages();
    }

    // ── movement ────────────────────────────────────────────────────────────

    private void handleMovement(double dt) {
        final double MOVE_SPEED = 3.5;
        final double ROT_SPEED  = 2.2;
        final double MARGIN     = COLLISION_MARGIN;

        double angle = player.getAngle();
        double nx    = player.getX();
        double ny    = player.getY();

        if (input.isHeld(KeyEvent.VK_W) || input.isHeld(KeyEvent.VK_UP)) {
            nx += Math.cos(angle) * MOVE_SPEED * dt;
            ny += Math.sin(angle) * MOVE_SPEED * dt;
        }
        if (input.isHeld(KeyEvent.VK_S) || input.isHeld(KeyEvent.VK_DOWN)) {
            nx -= Math.cos(angle) * MOVE_SPEED * dt;
            ny -= Math.sin(angle) * MOVE_SPEED * dt;
        }
        if (input.isHeld(KeyEvent.VK_Q)) {   // strafe left
            nx += Math.cos(angle - Math.PI / 2) * MOVE_SPEED * dt;
            ny += Math.sin(angle - Math.PI / 2) * MOVE_SPEED * dt;
        }
        if (input.isHeld(KeyEvent.VK_E)) {   // strafe right
            nx += Math.cos(angle + Math.PI / 2) * MOVE_SPEED * dt;
            ny += Math.sin(angle + Math.PI / 2) * MOVE_SPEED * dt;
        }
        if (input.isHeld(KeyEvent.VK_A) || input.isHeld(KeyEvent.VK_LEFT)) {
            player.setAngle(angle - ROT_SPEED * dt);
        }
        if (input.isHeld(KeyEvent.VK_D) || input.isHeld(KeyEvent.VK_RIGHT)) {
            player.setAngle(angle + ROT_SPEED * dt);
        }

        // Slide along walls: test each axis independently
        if (canMoveX(nx, player.getY(), MARGIN)) player.setX(nx);
        if (canMoveY(player.getX(), ny, MARGIN)) player.setY(ny);
    }

    private boolean canMoveX(double nx, double y, double m) {
        return map.isWalkable((int)(nx - m), (int)(y - m))
            && map.isWalkable((int)(nx + m), (int)(y - m))
            && map.isWalkable((int)(nx - m), (int)(y + m))
            && map.isWalkable((int)(nx + m), (int)(y + m));
    }

    private boolean canMoveY(double x, double ny, double m) {
        return map.isWalkable((int)(x - m), (int)(ny - m))
            && map.isWalkable((int)(x + m), (int)(ny - m))
            && map.isWalkable((int)(x - m), (int)(ny + m))
            && map.isWalkable((int)(x + m), (int)(ny + m));
    }

    // ── combat ──────────────────────────────────────────────────────────────

    private void handleAttack(double dt) {
        if (input.wasJustPressed(KeyEvent.VK_SPACE)) attackQueued = true;
        if (attackCooldown > 0) {
            attackCooldown = Math.max(0, attackCooldown - dt);
            return;
        }
        if (!attackQueued) return;

        attackQueued = false;
        attackCooldown = ATTACK_CD;
        boolean hit = false;
        for (Enemy e : enemies) {
            if (!e.isAlive()) continue;
            double dist = dist(e.getX(), e.getY(), player.getX(), player.getY());
            if (dist < 1.6) {
                int dmg = CombatSystem.attack(player, e);
                addMsg("You hit " + e.getType().name + " for " + dmg + " dmg!");
                hit = true;
                if (!e.isAlive()) {
                    addMsg(e.getType().name + " defeated!  +" + e.getType().scoreValue + " pts");
                    player.addScore(e.getType().scoreValue);
                }
            }
        }
        if (!hit) addMsg("No enemy in reach! (SPACE to swing)");
    }

    // ── items ───────────────────────────────────────────────────────────────

    private void checkItemPickup() {
        for (Item it : items) {
            if (it.isPicked()) continue;
            if (dist(it.getX(), it.getY(), player.getX(), player.getY()) < ITEM_PICKUP_RANGE) {
                it.collect(player);
                addMsg("Picked up " + it.getType().name + "!");
            }
        }
        items.removeIf(Item::isPicked);
    }

    // ── stairs ──────────────────────────────────────────────────────────────

    private void checkStairs() {
        if (map.getTile((int) player.getX(), (int) player.getY()) == Tile.STAIRS_DOWN) {
            if (input.wasJustPressed(KeyEvent.VK_F)) {
                player.nextFloor();
                loadFloor();
                addMsg("Floor " + player.getFloor() + " – deeper into the dark…");
            }
        }
    }

    // ── enemies ─────────────────────────────────────────────────────────────

    private void updateEnemies(double dt) {
        for (Enemy e : enemies) {
            int dmg = e.update(dt, player, map);
            if (dmg > 0 && player.isAlive()) {
                player.takeDamage(dmg);
                addMsg(e.getType().name + " attacks you for " + dmg + " dmg!");
            }
        }
    }

    // ── messages ────────────────────────────────────────────────────────────

    private void addMsg(String msg) {
        messages.add(msg);
    }

    private void trimMessages() {
        while (messages.size() > 6) messages.remove(0);
    }

    // =======================================================================
    // Level loading
    // =======================================================================

    private void newGame() {
        player = new Player(0, 0);
        enemies = new ArrayList<>();
        items = new ArrayList<>();
        messages.clear();
        attackQueued = false;
        loadFloor();
        state = GameState.PLAYING;
        addMsg("Welcome to the dungeon! Find the golden stairs ▼");
    }

    private void loadFloor() {
        MapGenerator.GenerationResult result = mapGen.generate();
        map = result.map();

        player.setX(result.playerStart()[0] + 0.5);
        player.setY(result.playerStart()[1] + 0.5);

        enemies.clear();
        EnemyType[] types = EnemyType.values();
        for (int[] sp : result.enemySpawns()) {
            // Scale enemy type probabilities by floor
            EnemyType type = types[Math.min(rng.nextInt(types.length) + (player.getFloor() - 1) / FLOORS_PER_ENEMY_TIER,
                                            types.length - 1)];
            enemies.add(new Enemy(sp[0] + 0.5, sp[1] + 0.5, type));
        }

        items.clear();
        ItemType[] itypes = ItemType.values();
        for (int[] sp : result.itemSpawns()) {
            items.add(new Item(itypes[rng.nextInt(itypes.length)], sp[0] + 0.5, sp[1] + 0.5));
        }
    }

    // =======================================================================
    // Rendering
    // =======================================================================

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        synchronized (stateLock) {
            switch (state) {
                case MENU     -> menu.render(g2);
                case PLAYING  -> renderPlaying(g2);
                case GAME_OVER -> renderGameOver(g2);
            }
        }
    }

    private void renderPlaying(Graphics2D g) {
        renderer.render(g, map, player, enemies, items);
        hud.render(g, player, messages, map);
    }

    private void renderGameOver(Graphics2D g) {
        // Dim background
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, SW, SH);

        // Title
        g.setFont(new Font("Arial", Font.BOLD, 64));
        String title = "GAME OVER";
        FontMetrics fm = g.getFontMetrics();
        g.setColor(new Color(200, 30, 30));
        g.drawString(title, (SW - fm.stringWidth(title)) / 2, SH / 2 - 20);

        // Stats
        g.setFont(new Font("Arial", Font.PLAIN, 26));
        fm = g.getFontMetrics();
        if (player != null) {
            String stats = "Floor: " + player.getFloor() + "    Score: " + player.getScore();
            g.setColor(Color.WHITE);
            g.drawString(stats, (SW - fm.stringWidth(stats)) / 2, SH / 2 + 30);
        }

        // Continue hint
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        fm = g.getFontMetrics();
        String hint = "Press  ENTER  to return to menu";
        g.setColor(new Color(180, 180, 180));
        g.drawString(hint, (SW - fm.stringWidth(hint)) / 2, SH / 2 + 80);
    }

    // =======================================================================
    // Utility
    // =======================================================================

    private static double dist(double ax, double ay, double bx, double by) {
        double dx = ax - bx, dy = ay - by;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
