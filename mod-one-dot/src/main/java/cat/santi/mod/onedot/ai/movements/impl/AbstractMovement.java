package cat.santi.mod.onedot.ai.movements.impl;

import android.graphics.PointF;

import cat.santi.mod.onedot.ConfigParams;
import cat.santi.mod.onedot.ai.movements.Movement;

/**
 *
 */
public abstract class AbstractMovement implements
        Movement {

    protected static final float VELOCITY_MIN = ConfigParams.VELOCITY_MIN;
    protected static final float VELOCITY_MAX = ConfigParams.VELOCITY_MAX;

    protected static final float ACCELERATION_MIN = ConfigParams.ACCELERATION_MIN;
    protected static final float ACCELERATION_MAX = ConfigParams.ACCELERATION_MAX;

    protected static final int DURATION_MIN = ConfigParams.DURATION_MIN;
    protected static final int DURATION_MAX = ConfigParams.DURATION_MAX;

    private float mVelocityX;
    private float mVelocityY;
    private float mAccelerationX;
    private float mAccelerationY;
    private final int mDuration;

    private int mTickCount;

    AbstractMovement(float velocityX, float velocityY, float accelerationX, float accelerationY, int duration) {
        mVelocityX = velocityX;
        mVelocityY = velocityY;
        mAccelerationX = accelerationX;
        mAccelerationY = accelerationY;
        mDuration = duration;
        reset();
    }

    @Override
    public void reset() {
        mTickCount = 0;
    }

    @Override
    public void iterate(PointF position, double delta) {
        performLogic(position, delta);
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
        return mTickCount >= mDuration;
    }

    public void performLogic(PointF position, double delta) {
        position.x += mVelocityX * delta;
        position.y += mVelocityY * delta;
        mVelocityX += mAccelerationX * delta;
        mVelocityY += mAccelerationY * delta;
    }
}
