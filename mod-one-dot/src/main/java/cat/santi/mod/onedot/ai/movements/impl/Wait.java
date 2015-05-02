package cat.santi.mod.onedot.ai.movements.impl;

import cat.santi.mod.onedot.utils.RandomUtils;

/**
 *
 */
public class Wait extends AbstractMovement {

    public Wait() {
        super(
                0f,
                0f,
                0f,
                0f,
                RandomUtils.nextDuration(DURATION_MIN, DURATION_MAX));
    }
}
