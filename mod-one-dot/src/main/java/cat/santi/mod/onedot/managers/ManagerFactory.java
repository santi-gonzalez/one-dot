package cat.santi.mod.onedot.managers;

import android.content.res.Resources;

import cat.santi.mod.onedot.managers.impl.BitmapManagerImpl;

/**
 *
 */
public class ManagerFactory {

    private static BitmapManager sBitmapManagerInstance;

    private ManagerFactory() {
        // Declare a private constructor to thwart instantiation
    }

    public static BitmapManager getBitmapManager(Resources resources) {
        ensureBitmapManagerInstantiated(resources);
        return sBitmapManagerInstance;
    }

    private static void ensureBitmapManagerInstantiated(Resources resources) {
        if(sBitmapManagerInstance == null)
            sBitmapManagerInstance = new BitmapManagerImpl(resources);
    }
}
