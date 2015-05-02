package cat.santi.mod.onedot.ai.actions.impl;

import cat.santi.mod.onedot.ai.actions.Action;

/**
 *
 */
public abstract class AbstractAction implements
        Action {

    private final float mVelocityX;
    private final float mVelocityY;
    private final int mTicks;

    private int mTickCount;

    AbstractAction(float velocityX, float velocityY, int ticks) {
        mVelocityX = velocityX;
        mVelocityY = velocityY;
        mTicks = ticks;
        mTickCount = 0;
    }

    @Override
    public void reset() {
        mTickCount = 0;
    }

    @Override
    public void iterate() {
        performLogic();
        mTickCount++;
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
    public boolean isFinished() {
        return mTickCount >= mTicks;
    }

    public abstract void performLogic();
}
