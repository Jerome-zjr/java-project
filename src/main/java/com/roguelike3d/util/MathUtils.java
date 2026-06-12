package com.roguelike3d.util;

/**
 * Utility class for common mathematical operations used throughout the game.
 * Provides optimized distance calculations and trigonometric helpers.
 */
public class MathUtils {
    private MathUtils() {
        // Utility class, no instantiation
    }

    /**
     * Calculate squared Euclidean distance between two points.
     * Useful for distance comparisons without sqrt overhead.
     *
     * @param dx delta x
     * @param dy delta y
     * @return dx² + dy²
     */
    public static double distSquared(double dx, double dy) {
        return dx * dx + dy * dy;
    }

    /**
     * Calculate Euclidean distance between two points.
     *
     * @param dx delta x
     * @param dy delta y
     * @return √(dx² + dy²)
     */
    public static double distance(double dx, double dy) {
        return Math.sqrt(distSquared(dx, dy));
    }

    /**
     * Calculate squared Euclidean distance between two coordinate points.
     * Useful for distance comparisons without sqrt overhead.
     *
     * @param x1 first x coordinate
     * @param y1 first y coordinate
     * @param x2 second x coordinate
     * @param y2 second y coordinate
     * @return (x2-x1)² + (y2-y1)²
     */
    public static double distSquared(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return distSquared(dx, dy);
    }

    /**
     * Calculate Euclidean distance between two coordinate points.
     *
     * @param x1 first x coordinate
     * @param y1 first y coordinate
     * @param x2 second x coordinate
     * @param y2 second y coordinate
     * @return √((x2-x1)² + (y2-y1)²)
     */
    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(distSquared(x1, y1, x2, y2));
    }

    /**
     * Clamp a value between min and max.
     *
     * @param value the value to clamp
     * @param min minimum value
     * @param max maximum value
     * @return clamped value
     */
    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(value, max));
    }

    /**
     * Clamp an integer value between min and max.
     *
     * @param value the value to clamp
     * @param min minimum value
     * @param max maximum value
     * @return clamped value
     */
    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(value, max));
    }

    /**
     * Normalize an angle to the range [0, 2π).
     *
     * @param angle the angle in radians
     * @return normalized angle
     */
    public static double normalizeAngle(double angle) {
        double result = angle % (2 * Math.PI);
        return result < 0 ? result + 2 * Math.PI : result;
    }
}
