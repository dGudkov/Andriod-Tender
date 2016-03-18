package com.nicecode.android.tender.activity;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nicecode.android.tender.ApplicationWrapper;
import com.nicecode.android.tender.R;
import com.nicecode.android.tender.library.async.AsyncTask;
import com.nicecode.android.tender.library.exception.ApplicationException;
import com.nicecode.android.tender.library.utils.LayoutUtils;
import com.nicecode.android.tender.library.utils.WeakReference;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2016
 * @since 18.03.16.
 */

public class Activity_Splash extends AppCompatActivity {

    protected ApplicationWrapper mApplication;
    private View mRootView;
    private TextView mVersionText, mAppText;

    private LoadApplicationTask mLoadApplicationTask;
    private RelativeLayout mImageLayout;
    private View mDelimiter;
    private ImageView mKitImage, mLogoImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_layout);
        try {
            this.mRootView = findViewById(R.id.activity_splash_layout);
            this.mLogoImage = (ImageView) findViewById(R.id.activity_splash_logo);
            this.mKitImage = (ImageView) findViewById(R.id.activity_splash_kit);
            this.mDelimiter = findViewById(R.id.activity_splash_delimiter);
            this.mVersionText = (TextView) findViewById(R.id.activity_splash_version_text);
            this.mAppText = (TextView) findViewById(R.id.activity_splash_app_text);
            this.mImageLayout = (RelativeLayout) findViewById(R.id.activity_splash_image_layout);
            this.mApplication = (ApplicationWrapper) this.getApplication();
            this.mApplication.init(this);
            this.intiLayout();
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
    }

    private void intiLayout() {
        int width = this.mApplication.getMetrics().widthPixels;
        int height = this.mApplication.getMetrics().heightPixels;

        RelativeLayout.LayoutParams rLp = (RelativeLayout.LayoutParams) this.mDelimiter.getLayoutParams();
        rLp.width = (int) (width * this.mApplication.ACTIVITY_SPLASH_DELIMITER_WIDTH_RATIO);

        rLp = (RelativeLayout.LayoutParams) this.mLogoImage.getLayoutParams();
        rLp.width = (int) (width * this.mApplication.ACTIVITY_SPLASH_LOGO_IMAGE_WIDTH_RATIO);
        rLp.height = (int) (height * this.mApplication.ACTIVITY_SPLASH_LOGO_IMAGE_HEIGHT_RATIO);

        rLp = (RelativeLayout.LayoutParams) this.mKitImage.getLayoutParams();
        rLp.width = (int) (width * this.mApplication.ACTIVITY_SPLASH_KIT_IMAGE_WIDTH_RATIO);
        rLp.height = (int) (height * this.mApplication.ACTIVITY_SPLASH_KIT_IMAGE_HEIGHT_RATIO);

        this.mVersionText.setTextSize(this.mApplication.ACTIVITY_SPLASH_TEXT_VERSION_SIZE);
        this.mAppText.setTextSize(this.mApplication.ACTIVITY_SPLASH_TEXT_APP_SIZE);
        this.mImageLayout.getLayoutParams().height = (int) (
                height *
                        this.mApplication.ACTIVITY_SPLASH_IMAGE_LAYOUT_HEIGHT_RATIO
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.mApplication = (ApplicationWrapper) this.getApplication();
        String version;
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = getResources().getString(R.string.str_activity_splash_version_text) + " @ 2016 V-" + pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            version = getResources().getString(R.string.str_activity_splash_version_text) + " @ unknown.";
        }
        this.mVersionText.setText(version);

        LayoutUtils.loadImage(this.mLogoImage, R.drawable.logo, R.drawable.logo);
        LayoutUtils.loadImage(this.mKitImage, R.drawable.kit, R.drawable.kit);

        this.mLoadApplicationTask = new LoadApplicationTask(this.mApplication, this);
        this.mLoadApplicationTask.execute();
    }

    @Override
    protected void onPause() {
        if (this.mLoadApplicationTask != null) {
            this.mLoadApplicationTask.interrupt(true);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        this.mRootView = LayoutUtils.cleanLayout(this.mRootView);
        this.mVersionText = null;
        this.mImageLayout = null;
        super.onDestroy();
        this.mApplication = null;
    }

    private class LoadApplicationTask extends AsyncTask<Void, String, Void> {
        private WeakReference<ApplicationWrapper> mApplicationWrapper;
        private WeakReference<Activity_Splash> mActivity;

        private long mStartTime;

        public LoadApplicationTask(ApplicationWrapper applicationWrapper, Activity_Splash activity) {
            this.mActivity = new WeakReference<>(activity);
            this.mApplicationWrapper = new WeakReference<>(applicationWrapper);
        }

        @Override
        protected void onPreExecute() {
            mStartTime = System.currentTimeMillis();
        }

        @Override
        protected Void doInBackground(Void... params) {
            ApplicationWrapper application = this.mApplicationWrapper.get();
            Activity_Splash activity = this.mActivity.get();

            if ((application != null) && (activity != null)) {

                try {
                    this.stopIfCancelled();
                    final long totalTime = System.currentTimeMillis() - mStartTime;
                    if (totalTime < application.ACTIVITY_SPLASH_TIME) {
                        try {
                            Thread.sleep(application.ACTIVITY_SPLASH_TIME - totalTime);
                        } catch (InterruptedException ignored) {
                        }
                    }

                    this.stopIfCancelled();

                } catch (Exception e) {
                    publishProgress("Server error...");
                    Log.e("Activity_Splash", "Error: " + e.getMessage());
                }
            }
            return null;
        }

        protected void onProgressUpdate(String... progress) {
            Activity activity = this.mActivity.get();
            if (activity != null) {
                Toast.makeText(activity, progress[0], Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Activity activity = this.mActivity.get();
            try {
                ApplicationWrapper application = this.mApplicationWrapper.get();
//                if ((application != null) && (activity != null)) {
//                    Intent intent;//
//                    if (application.getPreferences().getWalkThroughtEnabled()) {
//                        intent = new Intent(activity, Activity_WalkThrought.class);
//                    } else {
//                        intent = new Intent(activity, Activity_UserLogin.class);
//                    }
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
//                }
            } finally {
                super.onPostExecute(aVoid);
//                if ((activity != null) && (!activity.isFinishing())) {
//                    activity.finish();
//                }
            }
        }

        @Override
        protected void finalizeTask() {
            if (this.mActivity != null) {
                Activity_Splash activity = this.mActivity.get();
                if (activity != null) {
                    activity.mLoadApplicationTask = null;
                    this.mActivity = null;
                }
            }
            this.mApplicationWrapper = null;
        }
    }
}
