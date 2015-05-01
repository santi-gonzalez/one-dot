package cat.santi.mod.onedot.entities.impl;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;

import cat.santi.mod.onedot.OneDotView;
import cat.santi.mod.onedot.entities.AbstractEntity;
import cat.santi.mod.onedot.entities.Killable;
import cat.santi.mod.onedot.entities.Movable;
import cat.santi.mod.onedot.manager.BitmapManager;

/**
 *
 */
public class Dot extends AbstractEntity
        implements Movable, Killable {

    private static final int SCORE = 1;

    private static final float DEFAULT_VELOCITY_X = 0f;
    private static final float DEFAULT_VELOCITY_Y = 0f;

    private boolean mFlagKilled;
    private float mVelocityX;
    private float mVelocityY;

    public Dot(PointF position, int size) {
        super(position, size);
        mFlagKilled = false;
        mVelocityX = DEFAULT_VELOCITY_X;
        mVelocityY = DEFAULT_VELOCITY_Y;
    }

    @Override
    public float getVelocityX() {
        return mVelocityX;
    }

    @Override
    public float getVelocityY() {
        return mVelocityY;
    }

    @Override
    public boolean collides(float x, float y, float thumbRadius) {
        return Math.pow(x - mPosition.x, 2) + Math.pow(y - mPosition.y, 2) < Math.pow(thumbRadius, 2);
    }

    @Override
    public void smash(OneDotView view, float x, float y) {
        mFlagKilled = true;
        view.dotSmashed(this);
    }

    @Override
    public int getScore() {
        return SCORE;
    }

    @Override
    public void process(Rect surface, double delta) {
        getPosition().x += getVelocityX() * delta;
        getPosition().y += getVelocityY() * delta;
    }

    @Override
    public void draw(Canvas canvas, BitmapManager bitmapManager) {
        drawInternal(canvas);
    }

    @Override
    public boolean shouldBeRemoved() {
        return mFlagKilled;
    }
}
