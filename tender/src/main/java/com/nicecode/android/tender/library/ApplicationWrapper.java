package com.nicecode.android.tender.library;

import android.app.Activity;
import android.app.Application;
import android.content.res.TypedArray;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 18.03.16.
 */

public class ApplicationWrapper extends Application {

    protected boolean initialized;

    protected DisplayMetrics metrics;

    protected int mActionBarSize;

    public void onCreate() {
        super.onCreate();
        this.initialized = false;
    }

    public void init(Activity activity) {
        if (!initialized) {

            this.metrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(this.metrics);

            final TypedArray styledAttributes = getTheme().obtainStyledAttributes(
                    new int[]{android.R.attr.actionBarSize});
            this.mActionBarSize = (int) styledAttributes.getDimension(0, 0);
            styledAttributes.recycle();

        }
    }

    @SuppressWarnings("unused")
    public DisplayMetrics getMetrics() {
        return metrics;
    }

    public int getActionBarSize() {
        return mActionBarSize;
    }

    public float getFloatResource(int resourceId) {
        TypedValue outValue = new TypedValue();
        getResources().getValue(resourceId, outValue, true);
        return outValue.getFloat();
    }

}
