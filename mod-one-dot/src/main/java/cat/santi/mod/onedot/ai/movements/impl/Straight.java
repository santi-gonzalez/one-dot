package cat.santi.mod.onedot.ai.movements.impl;

import cat.santi.mod.onedot.utils.RandomUtils;

/**
 *
 */
public class Straight extends AbstractMovement {

    public Straight() {
        super(
                RandomUtils.nextVelocity(VELOCITY_MIN, VELOCITY_MAX),
                RandomUtils.nextVelocity(VELOCITY_MIN, VELOCITY_MAX),
                0f,
                0f,
                RandomUtils.nextDuration(DURATION_MIN, DURATION_MAX));
    }
}
