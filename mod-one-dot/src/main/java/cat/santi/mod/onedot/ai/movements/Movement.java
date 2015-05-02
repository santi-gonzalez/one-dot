package cat.santi.mod.onedot.ai.movements;

import android.graphics.PointF;

/**
 *
 */
public interface Movement {

    void reset();

    void iterate(PointF position, double delta);

    float getVelocityX();

    float getVelocityY();

    boolean isFinished();
}
