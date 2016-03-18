package com.nicecode.android.tender.fragments;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.nicecode.android.tender.ApplicationWrapper;
import com.nicecode.android.tender.library.exception.ApplicationException;
import com.nicecode.android.tender.library.interfaces.IToolBarInterface;
import com.nicecode.android.tender.library.widget.ToolBarFragmentActivity;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2016
 * @since 18.03.16.
 */

public abstract class BaseFragment extends com.nicecode.android.tender.library.widget.BaseFragment {

    protected final static String INITIALIZATION_ERROR = "Error fragment '%s' initialization";
    protected ApplicationWrapper mApplication;

    public BaseFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static BaseFragment newInstance(Class<? extends BaseFragment> clazz,
                                             ToolBarFragmentActivity activity,
                                             String titleText) throws ApplicationException {
        BaseFragment fragment = null;
        try {
            fragment = clazz.newInstance();
            fragment.init(
                    activity,
                    activity.getToolBar(),
                    activity,
                    titleText);
        } catch (Exception e) {
            throwException(INITIALIZATION_ERROR, clazz.getName());
        }
        return fragment;
    }

    @Override
    public void init(Activity activity, Toolbar toolBar, IToolBarInterface onToolBarClickListener,
                     String titleText) {
        super.init(activity, toolBar, onToolBarClickListener, titleText);
        this.mApplication = (ApplicationWrapper) activity.getApplication();
    }

    @Override
    public void onDestroy() {
        this.mApplication = null;
        super.onDestroy();
    }

    @SuppressWarnings("unused")
    protected DisplayMetrics getMetrics() {
        return (this.mApplication != null) ? this.mApplication.getMetrics() : null;
    }

    @SuppressWarnings("unused")
    protected void setImeVisibility(final View v, final boolean visible) {
        final InputMethodManager imm = (InputMethodManager) getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            v.post(new Runnable() {
                @Override
                public void run() {
                    if (visible) {
                        imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
                    } else {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
            });
        }
    }


}
