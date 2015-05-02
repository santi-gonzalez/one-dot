package cat.santi.mod.onedot.ai.impl;

import java.util.LinkedList;
import java.util.Queue;

import cat.santi.mod.onedot.ai.AIModule;
import cat.santi.mod.onedot.ai.actions.Action;

/**
 *
 */
public class AIModuleImpl implements
        AIModule {

    private Queue<Action> mActionQueue;

    public AIModuleImpl(Action... actions) {
        if (actions == null || actions.length == 0)
            throw new IllegalArgumentException("actions must not be null or empty");

        mActionQueue = new LinkedList<>();
        for (Action action : actions)
            mActionQueue.offer(action);
    }

    @Override
    public Action next() {
        if (mActionQueue.size() == 0)
            return null;

        final Action action = mActionQueue.poll();
        action.reset();
        mActionQueue.offer(action);
        return action;
    }
}
