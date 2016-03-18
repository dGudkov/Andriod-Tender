package com.nicecode.android.tender;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;

import com.nicecode.android.tender.library.Constants;
import com.nicecode.android.tender.preference.Preferences;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2016
 * @since 18.03.16.
 */

public class ApplicationWrapper extends com.nicecode.android.tender.library.ApplicationWrapper {

    private Preferences preferences;

    // ===========================================================================
    // Status Bar
    // ===========================================================================
//    public int STATUS_BAR_LEFT_MARGIN;
//    public int STATUS_BAR_RIGHT_MARGIN;
//    public int STATUS_BAR_IMAGE_SIZE;
//    public float STATUS_BAR_TEXT_SIZE;
//    public float STATUS_IMAGE_SIZE;
//    public int STATUS_SELECTOR_WIDTH;

    // ===========================================================================
    // Menu
    // ===========================================================================
//    public int MENU_WIDTH;
//    public int MENU_ITEM_HEIGHT;
//    public float MENU_ITEM_TEXT_SIZE;
//
    // ===========================================================================
    // Splash activity
    // ===========================================================================
    public final int ACTIVITY_SPLASH_TIME = 5000;
    public float ACTIVITY_SPLASH_TEXT_VERSION_SIZE;
    public float ACTIVITY_SPLASH_TEXT_APP_SIZE;
    public float ACTIVITY_SPLASH_IMAGE_LAYOUT_HEIGHT_RATIO;
    public float ACTIVITY_SPLASH_DELIMITER_HEIGHT;
    public float ACTIVITY_SPLASH_DELIMITER_WIDTH_RATIO;
    public float ACTIVITY_SPLASH_LOGO_IMAGE_WIDTH_RATIO;
    public float ACTIVITY_SPLASH_LOGO_IMAGE_HEIGHT_RATIO;
    public float ACTIVITY_SPLASH_KIT_IMAGE_WIDTH_RATIO;
    public float ACTIVITY_SPLASH_KIT_IMAGE_HEIGHT_RATIO;

    // ===========================================================================
    // Login fragment
    // ===========================================================================
//    public float FRAGMENT_LOGIN_LOGO_WIDTH_RATIO;
//    public float FRAGMENT_LOGIN_IMAGES_SIZE_RATIO;
//    public float FRAGMENT_LOGIN_EDIT_TEXT_SIZE;
//    public float FRAGMENT_LOGIN_FORGOT_TEXT_SIZE;
//    public float FRAGMENT_LOGIN_REMEMBER_TEXT_SIZE;
//    public float FRAGMENT_LOGIN_REGISTRATION_TEXT_SIZE;
//    public float FRAGMENT_LOGIN_BUTTON_LOGIN_TEXT_SIZE;
//    public float FRAGMENT_LOGIN_ERROR_TEXT_SIZE;

    @Override
    public void onCreate() {
        if (Constants.Config.DEVELOPER_MODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());
        }
        super.onCreate();
        initImageLoader(getApplicationContext());
        this.preferences = new Preferences(this);
    }

    @Override
    public void init(Activity activity) {
        if (!initialized) {
            super.init(activity);

            // ===========================================================================
            // Status Bar
            // ===========================================================================
//            STATUS_BAR_LEFT_MARGIN = (int) (this.metrics.widthPixels *
//                    this.getFloatResource(R.dimen.fl_status_bar_left_margin_ratio));
//
//            STATUS_BAR_RIGHT_MARGIN = (int) (this.metrics.widthPixels *
//                    this.getFloatResource(R.dimen.fl_status_bar_right_margin_ratio));
//
//            STATUS_BAR_IMAGE_SIZE = (int) (this.mActionBarSize *
//                    this.getFloatResource(R.dimen.fl_status_bar_image_size_ratio));
//
//            STATUS_BAR_TEXT_SIZE = this.mActionBarSize *
//                    this.getFloatResource(R.dimen.fl_status_bar_text_size_ratio)
//                    / this.metrics.scaledDensity;
//
//            STATUS_IMAGE_SIZE = this.getFloatResource(R.dimen.fl_status_bar_image_size_ratio);
//            STATUS_SELECTOR_WIDTH = this.mActionBarSize;

            // ===========================================================================
            // Menu
            // ===========================================================================
//            MENU_WIDTH = (int) (this.metrics.widthPixels *
//                    getFloatResource(R.dimen.fl_menu_width_ratio));
//
//            MENU_ITEM_HEIGHT = (int) (this.metrics.heightPixels *
//                    getFloatResource(R.dimen.fl_menu_item_height_ratio));
//
//            MENU_ITEM_TEXT_SIZE = (MENU_ITEM_HEIGHT *
//                    getFloatResource(R.dimen.fl_menu_text_size_ratio)) / this.metrics.scaledDensity;

            // ===========================================================================
            // Splash activity
            // ===========================================================================
            ACTIVITY_SPLASH_TEXT_VERSION_SIZE = ((this.metrics.heightPixels *
                    getFloatResource(R.dimen.fl_activity_splash_text_version_size))
                    / this.metrics.scaledDensity);

            ACTIVITY_SPLASH_IMAGE_LAYOUT_HEIGHT_RATIO = getFloatResource(R.dimen.fl_activity_splash_image_layout_height_ratio);

            ACTIVITY_SPLASH_TEXT_APP_SIZE = ((this.metrics.heightPixels *
                    getFloatResource(R.dimen.fl_activity_splash_text_app_size))
                    / this.metrics.scaledDensity);

            ACTIVITY_SPLASH_DELIMITER_HEIGHT = getFloatResource(R.dimen.fl_activity_splash_delimiter_height);
            ACTIVITY_SPLASH_DELIMITER_WIDTH_RATIO = getFloatResource(R.dimen.fl_activity_splash_delimiter_width_ratio);

            ACTIVITY_SPLASH_LOGO_IMAGE_WIDTH_RATIO = getFloatResource(R.dimen.fl_activity_splash_logo_image_width_ratio);
            ACTIVITY_SPLASH_LOGO_IMAGE_HEIGHT_RATIO = getFloatResource(R.dimen.fl_activity_splash_logo_image_height_ratio);

            ACTIVITY_SPLASH_KIT_IMAGE_WIDTH_RATIO = getFloatResource(R.dimen.fl_activity_splash_kit_image_width_ratio);
            ACTIVITY_SPLASH_KIT_IMAGE_HEIGHT_RATIO = getFloatResource(R.dimen.fl_activity_splash_kit_image_height_ratio);


            initialized = true;
        }
    }

    public Preferences getPreferences() {
        return preferences;
    }

    private static void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        ImageLoader.getInstance().init(config.build());
    }

}
