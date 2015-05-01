package cat.santi.mod.onedot.manager;

import android.graphics.Bitmap;

/**
 * Created by Santiago on 01/05/2015.
 */
public interface BitmapManager {

    int BMP_INDEX_SKULL = 0;

    Bitmap get(int bitmapIndex);
}
