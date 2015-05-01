package cat.santi.mod.onedot.entities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

import cat.santi.mod.onedot.OneDotView;
import cat.santi.mod.onedot.manager.BitmapManager;

/**
 *
 */
public interface Entity {

    Point getPosition();

    void process(Rect surface, double delta);

    void draw(Canvas canvas);

    void draw(Canvas canvas, BitmapManager bitmapManager);

    void draw(Canvas canvas, Bitmap bitmap);

    boolean collides(float x, float y, float thumbRadius);

    void smash(OneDotView view, float x, float y);

    int getScore();

    boolean shouldBeRemoved();
}
