package cat.santi.mod.onedot.ai.movements;

import android.graphics.PointF;
import android.graphics.Rect;

/**
 *
 */
public interface Movement {

    void reset();

    void iterate(PointF position, Rect surface, double delta);

    boolean isFinished();
}
