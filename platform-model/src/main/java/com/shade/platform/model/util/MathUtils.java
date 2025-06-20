package com.shade.platform.model.util;

public class MathUtils {
    public static final double TAU = Math.TAU;
    public static final double HALF_PI = Math.PI / 2.0;

    private MathUtils() {
        // prevents instantiation
    }

    public static int alignUp(int value, int alignment) {
        return ceilDiv(value, alignment) * alignment;
    }

    public static int wrapAround(int value, int max) {
        return (value % max + max) % max;
    }

    public static int wrapAround(int value, int min, int max) {
        if (value < min) {
            return max - (min - value) % (max - min);
        } else {
            return min + (value - min) % (max - min);
        }
    }

    public static int ceilDiv(int x, int y) {
        return Math.ceilDiv(x, y);
    }

    public static long ceilDiv(long x, long y) {
        return Math.ceilDiv(x, y);
    }

    public static float halfToFloat(int value) {
        return Float.float16ToFloat((short) value);
    }

    public static int floatToHalf(float value) {
        return Float.floatToFloat16(value);
    }

    public static float clamp(float value, float min, float max) {
        return Math.clamp(value, min, max);
    }

    public static int clamp(int value, int min, int max) {
        return Math.clamp(value, min, max);
    }
}
