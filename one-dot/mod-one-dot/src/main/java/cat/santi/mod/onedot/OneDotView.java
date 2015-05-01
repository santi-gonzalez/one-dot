package cat.santi.mod.onedot;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import cat.santi.mod.onedot.entities.Entity;
import cat.santi.mod.onedot.entities.impl.Dot;
import cat.santi.mod.onedot.entities.impl.Skull;

/**
 * Main view for one-dot enemies style game, which is self-managed. This means that by only adding
 * this view to a layout, and a few lifecycle and method calls, you can have an up-and-running game.
 * <p/>
 * This game also presents a {@link cat.santi.mod.onedot.OneDotView.OneDotCallbacks} interface on
 * which you can subscribe an object to be notified about game events, such as score changes or
 * user interaction. This is intended to decouple the game logic (dots, smashes, power-ups) from
 * statics representation. (for instance, you can have a {@code TextView} separate of the game
 * to display the player score).
 * <p/>
 * Note: Don't forget to add {@link OneDotView#onResume()}, {@link OneDotView#onPause()} and
 * {@link OneDotView#onDestroy()} in the appropriate lifecycle callbacks.
 */
public class OneDotView extends FrameLayout {

    private static final String TAG = OneDotView.class.getSimpleName();

    //////////////////////////////////////////////////
    // CONSTANTS
    //////////////////////////////////////////////////

    private static final int TARGET_FPS = 30;

    private static final int SIZE_DOT_SMALL = 1;
    private static final int SIZE_DOT_MEDIUM = 5;
    private static final int SIZE_DOT_LARGE = 10;

    private static final float THUMB_RADIUS = 50f;

    // Default attribute values

    private static final int DEF_SURFACE = Color.WHITE;
    private static final boolean DEF_DEBUG = false;

    //////////////////////////////////////////////////
    // FIELDS
    //////////////////////////////////////////////////

    private int mScore;

    private List<Entity> mEntities;

    private Rect mSurfaceRect;

    private PointF mThumbPoint;
    private Paint mThumbPaint;

    private GameLoop mGameLoop;

    private OneDotCallbacks mCallbacks;

    // View attributes

    private boolean _debug;
    private int mSurface;

    //////////////////////////////////////////////////
    // METHODS
    //////////////////////////////////////////////////

    // Required constructors

    public OneDotView(Context context) {
        super(context);
        init();
    }

    public OneDotView(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseAttributes(attrs, 0);
        init();
    }

    public OneDotView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseAttributes(attrs, defStyleAttr);
        init();
    }

    // Overridden methods

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);

        // Put fields which are gonna be saved
        ss.score = this.mScore;
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        // Put fields which are gonna be restored
        this.mScore = ss.score;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mThumbPoint = new PointF(event.getX(), event.getY());
                checkCollisions(mThumbPoint);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mThumbPoint = null;
                break;
        }
        return true;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        updateSurfaceRect(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        onDrawDebug(canvas);
        onDrawEntities(canvas);
    }

    // View attributes

    /**
     * Get whether or not this view is in debug mode.
     *
     * @return {@code true} if the view is in debug mode. {@code false} otherwise.
     */
    public boolean isDebug() {
        return _debug;
    }

    /**
     * Set whether or not this view should run in debug mode.
     * <p/>
     * <i>Default mode is NOT debug.</i>
     *
     * @param debug Set to {@code true} to allow debug mode.
     */
    public void setDebug(boolean debug) {
        _debug = debug;
    }

    /**
     * Get the surface color.
     *
     * @return The surface color.
     */
    public int getSurface() {
        return mSurface;
    }

    /**
     * Set the surface color.
     * <p/>
     * <i>Default color is WHITE.</i>
     *
     * @param surface The surface color to apply.
     */
    public void setSurface(int surface) {
        mSurface = surface;
        setBackgroundColor(surface);
    }

    // View accessors

    public void onResume() {
        mGameLoop.onResume();
    }

    public void onPause() {
        mGameLoop.onPause();
    }

    public int onDestroy() {
        mGameLoop.finish();
        return mScore;
    }

    public void increaseScore(int score) {
        mScore += score;
        notifyScoreChanged(mScore, score);
    }

    public void generateDot() {
        mEntities.add(new Dot(mSurfaceRect, SIZE_DOT_SMALL));
    }

    public void generateDot(int x, int y) {
        mEntities.add(new Dot(new Point(x, y), SIZE_DOT_SMALL));
    }

    public void generateSkull(int x, int y) {
        mEntities.add(new Skull(new Point(x, y)));
    }

    public void setCallbacks(OneDotCallbacks callbacks) {
        mCallbacks = callbacks;
    }

    // Private methods

    private void parseAttributes(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().obtainStyledAttributes(
                attrs,
                R.styleable.OneDotView,
                defStyleAttr, 0);

        setDebug(a.getBoolean(R.styleable.OneDotView_odvDebug, DEF_DEBUG));
        setSurface(a.getColor(R.styleable.OneDotView_odvSurface, DEF_SURFACE));

        a.recycle();
    }

    private void init() {
        // Tell the view the #onDraw method should be considered
        setWillNotDraw(false);
        // Initialize fields
        mGameLoop = new GameLoop();
        mEntities = new ArrayList<>();
        mThumbPoint = null;
        mThumbPaint = new Paint();
        mThumbPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mThumbPaint.setColor(Color.GREEN);

        // Begin looping
        mGameLoop.start();
    }

    private void updateSurfaceRect(boolean changed, int left, int top, int right, int bottom) {
        if (changed) {
            int padding = SurfaceUtils.dipToPixels(getResources().getDisplayMetrics(), 20);
            // TODO: SHOULD ENSURE THERE IS ENOUGH SURFACE SPACE BEFORE DOING ANYTHING ELSE...
            mSurfaceRect = new Rect(left + padding, top + padding, right - padding, bottom - padding);

            // Add some dots just for fun
            for (int index = 0; index < 10; index++)
                generateDot();
        }
    }

    private void process(double delta) {
        if (mSurfaceRect != null)
            for (int index = mEntities.size() - 1; index >= 0; index--)
                if (mEntities.get(index).shouldBeRemoved())
                    mEntities.remove(index);
                else
                    mEntities.get(index).process(mSurfaceRect, delta);
    }

    private void updateFPS(int fps) {
        if (isDebug())
            Log.d(TAG, "FPS: " + fps);
    }

    private void checkCollisions(PointF point) {
        for (int index = mEntities.size() - 1; index >= 0; index--)
            if (mEntities.get(index).collides(point.x, point.y, THUMB_RADIUS))
                mEntities.get(index).smash(this, point.x, point.y);
    }

    private void onDrawEntities(Canvas canvas) {
        for (int index = mEntities.size() - 1; index >= 0; index--)
            mEntities.get(index).draw(canvas, getResources());
    }

    private void onDrawDebug(Canvas canvas) {
        if (_debug && mThumbPoint != null)
            canvas.drawCircle(mThumbPoint.x, mThumbPoint.y, THUMB_RADIUS, mThumbPaint);
    }

    private void notifyScoreChanged(int score, int delta) {
        if (mCallbacks != null)
            mCallbacks.onScoreChanged(score, delta);
    }

    public interface OneDotCallbacks {

        void onScoreChanged(int score, int delta);
    }

    private static class SavedState extends BaseSavedState {

        //required field that makes Parcelables from a Parcel
        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {

                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };

        int score;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.score = in.readInt();
        }

        @Override
        public void writeToParcel(@NonNull Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.score);
        }
    }

    private class GameLoop extends Thread {

        private final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;

        private volatile boolean mCancelled;
        private volatile boolean mTriggersLogic;

        private long mLastLoopTime;
        private long mLastFpsTime;
        private int mFps;

        GameLoop() {
            mTriggersLogic = false;
            mCancelled = false;
            mLastLoopTime = System.nanoTime();
            mLastFpsTime = 0;
            mFps = 0;
        }

        @Override
        public void run() {
            while (!mCancelled) {
                final long now = System.nanoTime();
                final long updateLength = now - mLastLoopTime;
                mLastLoopTime = now;
                final double delta = updateLength / ((double) OPTIMAL_TIME);

                mLastFpsTime += updateLength;
                mFps++;

                if (mLastFpsTime >= 1000000000) {
                    updateFPS(mFps);
                    mLastFpsTime = 0;
                    mFps = 0;
                }

                if (mTriggersLogic) {
                    process(delta);
                    postInvalidate();
                }

                try {
                    Thread.sleep((mLastLoopTime - System.nanoTime() + OPTIMAL_TIME) / 1000000);
                } catch (Exception ignored) {
                }
            }
        }

        public void onResume() {
            mTriggersLogic = true;
        }

        public void onPause() {
            mTriggersLogic = false;
        }

        public void finish() {
            mCancelled = true;
        }
    }
}