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

import cat.santi.mod.onedot.ai.impl.AIModuleImpl;
import cat.santi.mod.onedot.entities.Entity;
import cat.santi.mod.onedot.entities.Killable;
import cat.santi.mod.onedot.entities.impl.Dot;
import cat.santi.mod.onedot.entities.impl.Skull;
import cat.santi.mod.onedot.managers.BitmapManager;
import cat.santi.mod.onedot.managers.ManagerFactory;
import cat.santi.mod.onedot.utils.ConfigUtils;
import cat.santi.mod.onedot.utils.RandomUtils;
import cat.santi.mod.onedot.utils.SurfaceUtils;

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

    private static final int TARGET_CYCLES_PER_SECOND = ConfigUtils.TARGET_CYCLES_PER_SECOND;

    @SuppressWarnings("unused")
    private static final int SIZE_DOT_SMALL = ConfigUtils.SIZE_DOT_SMALL;
    @SuppressWarnings("unused")
    private static final int SIZE_DOT_MEDIUM = ConfigUtils.SIZE_DOT_MEDIUM;
    @SuppressWarnings("unused")
    private static final int SIZE_DOT_LARGE = ConfigUtils.SIZE_DOT_LARGE;

    private static final float SURFACE_PADDING = ConfigUtils.SURFACE_PADDING;

    private static final int GENERATED_MOVEMENT_COUNT = ConfigUtils.GENERATED_MOVEMENT_COUNT;

    private static final boolean SHOW_TOUCHES_IN_DEBUG_MODE = ConfigUtils.SHOW_TOUCHES_IN_DEBUG_MODE;
    private static final boolean SHOW_FPS_IN_DEBUG_MODE = ConfigUtils.SHOW_FPS_IN_DEBUG_MODE;

    // Default attribute values

    private static final int DEF_SCORE = ConfigUtils.DEF_SCORE;
    private static final float DEF_THUMB_RADIUS = ConfigUtils.THUMB_RADIUS;
    private static final int DEF_SURFACE = ConfigUtils.DEF_SURFACE;
    private static final boolean DEF_DEBUG = ConfigUtils.DEF_DEBUG;

    //////////////////////////////////////////////////
    // FIELDS
    //////////////////////////////////////////////////

    // Game fields

    private GameLoop mGameLoop;
    private List<Entity> mEntities;
    private PointF mThumbPoint;
    private Paint mThumbPaint;

    // View fields

    private Rect mSurfaceRect;

    private OneDotCallbacks mCallbacks;

    // Managers

    private BitmapManager mBitmapManager;

    // View attributes

    private int mScore;
    private float mThumbRadius;
    private int mSurface;
    private boolean _debug;

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
        ss.score = getScore();
        ss.surface = getSurface();
        ss.debug = isDebug();
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        // Put fields which are gonna be restored
        setScore(ss.score);
        setSurface(ss.surface);
        setDebug(ss.debug);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        // Ignore touches when paused or destroyed
        if (mGameLoop != null && (mGameLoop.isPaused() || mGameLoop.isCancelled()))
            return false;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //noinspection PointlessBooleanExpression,ConstantConditions
                if (isDebug() && SHOW_TOUCHES_IN_DEBUG_MODE)
                    Log.v(TAG, "User touched the surface...");
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
        if (changed)
            setSurfaceRect(left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawDebug(canvas);
        drawEntities(canvas);
    }

    // View attributes

    public int getScore() {
        return mScore;
    }

    public void setScore(int score) {
        if (score < 0)
            score = 0;
        this.mScore = score;
    }

    public float getThumbRadiusPx() {
        return mThumbRadius;
    }

    public void setThumbRadiusPx(float thumbRadius) {
        mThumbRadius = thumbRadius;
    }

    @SuppressWarnings("unused")
    public void setThumbRadiusDip(float thumbRadius) {
        setThumbRadiusPx(SurfaceUtils.dipToPixels(getResources().getDisplayMetrics(), thumbRadius));
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
     * Set the surface color, actually changing the {@code android:background} xml attribute.
     * <p/>
     * <i>Default color is WHITE.</i>
     *
     * @param surface The surface color to apply.
     */
    public void setSurface(int surface) {
        mSurface = surface;
        setBackgroundColor(surface);
    }

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

    // View accessors

    public void newGame(int dots) {
        newGame(dots, null);
    }

    public void newGame(int dots, Long seed) {
        if (mSurfaceRect == null)
            throw new IllegalStateException("A new game can not be started before creation of" +
                    "view layout");

        if (isDebug())
            Log.v(TAG, "Creating a new game...");

        if (seed != null)
            RandomUtils.setSeed(seed);

        setScore(0);
        clearEntities();
        generateDots(dots);
        onResume();
        if (isDebug())
            Log.v(TAG, "New game created!");
    }

    public void onResume() {
        if (mGameLoop == null || mGameLoop.isCancelled())
            return;

        if (isDebug())
            Log.v(TAG, "Game resumed");
        mGameLoop.onResume();
    }

    public void onPause() {
        if (mGameLoop == null || mGameLoop.isCancelled())
            return;

        if (isDebug())
            Log.v(TAG, "Game paused");
        mGameLoop.onPause();
    }

    public int onDestroy() {
        if (mGameLoop == null || mGameLoop.isCancelled())
            return 0;

        if (isDebug())
            Log.v(TAG, "Game destroyed | score:" + getScore());
        mGameLoop.finish();
        mEntities.clear();
        invalidate();
        return getScore();
    }

    /**
     * This method is used by entities. <b>Do not invoke this method directly.</b>
     *
     * @param dot The <i>dot</i> which has been smashed.
     */
    // TODO: REFACTOR PROCESS
    public void onDotSmashed(Dot dot) {
        if (isDebug())
            Log.v(TAG, "Dot smashed!");
        addScore(dot.getScore());
        generateSkull(dot.getPosition().x, dot.getPosition().y);
    }

    public void addScore(int score) {
        if (isDebug())
            Log.v(TAG, "Score added:" + score);
        setScore(getScore() + score);
        notifyScoreChanged(getScore(), score);
    }

    public void generateDot() {
        final Point point = SurfaceUtils.generateRandomPoint(mSurfaceRect);
        generateDot(point.x, point.y);
    }

    public void generateDot(float x, float y) {
        mEntities.add(new Dot(new PointF(x, y), SIZE_DOT_MEDIUM, new AIModuleImpl(GENERATED_MOVEMENT_COUNT)));
        if (isDebug())
            Log.v(TAG, "A dot was generated");
    }

    public void clearEntities() {
        if (mEntities == null)
            mEntities = new ArrayList<>();
        mEntities.clear();
    }

    public void generateDots(int dots) {
        for (int index = 0; index < dots; index++)
            generateDot();
    }

    public void generateSkull(float x, float y) {
        mEntities.add(new Skull(new PointF(x, y)));
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

        setScore(a.getInteger(R.styleable.OneDotView_odvScore, DEF_SCORE));
        setThumbRadiusPx(a.getDimension(R.styleable.OneDotView_odvThumbRadius, DEF_THUMB_RADIUS));
        setSurface(a.getColor(R.styleable.OneDotView_odvSurface, DEF_SURFACE));
        setDebug(a.getBoolean(R.styleable.OneDotView_odvDebug, DEF_DEBUG));

        a.recycle();
    }

    private void init() {
        // Tell the view the #onDraw method should be considered
        setWillNotDraw(false);
        // Initialize manager
        mBitmapManager = ManagerFactory.getBitmapManager(getResources());
        // Initialize fields
        mGameLoop = new GameLoop();
        mEntities = new ArrayList<>();
        mThumbPoint = null;
        mThumbPaint = new Paint();
        mThumbPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mThumbPaint.setColor(Color.GREEN);

        // Begin looping
        mGameLoop.start();

        if (isDebug())
            Log.v(TAG, ">> GAME VIEW CREATED <<");
    }

    private void setSurfaceRect(int left, int top, int right, int bottom) {
        int padding = SurfaceUtils.dipToPixels(getResources().getDisplayMetrics(), SURFACE_PADDING);
        // TODO: SHOULD CHECK IF THERE IS ENOUGH SURFACE SPACE, BEFORE DOING ANYTHING ELSE...
        mSurfaceRect = new Rect(left + padding, top + padding, right - padding, bottom - padding);
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
        //noinspection PointlessBooleanExpression,ConstantConditions
        if (isDebug() && SHOW_FPS_IN_DEBUG_MODE)
            Log.v(TAG, "FPS:" + fps);
    }

    private void checkCollisions(PointF point) {
        for (int index = mEntities.size() - 1; index >= 0; index--)
            if (mEntities.get(index) instanceof Killable) {
                final Killable killableEntity = (Killable) mEntities.get(index);
                if (killableEntity.collides(point.x, point.y, getThumbRadiusPx()))
                    killableEntity.smash(this, point.x, point.y);
            }
    }

    private void drawEntities(Canvas canvas) {
        for (int index = mEntities.size() - 1; index >= 0; index--)
            mEntities.get(index).draw(canvas, mBitmapManager);
    }

    private void drawDebug(Canvas canvas) {
        if (_debug && mThumbPoint != null)
            canvas.drawCircle(mThumbPoint.x, mThumbPoint.y, getThumbRadiusPx(), mThumbPaint);
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
        int surface;
        boolean debug;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.score = in.readInt();
            this.surface = in.readInt();
            this.debug = in.readInt() == 1;
        }

        @Override
        public void writeToParcel(@NonNull Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.score);
            out.writeInt(this.surface);
            out.writeInt(this.debug ? 1 : 0);
        }
    }

    private class GameLoop extends Thread {

        private final long ONE_SEC_MILLIS = 1000l;
        private final long ONE_SEC_NANOS = (long) Math.pow(ONE_SEC_MILLIS, 3);

        private final long OPTIMAL_TIME = ONE_SEC_NANOS / TARGET_CYCLES_PER_SECOND;

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
            while (!isCancelled()) {
                final long now = System.nanoTime();
                final long updateLength = now - mLastLoopTime;
                mLastLoopTime = now;
                final double delta = updateLength / ((double) OPTIMAL_TIME);

                mLastFpsTime += updateLength;
                mFps++;

                if (mLastFpsTime >= ONE_SEC_NANOS) {
                    updateFPS(mFps);
                    mLastFpsTime = 0;
                    mFps = 0;
                }

                if (mTriggersLogic) {
                    process(delta);
                    postInvalidate();
                }

                try {
                    Thread.sleep((mLastLoopTime - System.nanoTime() + OPTIMAL_TIME) /
                            (ONE_SEC_NANOS / ONE_SEC_MILLIS));
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

        public boolean isPaused() {
            return !mTriggersLogic;
        }

        public boolean isCancelled() {
            return mCancelled;
        }
    }
}