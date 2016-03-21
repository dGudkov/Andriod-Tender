package com.nicecode.android.tender.activity;

import android.os.Bundle;

import com.nicecode.android.tender.ApplicationWrapper;
import com.nicecode.android.tender.library.widget.ToolBarFragmentActivity;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 18.03.16.
 */

public abstract class BaseActivity extends ToolBarFragmentActivity {

    protected ApplicationWrapper mApplication;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mApplication = (ApplicationWrapper) this.getApplication();
    }
    @Override
    protected void onDestroy() {
        this.mApplication = null;
        super.onDestroy();
    }




}
