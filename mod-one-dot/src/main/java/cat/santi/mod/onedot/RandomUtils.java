package cat.santi.mod.onedot;

import android.os.SystemClock;

import java.util.Random;

/**
 *
 */
public class RandomUtils {

    private static Random sRandom;
    private static long sSeed;

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
