package cat.santi.mod.onedot.entities;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;

import cat.santi.mod.onedot.managers.BitmapManager;

/**
 *
 */
public interface Entity {

    PointF getPosition();

    void process(Rect surface, double delta);

    void draw(Canvas canvas, BitmapManager bitmapManager);

    boolean shouldBeRemoved();
}
