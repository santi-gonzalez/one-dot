package cat.santi.mod.onedot.ai.movements.impl;

import cat.santi.mod.onedot.utils.RandomUtils;

/**
 *
 */
public class Turn extends AbstractMovement {

    public Turn() {
        super(
                RandomUtils.nextVelocity(VELOCITY_MIN, VELOCITY_MAX),
                RandomUtils.nextVelocity(VELOCITY_MIN, VELOCITY_MAX),
                RandomUtils.nextAcceleration(ACCELERATION_MIN, ACCELERATION_MAX),
                RandomUtils.nextAcceleration(ACCELERATION_MIN, ACCELERATION_MAX),
                RandomUtils.nextDuration(DURATION_MIN, DURATION_MAX));
    }
}
