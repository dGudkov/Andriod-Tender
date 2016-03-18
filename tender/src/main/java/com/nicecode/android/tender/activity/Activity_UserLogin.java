package com.nicecode.android.tender.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nicecode.android.tender.R;
import com.nicecode.android.tender.fragments.BaseFragment;
import com.nicecode.android.tender.fragments.Fragment_User_Login;
import com.nicecode.android.tender.library.utils.LayoutUtils;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2016
 * @since 18.03.16.
 */

public class Activity_UserLogin extends BaseActivity {

    public static final int LOGIN_FRAGMENT = 10;

    private View mRootView;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_userlogin_layout;
    }

    @Override
    protected int getToolbarId() {
        return R.id.activity_userlogin_toolbar;
    }

    @Override
    protected int getMenuLayoutId() {
        return R.id.activity_userlogin_status_bar_back_layout;
    }

    @Override
    protected int getMenuId() {
        return R.id.activity_userlogin_status_bar_back;
    }

    @Override
    protected int getTitleId() {
        return R.id.activity_userlogin_header_title;
    }

    @Override
    protected int getContainerId() {
        return R.id.activity_userlogin_container_body;
    }

    @Override
    protected int getHomeFragmentId() {
        return LOGIN_FRAGMENT;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        this.mRootView = findViewById(R.id.activity_userlogin_drawer_layout);

        View view = this.mRootView.findViewById(R.id.activity_userlogin_status_bar_back_layout);
        RelativeLayout.LayoutParams  rLp = (RelativeLayout.LayoutParams) view.getLayoutParams();
        rLp.leftMargin = this.mApplication.STATUS_BAR_LEFT_MARGIN;
        view.setOnClickListener(this);

        ImageView image = (ImageView) this.mRootView.findViewById(R.id.activity_userlogin_status_bar_back);
        ViewGroup.LayoutParams lp = image.getLayoutParams();
        lp.width = lp.height = this.mApplication.STATUS_BAR_IMAGE_SIZE;
        image.setOnClickListener(this);

        this.mTitle = ((TextView) this.mRootView.findViewById(R.id.activity_userlogin_header_title));
        this.mTitle.setTextSize(this.mApplication.STATUS_BAR_TEXT_SIZE);

        view = this.mRootView.findViewById(R.id.activity_userlogin_status_bar_more_layout);
        rLp = (RelativeLayout.LayoutParams) view.getLayoutParams();
        rLp.rightMargin= this.mApplication.STATUS_BAR_RIGHT_MARGIN;
        view.setOnClickListener(this);

        TextView text = ((TextView) this.mRootView.findViewById(R.id.activity_userlogin_status_bar_more));
        text.setTextSize(this.mApplication.STATUS_BAR_TEXT_SIZE);


        LayoutUtils.loadImage(image, R.drawable.back_button, R.drawable.back_button);
    }


    @Override
    protected BaseFragment getFragment(int position) {
        return BaseFragment.newInstance(
                Fragment_User_Login.class,
                this,
                getString(R.string.str_fragment_user_login_title)
        );
    }

    @Override
    public void displayView(int position) {
        super.displayView(position);
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        this.mRootView = LayoutUtils.cleanLayout(this.mRootView);
        super.onDestroy();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onClick(View v) {

    }
}