package com.nicecode.android.tender.activity;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class Activity_Splash extends AppCompatActivity implements View.OnClickListener {

    protected ApplicationWrapper mApplication;
    private View mRootView;
    private TextView mVersionText, mAppText;

    private RelativeLayout mImageLayout;
    private View mDelimiter;
    private ImageView mKitImage, mLogoImage;
    private LoadApplicationTask mLoadApplicationTask;
    private LinearLayout mButtonLayout;
    private RelativeLayout mLoginLayout, mRegisterLayout;
    private TextView mLoginText, mRegisterText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_layout);
        try {
            this.mApplication = (ApplicationWrapper) this.getApplication();
            this.mApplication.init(this);
            this.mRootView = findViewById(R.id.activity_splash_layout);
            this.mLogoImage = (ImageView) findViewById(R.id.activity_splash_logo);
            this.mKitImage = (ImageView) findViewById(R.id.activity_splash_kit);
            this.mDelimiter = findViewById(R.id.activity_splash_delimiter);
            this.mVersionText = (TextView) findViewById(R.id.activity_splash_version_text);
            this.mAppText = (TextView) findViewById(R.id.activity_splash_app_text);
            this.mImageLayout = (RelativeLayout) findViewById(R.id.activity_splash_image_layout);
            this.mButtonLayout = (LinearLayout) findViewById(R.id.activity_splash_button_layout);
            this.mLoginLayout = (RelativeLayout) findViewById(R.id.activity_splash_login_button);
            this.mRegisterLayout = (RelativeLayout) findViewById(R.id.activity_splash_register_button);

            this.mLoginText = (TextView) this.mLoginLayout.findViewById(R.id.widget_login_button_text);
            this.mRegisterText = (TextView) this.mRegisterLayout.findViewById(R.id.widget_login_button_text);

            this.mLoginLayout.setOnClickListener(this);
            this.mRegisterLayout.setOnClickListener(this);

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

        rLp = (RelativeLayout.LayoutParams) this.mLoginLayout.getLayoutParams();
        rLp.height = (int) (
                height *
                        this.mApplication.ACTIVITY_SPLASH_LOGIN_LAYOUT_HEIGHT_RATIO
        );
        rLp.leftMargin = rLp.rightMargin = (int) (
                height *
                        this.mApplication.ACTIVITY_SPLASH_LOGIN_BUTTON_MARGIN_RATIO
        );

        rLp = (RelativeLayout.LayoutParams) this.mRegisterLayout.getLayoutParams();
        rLp.height = (int) (
                height *
                        this.mApplication.ACTIVITY_SPLASH_REGISTER_LAYOUT_HEIGHT_RATIO
        );
        rLp.leftMargin = rLp.rightMargin = (int) (
                height *
                        this.mApplication.ACTIVITY_SPLASH_LOGIN_BUTTON_MARGIN_RATIO
        );

        this.mLoginText.setText(getString(R.string.str_activity_splash_login_text));
        this.mRegisterText.setText(getString(R.string.str_activity_splash_register_text));

    }

    @Override
    protected void onResume() {
        super.onResume();
        this.mApplication = (ApplicationWrapper) this.getApplication();
        this.mButtonLayout.setVisibility(View.INVISIBLE);
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
        this.mAppText = null;
        this.mImageLayout = null;
        this.mDelimiter = null;
        this.mKitImage = null;
        this.mLogoImage = null;
        this.mButtonLayout = null;
        this.mLoginLayout = null;
        this.mRegisterLayout = null;
        this.mLoginText = null;
        this.mRegisterText = null;
        this.mLoadApplicationTask = null;
        super.onDestroy();
        this.mApplication = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_splash_login_button:
                Snackbar.make(view, "This functionality not implemented now.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
            case R.id.activity_splash_register_button:
                Snackbar.make(view, "This functionality not implemented now.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
        }

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
            Activity_Splash activity = this.mActivity.get();
            try {
                ApplicationWrapper application = this.mApplicationWrapper.get();
                if (activity != null) {
                    LinearLayout layout = activity.mButtonLayout;
                    if (layout != null) {
                        layout.setVisibility(View.VISIBLE);
                    }
                }
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
