package cat.santi.mod.onedot;

import android.graphics.Color;

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

    public static final float SURFACE_PADDING = 20f;

    public static final int GENERATED_MOVEMENT_COUNT = 20;

    public static final boolean SHOW_TOUCHES_IN_DEBUG_MODE = false;
    public static final boolean SHOW_FPS_IN_DEBUG_MODE = false;

    public static final int DEF_SCORE = 0;
    public static final float THUMB_RADIUS = 17f;
    public static final int DEF_SURFACE = Color.WHITE;
    public static final boolean DEF_DEBUG = false;

    // Dot

    public static final int SCORE = 1;

    // Skull

    public static final int ALPHA_MAX = 255;
    public static final int ALPHA_DECAY_DELTA = 10;
    public static final int IMAGE_SIZE = 50;

    // Movement

    public static final float VELOCITY_MIN = -2f;
    public static final float VELOCITY_MAX = 2f;

    public static final float ACCELERATION_MIN = -.15f;
    public static final float ACCELERATION_MAX = .15f;

    public static final int DURATION_MIN = 5;
    public static final int DURATION_MAX = 30;

    private ConfigParams() {
        // Declare private constructor to thwart instantiation
    }
}
