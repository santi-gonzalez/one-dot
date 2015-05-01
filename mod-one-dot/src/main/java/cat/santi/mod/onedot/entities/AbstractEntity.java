package cat.santi.mod.onedot.entities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;

/**
 *
 */
public abstract class AbstractEntity implements
        Entity {

    protected PointF mPosition;

    private float mSize;
    private Paint mPaint;

    public AbstractEntity(PointF position, int size) {
        if (size < 1)
            size = 1;
        mPosition = position;
        mSize = size;
        initPaint();
    }

    @Override
    public PointF getPosition() {
        return mPosition;
    }

    @Override
    public void process(Rect surface, double delta) {
        // Default implementation does nothing
    }

    @Override
    public boolean shouldBeRemoved() {
        return false;
    }

    protected Paint getPaint() {
        return mPaint;
    }

    protected void drawInternal(Canvas canvas) {
        drawInternal(canvas, null);
    }

    protected void drawInternal(Canvas canvas, Bitmap bitmap) {
        if (bitmap != null) {
            int w = bitmap.getScaledWidth(canvas);
            int h = bitmap.getScaledHeight(canvas);
            canvas.drawBitmap(bitmap, mPosition.x - (w / 2), mPosition.y - (h / 2), mPaint);
        } else {
            canvas.drawCircle(mPosition.x, mPosition.y, mSize, mPaint);
        }
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }
}