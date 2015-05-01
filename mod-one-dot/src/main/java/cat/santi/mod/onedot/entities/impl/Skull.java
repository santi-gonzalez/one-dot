package cat.santi.mod.onedot.entities.impl;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;

import cat.santi.mod.onedot.ConfigParams;
import cat.santi.mod.onedot.entities.AbstractEntity;
import cat.santi.mod.onedot.manager.BitmapManager;

/**
 *
 */
public class Skull extends AbstractEntity {

    private static final int ALPHA_MAX = ConfigParams.ALPHA_MAX;
    private static final int ALPHA_DECAY_DELTA = ConfigParams.ALPHA_DECAY_DELTA;
    private static final int IMAGE_SIZE = ConfigParams.IMAGE_SIZE;

    private int mAlpha;

    public Skull(PointF position) {
        super(position, IMAGE_SIZE);
        resetAlpha();
    }

    @Override
    public void process(Rect surface, double delta) {
        super.process(surface, delta);
        decayAlpha(ALPHA_DECAY_DELTA);
    }

    @Override
    public void draw(Canvas canvas, BitmapManager bitmapManager) {
        drawInternal(canvas, bitmapManager.get(BitmapManager.BMP_INDEX_SKULL));
    }

    @Override
    public boolean shouldBeRemoved() {
        return mAlpha <= 0;
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
}
