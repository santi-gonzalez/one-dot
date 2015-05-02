package cat.santi.mod.onedot.entities;

import cat.santi.mod.onedot.OneDotView;

/**
 *
 */
public interface Killable {

    boolean collides(float x, float y, float thumbRadius);

    void smash(OneDotView view, float x, float y);

    int getScore();
}
