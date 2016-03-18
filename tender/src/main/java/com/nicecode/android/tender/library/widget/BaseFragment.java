package com.nicecode.android.tender.library.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nicecode.android.tender.library.exception.ApplicationException;
import com.nicecode.android.tender.library.interfaces.IToolBarInterface;
import com.nicecode.android.tender.library.utils.LayoutUtils;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 18.03.16.

 */

public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    public static final int DEFAULT_RETURN_FRAGMENT_ID = -1;
    protected final static String LAYOUT_INITIALIZATION_ERROR = "Error layout initialization in %s";

    protected final String TITLE = "title";

    protected int mLayoutId = DEFAULT_RETURN_FRAGMENT_ID;
    protected Context mContext;
    protected String mTitleText;
    protected Toolbar mToolBar;
    protected IToolBarInterface mToolBarInterface;
    protected int mReturnFragmentId;
    protected View mRootView;

    public BaseFragment() {
        this.mReturnFragmentId = DEFAULT_RETURN_FRAGMENT_ID;
        this.mTitleText = null;
    }

    @SuppressWarnings("unused")
    public static BaseFragment newInstance(Class<? extends BaseFragment> clazz,
                                           Activity activity,
                                           Toolbar toolBar,
                                           IToolBarInterface onToolBarClickListener,
                                           String titleText,
                                           int returnFragmentId) throws ApplicationException {
        try {
            BaseFragment fragment = clazz.newInstance();
            fragment.init(activity, toolBar, onToolBarClickListener, titleText);
            return fragment;
        } catch (Exception e) {
            throw new ApplicationException("Error fragment initialization", e);
        }
    }

    public void init(Activity activity, Toolbar toolBar, IToolBarInterface onToolBarClickListener,
                     String titleText) {
        this.setContext(activity.getBaseContext());
        this.setToolBar(toolBar);
        this.setToolBarInterface(onToolBarClickListener);
        this.setTitleText(titleText);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (this.mLayoutId == -1) {
            throw new ApplicationException("Invalid layoutId");
        }
        this.mRootView = inflater.inflate(this.mLayoutId, container, false);

        if (savedInstanceState != null) {
            this.mTitleText = savedInstanceState.getString(TITLE);
        }

        this.setToolBarTitle();

        if (this.mRootView != null) {
            this.initView(this.mRootView);
        } else {
            throwException(LAYOUT_INITIALIZATION_ERROR, this.getClass().getName());
        }
        return this.mRootView;
    }

    protected abstract void initView(View rootView);

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TITLE, this.mTitleText);
    }

    @Override
    public void onDestroy() {
        this.mLayoutId = DEFAULT_RETURN_FRAGMENT_ID;
        this.mContext = null;
        this.mTitleText = null;
        this.mToolBar = null;
        this.mToolBarInterface = null;
        this.mReturnFragmentId = DEFAULT_RETURN_FRAGMENT_ID;
        this.mRootView = LayoutUtils.cleanLayout(this.mRootView);
        super.onDestroy();
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    public void setToolBar(Toolbar toolBar) {
        this.mToolBar = toolBar;
    }

    public void setToolBarInterface(IToolBarInterface toolBarInterface) {
        this.mToolBarInterface = toolBarInterface;
    }

    protected void onToolBarClick(View view) {
        if (this.mToolBarInterface != null) {
            this.mToolBarInterface.onToolBarClick(view);
        }
    }

    @Override
    public void onClick(View v) {
        if (isToolBarComponent(v)) {
            this.onToolBarClick(v);
        }
    }

    protected boolean isToolBarComponent(View v) {
        return false;
    }

    public void setTitleText(String titleText) {
        this.mTitleText = titleText;
        if (this.mTitleText != null) {
            this.setToolBarTitle(this.mTitleText);
        }
    }

    protected void setToolBarTitle() {
        if (this.mTitleText != null) {
            this.setToolBarTitle(this.mTitleText);
        }
    }

    protected void setToolBarTitle(String title) {
        if (this.mToolBarInterface != null) {
            this.mToolBarInterface.setToolBarTitle(title);
        }
    }

    @SuppressWarnings("unused")
    public int getReturnFragmentId() {
        return this.mReturnFragmentId;
    }

    @SuppressWarnings("unused")
    public void setReturnFragmentId(int returnFragmentId) {
        this.mReturnFragmentId = returnFragmentId;
    }

    protected static void throwException(String text, String className) {
        throwException(text, className, null);
    }

    protected static void throwException(String text, String className, Throwable t) {
        throw new ApplicationException(String.format(text, className), t);
    }

}
