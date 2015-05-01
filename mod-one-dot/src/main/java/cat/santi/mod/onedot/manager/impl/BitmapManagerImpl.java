package cat.santi.mod.onedot.manager.impl;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.List;

import cat.santi.mod.onedot.R;
import cat.santi.mod.onedot.manager.BitmapManager;

/**
 *
 */
public class BitmapManagerImpl implements
        BitmapManager {

    private List<Bitmap> mBitmaps;

    public BitmapManagerImpl(Resources resources) {
        mBitmaps = new ArrayList<>();
        init(resources);
    }

    @Override
    public Bitmap get(int bitmapIndex) {
        try {
            return mBitmaps.get(bitmapIndex);
        } catch (Exception ex) {
            throw new IllegalArgumentException("bitmap index [" + bitmapIndex + "] not supported");
        }
    }

    private void init(Resources resources) {
        mBitmaps.add(BitmapFactory.decodeResource(resources, R.drawable.od__skull));
    }
}
