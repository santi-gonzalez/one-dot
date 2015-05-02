package cat.santi.mod.onedot.ai.actions;

/**
 *
 */
public interface Action {

    void reset();

    void iterate();

    float getVelocityX();

    float getVelocityY();

    boolean isFinished();
}
