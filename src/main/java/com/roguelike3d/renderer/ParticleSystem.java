package com.roguelike3d.renderer;

import com.roguelike3d.theme.FloorTheme;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParticleSystem {

    private static final double DRIFT_FACTOR = 0.25;

    private final int width;
    private final int height;
    private final Random rng = new Random();
    private final List<Particle> particles = new ArrayList<>();

    private FloorTheme theme;

    private static final class Particle {
        double x;
        double y;
        double vx;
        double vy;
        int size;
        float alpha;
    }

    public ParticleSystem(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setTheme(FloorTheme theme) {
        this.theme = theme;
        particles.clear();
        if (theme == null) {
            return;
        }
        for (int i = 0; i < theme.particleCount(); i++) {
            particles.add(spawnParticle(theme, true));
        }
    }

    public void update(double dt) {
        if (theme == null) {
            return;
        }
        for (Particle p : particles) {
            p.x += p.vx * dt;
            p.y += p.vy * dt;
            if (p.y > height + p.size) {
                resetParticle(p, theme, false);
            }
            if (p.x < -p.size) {
                p.x = width + p.size;
            } else if (p.x > width + p.size) {
                p.x = -p.size;
            }
        }
    }

    public void render(Graphics2D g) {
        if (theme == null) {
            return;
        }
        Color base = theme.particleColor();
        for (Particle p : particles) {
            g.setColor(new Color(base.getRed(), base.getGreen(), base.getBlue(),
                    Math.min(255, Math.max(0, Math.round(p.alpha * 255)))));
            g.fillOval((int) p.x, (int) p.y, p.size, p.size);
        }
    }

    private Particle spawnParticle(FloorTheme theme, boolean randomY) {
        Particle p = new Particle();
        resetParticle(p, theme, randomY);
        return p;
    }

    private void resetParticle(Particle p, FloorTheme theme, boolean randomY) {
        int minSize = theme.particleMinSize();
        int maxSize = Math.max(minSize, theme.particleMaxSize());
        p.size = minSize + rng.nextInt(maxSize - minSize + 1);
        p.alpha = 0.35f + rng.nextFloat() * 0.45f;
        p.x = rng.nextDouble() * width;
        p.y = randomY ? rng.nextDouble() * height : -p.size - rng.nextDouble() * height * 0.2;

        double speed = theme.particleSpeed();
        p.vx = (rng.nextDouble() - 0.5) * speed * DRIFT_FACTOR;
        p.vy = speed * (0.4 + rng.nextDouble() * 0.6);
    }
}
