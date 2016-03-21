package com.nicecode.android.tender.library.models;

import android.content.Context;
import android.view.View;

import com.nicecode.android.tender.library.interfaces.IScrollViewData;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2016
 * @since 21.03.16.
 */

public abstract class ScrollViewData implements IScrollViewData {

    protected View view;
    protected int viewId;
    protected Context mContext;


    public ScrollViewData(int viewId) {
        this.viewId = viewId;
    }

    @Override
    public View initViews(Context context) {
        this.mContext = context;
        if (this.view == null) {
            this.view = inflateView();
        }
        if (this.view != null) {
            this.view.setTag(this);
        }
        return this.view;
    }

    @Override
    public View cloneView() {
        return inflateView();
    }

    private View inflateView() {
        return View.inflate(this.mContext, this.viewId, null);
    }
}
