package com.nicecode.android.tender.models;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.nicecode.android.tender.ApplicationWrapper;
import com.nicecode.android.tender.R;
import com.nicecode.android.tender.dto.Filters;
import com.nicecode.android.tender.library.models.ScrollViewData;
import com.nicecode.android.tender.library.utils.LayoutUtils;

import org.w3c.dom.Text;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 21.03.16.
 */

public class ViewData_Filters extends ScrollViewData {

    private Filters mFilter;
    private ApplicationWrapper mApplication;


    public ViewData_Filters(
            ApplicationWrapper application,
            Filters filter,
            int viewId) {
        super(viewId);
        this.mApplication = application;
        this.mFilter = filter;
    }

    @Override
    public View initViews(Context context) {
        final View view = super.initViews(context);

        TextView textView = (TextView) view.findViewById(R.id.test_test);
        textView.setText(this.mFilter.getFilterId().toString());
        return view;
    }

    @Override
    public void clean() {
        synchronized (this) {
            this.view = LayoutUtils.cleanLayout(this.view);
            this.mApplication = null;
            this.mFilter = null;
        }
    }
}
