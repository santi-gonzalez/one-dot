package cat.santi.mod.onedot.ai.impl;

import java.util.LinkedList;
import java.util.Queue;

import cat.santi.mod.onedot.ai.AIModule;
import cat.santi.mod.onedot.ai.movements.Movement;
import cat.santi.mod.onedot.utils.RandomUtils;

/**
 *
 */
public class AIModuleImpl implements
        AIModule {

    private Queue<Movement> mMovementQueue;

    public AIModuleImpl(int actionCount) {
        actionCount = ensureActionCount(actionCount);
        init(createMovementsArray(actionCount));
    }

    @SuppressWarnings("unused")
    public AIModuleImpl(Movement... movements) {
        init(movements);
    }

    @Override
    public Movement next() {
        if (mMovementQueue.size() == 0)
            return null;

        final Movement movement = mMovementQueue.poll();
        movement.reset();
        mMovementQueue.offer(movement);
        return movement;
    }

    private int ensureActionCount(int actionCount) {
        if (actionCount < 0)
            actionCount = 0;
        return actionCount;
    }

    private Movement[] createMovementsArray(int actionCount) {
        Movement[] movements = new Movement[actionCount];
        for(int index = 0 ; index < actionCount ; index++)
            movements[index] = RandomUtils.nextMovement();
        return movements;
    }

    private void init(Movement... movements) {
        if (movements == null || movements.length == 0)
            throw new IllegalArgumentException("actions must not be null or empty");

        mMovementQueue = new LinkedList<>();
        for (Movement movement : movements)
            mMovementQueue.offer(movement);
    }
}
