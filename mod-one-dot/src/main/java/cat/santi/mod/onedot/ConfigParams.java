package cat.santi.mod.onedot;

/**
 * This class in meant to be only as a helper to keep track on where all constants are declared.
 * This class will most likely banish in 1.0 version...
 */
public class ConfigParams {

    // OneDotView

    public static final int TARGET_FPS = 30;

    public static final int SIZE_DOT_SMALL = 1;
    public static final int SIZE_DOT_MEDIUM = 5;
    public static final int SIZE_DOT_LARGE = 10;

    public static final float THUMB_RADIUS = 50f;

    // Dot

    public static final int SCORE = 1;

    public static final float DEFAULT_VELOCITY_X = 0f;
    public static final float DEFAULT_VELOCITY_Y = 0f;

    // Skull

    public static final int ALPHA_MAX = 255;
    public static final int ALPHA_DECAY_DELTA = 10;
    public static final int IMAGE_SIZE = 50;

    private ConfigParams() {
        // Declare private constructor to thwart instantiation
    }
}
