package com.nicecode.android.tender.library.widget;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.nicecode.android.tender.library.exception.ApplicationException;
import com.nicecode.android.tender.library.interfaces.IToolBarInterface;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 18.03.16.
 */

public abstract class ToolBarFragmentActivity extends BaseActivity
        implements
        View.OnClickListener,
        IToolBarInterface {

    protected Toolbar mToolBar;
    protected TextView mTitle;
    //    protected BaseMenuFragment<I> mMenuFragment;
    protected BaseFragment mContent;
//    protected boolean mPerformBackToMainScreen = true;

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());

        this.mToolBar = (Toolbar) findViewById(getToolbarId());
        if (this.mToolBar == null) {
            throw new ApplicationException("Error toolbar initialization.");
        }
        setSupportActionBar(this.mToolBar);

        View view = this.mToolBar.findViewById(getMenuLayoutId());
        if (view != null) {
            view.setOnClickListener(this);
        }

        view = this.mToolBar.findViewById(getMenuId());
        if (view != null) {
            view.setOnClickListener(this);
        }
        this.mToolBar.setContentInsetsAbsolute(0, 0);

        this.mTitle = (TextView) this.mToolBar.findViewById(getTitleId());
        if (this.mTitle != null) {
            this.mTitle.setTextSize(24);
        }

        if (savedInstanceState != null) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag("mContent");
            if (fragment != null) {
                if (fragment instanceof BaseFragment) {
                    BaseFragment baseFragment = (BaseFragment) fragment;
                    baseFragment.init(this, this.mToolBar, this, null);
                    this.mContent = baseFragment;
                }
            } else {
                displayView(getHomeFragmentId());
            }
        } else {
            displayView(getHomeFragmentId());
        }
    }

    @Override
    protected void onDestroy() {
        this.mToolBar = null;
        this.mTitle = null;
        super.onDestroy();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) { // если нажата кнопка "Назад"
            if (this.mContent == null) {
                finish();
            } else {
                int returnFragmentId = this.mContent.getReturnFragmentId();
                if (returnFragmentId != BaseFragment.DEFAULT_RETURN_FRAGMENT_ID) {
                    displayView(returnFragmentId);
                } else {
                    finish();
                }
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void displayView(int position) {
        switchView(getFragment(position));
    }

    protected void switchView(BaseFragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(getContainerId(), fragment, "mContent")
                    .commit();
            this.mContent = fragment;
        }
    }

    @Override
    public void onToolBarClick(View v) {
        Toast.makeText(this, "ToolBar component clicked.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setToolBarTitle(String title) {
        if (title != null) {
            this.mTitle.setText(Html.fromHtml(title));
        }
    }


    @Override
    public void onClick(View v) {
    }

    @SuppressWarnings("unused")
    public Toolbar getToolBar() {
        return mToolBar;
    }

    public BaseFragment getContent() {
        return mContent;
    }

    protected abstract int getContentViewId();

    protected abstract int getToolbarId();

    protected abstract int getMenuLayoutId();

    protected abstract int getMenuId();

    protected abstract int getTitleId();

    protected abstract int getContainerId();

    protected abstract int getHomeFragmentId();

    protected abstract BaseFragment getFragment(int position);

}
