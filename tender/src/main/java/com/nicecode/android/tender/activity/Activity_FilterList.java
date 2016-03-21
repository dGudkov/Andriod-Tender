package com.nicecode.android.tender.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nicecode.android.tender.R;
import com.nicecode.android.tender.fragments.BaseFragment;
import com.nicecode.android.tender.fragments.Fragment_FilterList;
import com.nicecode.android.tender.library.utils.LayoutUtils;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 01.02.16.
 */

public class Activity_FilterList extends BaseActivity {

    public static final int FRAGMENT_FILTER_LIST = 10;

    private View mRootView;
    private ImageView mStatusBarSearchImage;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_filterlist_layout;
    }

    @Override
    protected int getToolbarId() {
        return R.id.activity_filterlist_toolbar;
    }

    @Override
    protected int getMenuLayoutId() {
        return R.id.activity_filterlist_status_bar_menu_layout;
    }

    @Override
    protected int getMenuId() {
        return R.id.activity_filterlist_status_bar_menu;
    }

    @Override
    protected int getTitleId() {
        return R.id.activity_filterlist_header_title;
    }

    @Override
    protected int getContainerId() {
        return R.id.activity_filterlist_container_body;
    }

    @Override
    protected int getHomeFragmentId() {
        return FRAGMENT_FILTER_LIST;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mRootView = findViewById(R.id.activity_filterlist_drawer_layout);

        RelativeLayout.LayoutParams rLp = (RelativeLayout.LayoutParams) this.mRootView.findViewById(R.id.activity_filterlist_status_bar_menu_layout).getLayoutParams();
        rLp.leftMargin = this.mApplication.STATUS_BAR_LEFT_MARGIN;
        this.mRootView.findViewById(R.id.activity_filterlist_status_bar_menu_layout).setOnClickListener(this);

        ImageView image = (ImageView) this.mRootView.findViewById(R.id.activity_filterlist_status_bar_menu);
        image.setOnClickListener(this);
        ViewGroup.LayoutParams lp = image.getLayoutParams();
        lp.width = lp.height = this.mApplication.STATUS_BAR_IMAGE_SIZE;

        this.mStatusBarSearchImage = (ImageView) this.mRootView.findViewById(R.id.activity_filterlist_status_bar_search);
        lp = this.mStatusBarSearchImage.getLayoutParams();
        lp.width = lp.height = this.mApplication.STATUS_BAR_IMAGE_SIZE;

        this.mTitle = ((TextView) this.mRootView.findViewById(R.id.activity_filterlist_header_title));
        this.mTitle.setTextSize(this.mApplication.STATUS_BAR_TEXT_SIZE);

        LayoutUtils.loadImage(image, R.drawable.menu, R.drawable.menu);
        LayoutUtils.loadImage(this.mStatusBarSearchImage, R.drawable.search, R.drawable.search);
    }

    @Override
    protected BaseFragment getFragment(int position) {
        BaseFragment fragment = null;

        switch (position) {
            case FRAGMENT_FILTER_LIST:
                fragment = BaseFragment.newInstance(
                        Fragment_FilterList.class,
                        this,
                        getString(R.string.str_fragment_filter_list_title)
                );
                break;

        }
        return fragment;
    }

    @Override
    public void displayView(int position) {
        super.displayView(position);
    }

    @Override
    public void onToolBarClick(View v) {
//        switch (v.getId()) {
//            case R.id.activity_getphoto_status_bar_back_layout:
//            case R.id.activity_getphoto_status_bar_back:
//                if (!this.isFinishing()) {
//                    this.finish();
//                }
//                break;
//            default:
//                super.onToolBarClick(v);
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();

//        User user = this.mApplication.getPreferences().getUser();
//        if (!((user != null) && (user.getSessionId() != null))) {
//            Intent intent = new Intent(this, Activity_UserLogin.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//            finish();
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        this.mRootView = LayoutUtils.cleanLayout(this.mRootView);
        this.mStatusBarSearchImage = null;
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }
}
