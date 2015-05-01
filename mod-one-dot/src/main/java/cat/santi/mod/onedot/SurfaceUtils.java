package cat.santi.mod.onedot;

import android.graphics.Point;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 *
 */
public class SurfaceUtils {

    private SurfaceUtils() {
        // Declare a private constructor to thwart instantiation
    }

    public static Point generateRandomPoint(Rect boundaries) {
        return new Point(
                RandomUtils.getRandom().nextInt(boundaries.right - boundaries.left) + boundaries.left,
                RandomUtils.getRandom().nextInt(boundaries.bottom - boundaries.top) + boundaries.top);
    }

    public static int dipToPixels(DisplayMetrics displayMetrics, float dipValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, displayMetrics);
    }
}
