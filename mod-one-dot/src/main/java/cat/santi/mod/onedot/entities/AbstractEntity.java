package cat.santi.mod.onedot.entities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import cat.santi.mod.onedot.OneDotView;
import cat.santi.mod.onedot.manager.BitmapManager;

/**
 *
 */
public abstract class AbstractEntity implements
        Entity {

    protected Point mPosition;

    private float mSize;
    private Paint mPaint;

    public AbstractEntity(Point position, int size) {
        if (size < 1)
            size = 1;
        mPosition = position;
        mSize = size;
        initPaint();
    }

    @Override
    public Point getPosition() {
        return mPosition;
    }

    @Override
    public void process(Rect surface, double delta) {
//        for (int i = 0; i < stuff.size(); i++) {
//            // all time-related values must be multiplied by delta!
//            Stuff s = stuff.get(i);
//            s.velocity += Gravity.VELOCITY * delta;
//            s.position += s.velocity * delta;
//
//            // stuff that isn't time-related doesn't care about delta...
//            if (s.velocity >= 1000) {
//                s.color = Color.RED;
//            } else {
//                s.color = Color.BLUE;
//            }
//        }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(mPosition.x, mPosition.y, mSize, mPaint);
    }

    @Override
    public void draw(Canvas canvas, BitmapManager bitmapManager) {
        // Default implementation only renders a dot, not a bitmap
        // override this method in concrete entity sub-class if you want to print specific bitmap
        draw(canvas);
    }

    @Override
    public void draw(Canvas canvas, Bitmap bitmap) {
        if (bitmap != null) {
            int w = bitmap.getScaledWidth(canvas);
            int h = bitmap.getScaledHeight(canvas);
            canvas.drawBitmap(bitmap, mPosition.x - (w / 2), mPosition.y - (h / 2), mPaint);
        } else {
            draw(canvas);
        }
    }

    @Override
    public boolean collides(float x, float y, float thumbRadius) {
        return Math.pow(x - mPosition.x, 2) + Math.pow(y - mPosition.y, 2) < Math.pow(thumbRadius, 2);
    }

    @Override
    public void smash(OneDotView view, float x, float y) {
        // Default implementation does nothing
    }

    @Override
    public int getScore() {
        return 0;
    }

    @Override
    public boolean shouldBeRemoved() {
        return false;
    }

    protected Paint getPaint() {
        return mPaint;
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }
}