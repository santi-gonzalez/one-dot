package cat.santi.mod.onedot.entities;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import cat.santi.mod.onedot.OneDotView;

/**
 *
 */
public interface Entity {

    void process(Rect surface, double delta);

    void draw(Canvas canvas);

    void draw(Canvas canvas, Resources resources);

    void draw(Canvas canvas, Bitmap bitmap);

    boolean collides(float x, float y, float thumbRadius);

    void smash(OneDotView view, float x, float y);

    int getScore();

    boolean shouldBeRemoved();
}
