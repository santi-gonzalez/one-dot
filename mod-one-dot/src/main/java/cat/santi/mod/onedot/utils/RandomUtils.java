package cat.santi.mod.onedot.utils;

import android.os.SystemClock;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cat.santi.mod.onedot.ai.movements.Movement;
import cat.santi.mod.onedot.ai.movements.impl.Straight;
import cat.santi.mod.onedot.ai.movements.impl.Turn;
import cat.santi.mod.onedot.ai.movements.impl.Wait;

/**
 *
 */
public class RandomUtils {

    private static final List<Class<? extends Movement>> MOVEMENT_LIST;

    private static Random sRandom;
    private static long sSeed;

    static {
        MOVEMENT_LIST = new ArrayList<>();
        MOVEMENT_LIST.add(Wait.class);
        MOVEMENT_LIST.add(Straight.class);
        MOVEMENT_LIST.add(Turn.class);
    }

    private RandomUtils() {
        // Declare a private constructor to thwart instantiation
        createRandom();
    }

    public static long getSeed() {
        return sSeed;
    }

    public static void setSeed(long seed) {
        sSeed = seed;
        createRandom(seed);
    }

    public static Random getRandom() {
        ensureRandomCreated();
        return sRandom;
    }

    public static int nextDuration(int min, int max) {
        return (getRandom().nextInt(max - min + 1)) + min;
    }

    public static float nextVelocity(float min, float max) {
        return (getRandom().nextFloat() * (max - min)) + min;
    }

    public static float nextAcceleration(float min, float max) {
        return (getRandom().nextFloat() * (max - min)) + min;
    }

    public static Movement nextMovement() {
        try {
            final int movementIndex = getRandom().nextInt(MOVEMENT_LIST.size());
            return MOVEMENT_LIST.get(movementIndex).newInstance();
        } catch (Exception ex) {
            throw new RuntimeException("could not pick eligible random movement");
        }
    }

    private static void ensureRandomCreated() {
        if (sRandom == null)
            createRandom(sSeed);
    }

    private static void createRandom() {
        createRandom(SystemClock.currentThreadTimeMillis());
    }

    private static void createRandom(long seed) {
        sRandom = new Random(seed);
    }
}
