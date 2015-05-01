package cat.santi.mod.onedot.entities.impl;

import android.graphics.Point;
import android.graphics.Rect;

import cat.santi.mod.onedot.OneDotView;
import cat.santi.mod.onedot.SurfaceUtils;
import cat.santi.mod.onedot.entities.AbstractEntity;

/**
 *
 */
public class Dot extends AbstractEntity {

    private boolean mFlagKilled;

    public Dot(Rect surface, int size) {
        this(SurfaceUtils.generateRandomPoint(surface), size);
    }

    public Dot(Point position, int size) {
        super(position, size);
        mFlagKilled = false;
    }

    @Override
    public void smash(OneDotView view, float x, float y) {
        super.smash(view, x, y);
        mFlagKilled = true;
        view.increaseScore(getScore());
        view.generateSkull(mPosition.x, mPosition.y);
    }

    @Override
    public int getScore() {
        return 1;
    }

    @Override
    public boolean shouldBeRemoved() {
        return mFlagKilled;
    }
}
