package cat.santi.mod.onedot.entities.impl;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;

import cat.santi.mod.onedot.ConfigParams;
import cat.santi.mod.onedot.OneDotView;
import cat.santi.mod.onedot.ai.AIModule;
import cat.santi.mod.onedot.ai.movements.Movement;
import cat.santi.mod.onedot.entities.AbstractEntity;
import cat.santi.mod.onedot.entities.Killable;
import cat.santi.mod.onedot.entities.Movable;
import cat.santi.mod.onedot.managers.BitmapManager;

/**
 *
 */
public class Dot extends AbstractEntity
        implements Movable, Killable {

    private static final int SCORE = ConfigParams.SCORE;

    private AIModule mAIModule;
    private boolean mFlagKilled;

    private Movement mMovement;

    public Dot(PointF position, int size, AIModule aiModule) {
        super(position, size);
        mAIModule = aiModule;
        mFlagKilled = false;
    }

    @Override
    public boolean collides(float x, float y, float thumbRadius) {
        return Math.pow(x - mPosition.x, 2) + Math.pow(y - mPosition.y, 2) < Math.pow(thumbRadius, 2);
    }

    @Override
    public void smash(OneDotView view, float x, float y) {
        mFlagKilled = true;
        view.onDotSmashed(this);
    }

    @Override
    public int getScore() {
        return SCORE;
    }

    @Override
    public void draw(Canvas canvas, BitmapManager bitmapManager) {
        drawInternal(canvas);
    }

    @Override
    public boolean shouldBeRemoved() {
        return mFlagKilled;
    }

    @Override
    public void move(Rect surface, double delta) {
        if (mMovement == null || mMovement.isFinished())
            mMovement = mAIModule.next();

        mMovement.iterate(getPosition(), surface, delta);
    }
}
