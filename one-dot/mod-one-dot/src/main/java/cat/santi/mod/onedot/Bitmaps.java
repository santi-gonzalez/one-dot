package cat.santi.mod.onedot;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Bitmaps {

    public static final int BITMAP_SKULL = 0;

    private List<Bitmap> mBitmaps;

    private static Bitmaps sInstance = null;

    private Bitmaps(Resources resources) {
        mBitmaps = new ArrayList<>();
        init(resources);
    }

    public static synchronized Bitmaps getInstance(Resources resources) {
        if(sInstance == null)
            sInstance = new Bitmaps(resources);
        return sInstance;
    }

    public Bitmap get(int bitmap) {
        try {
            return mBitmaps.get(bitmap);
        } catch (Exception ex) {
            throw new IllegalArgumentException("bitmap index [" + bitmap + "] not supported");
        }
    }

    private void init(Resources resources) {
        mBitmaps.add(BitmapFactory.decodeResource(resources, R.drawable.od__skull));
    }
}
