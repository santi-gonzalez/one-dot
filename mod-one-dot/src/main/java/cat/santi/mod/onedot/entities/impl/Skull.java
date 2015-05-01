package cat.santi.mod.onedot.entities.impl;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

import cat.santi.mod.onedot.Bitmaps;
import cat.santi.mod.onedot.entities.AbstractEntity;

/**
 *
 */
public class Skull extends AbstractEntity {

    private static final int ALPHA_MAX = 250;
    private static final int ALPHA_DECAY_DELTA = 25;
    private static final int IMAGE_SIZE = 50;

    private int mAlpha;

    public Skull(Point position) {
        super(position, IMAGE_SIZE);
        resetAlpha();
    }

    @Override
    public void process(Rect surface, double delta) {
        super.process(surface, delta);
        decayAlpha(ALPHA_DECAY_DELTA);
    }

    @Override
    public void draw(Canvas canvas, Resources resources) {
        super.draw(canvas, Bitmaps.getInstance(resources).get(Bitmaps.BITMAP_SKULL));
    }

    private void resetAlpha() {
        mAlpha = ALPHA_MAX;
        getPaint().setAlpha(mAlpha);
    }

    private void decayAlpha(int alphaDecayDelta) {
        mAlpha -= alphaDecayDelta;
        if (mAlpha < 0)
            mAlpha = 0;
        getPaint().setAlpha(mAlpha);
    }

    @Override
    public boolean shouldBeRemoved() {
        return mAlpha <= 0;
    }
}
