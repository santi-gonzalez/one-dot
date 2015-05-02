package cat.santi.mod.onedot.ai.movements.impl;

import android.graphics.PointF;
import android.graphics.Rect;

import cat.santi.mod.onedot.utils.ConfigUtils;
import cat.santi.mod.onedot.ai.movements.Movement;

/**
 *
 */
public abstract class AbstractMovement implements
        Movement {

    protected static final float VELOCITY_MIN = ConfigUtils.VELOCITY_MIN;
    protected static final float VELOCITY_MAX = ConfigUtils.VELOCITY_MAX;

    protected static final float ACCELERATION_MIN = ConfigUtils.ACCELERATION_MIN;
    protected static final float ACCELERATION_MAX = ConfigUtils.ACCELERATION_MAX;

    protected static final int DURATION_MIN = ConfigUtils.DURATION_MIN;
    protected static final int DURATION_MAX = ConfigUtils.DURATION_MAX;

    private final float mStartingVelocityX;
    private final float mStartingVelocityY;
    private final float mStartingAccelerationX;
    private final float mStartingAccelerationY;

    private float mVelocityX;
    private float mVelocityY;
    private float mAccelerationX;
    private float mAccelerationY;
    private final int mDuration;

    private int mTickCount;

    AbstractMovement(float velocityX, float velocityY, float accelerationX, float accelerationY, int duration) {
        mStartingVelocityX = velocityX;
        mStartingVelocityY = velocityY;
        mStartingAccelerationX = accelerationX;
        mStartingAccelerationY = accelerationY;
        mDuration = duration;
        reset();
    }

    @Override
    public void reset() {
        mTickCount = 0;
        mVelocityX = mStartingVelocityX;
        mVelocityY = mStartingVelocityY;
        mAccelerationX = mStartingAccelerationX;
        mAccelerationY = mStartingAccelerationY;
    }

    @Override
    public void iterate(PointF position, Rect surface, double delta) {
        performLogic(position, surface, delta);
        mTickCount++;
    }

    @Override
    public boolean isFinished() {
        return mTickCount >= mDuration;
    }

    protected void performLogic(PointF position, Rect surface, double delta) {
        position.x += mVelocityX * delta;
        position.y += mVelocityY * delta;
        mVelocityX += mAccelerationX * delta;
        mVelocityY += mAccelerationY * delta;
        ensurePosition(position, surface);
    }

    protected void ensurePosition(PointF position, Rect surface) {
        if(position.x < surface.left)
            position.x = surface.left;
        if(position.x > surface.right)
            position.x = surface.right;
        if(position.y < surface.top)
            position.y = surface.top;
        if(position.y > surface.bottom)
            position.y = surface.bottom;
    }
}
