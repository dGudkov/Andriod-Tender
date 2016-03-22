package com.nicecode.android.tender.models;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nicecode.android.tender.ApplicationWrapper;
import com.nicecode.android.tender.R;
import com.nicecode.android.tender.dto.Filters;
import com.nicecode.android.tender.library.models.ScrollViewData;
import com.nicecode.android.tender.library.utils.LayoutUtils;
import com.nicecode.android.tender.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 21.03.16.
 */

public class ViewData_Filters extends ScrollViewData {

    private static final  SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

    private Filters mFilter;
    private ApplicationWrapper mApplication;
    private TextView mComment;
    private View mCheckBoxLayout;
    private ImageView mCheckBox;

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

//        TextView textView = (TextView) view.findViewById(R.id.test_test);
//        textView.setText(this.mFilter.getFilterId().toString());

        RelativeLayout.LayoutParams rLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        rLp.height = (int) this.mApplication.FRAGMENT_FILTER_LIST_CARD_HEIGHT;
        rLp.leftMargin = rLp.rightMargin = 10;
        view.setLayoutParams(rLp);

        this.mCheckBoxLayout = view.findViewById(R.id.widget_card_layout_checkBox_layout);
        rLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        rLp.width = (int) this.mApplication.FRAGMENT_FILTER_LIST_CARD_CHECKBOX_LAYOUT_WIDTH;
        this.mCheckBoxLayout.setLayoutParams(rLp);

        this.mCheckBox = (ImageView) view.findViewById(R.id.widget_card_layout_checkBox);
        rLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        rLp.width = rLp.height = (int) this.mApplication.FRAGMENT_FILTER_LIST_CARD_CHECKBOX_WIDTH;

        this.mComment = (TextView) view.findViewById(R.id.widget_card_layout_comment);
        this.mComment.setTextSize(this.mApplication.FRAGMENT_FILTER_LIST_COMMENT_TEXT_SIZE);

        if (this.mFilter.getPaid() != null) {
            this.mComment.setText("before " + sdf.format(this.mFilter.getPaid()));
            this.mComment.setTextColor(ContextCompat.getColor(this.mApplication.getBaseContext(), R.color.clr_widget_filter_paid_color));
        } else {
            this.mComment.setText(mFilter.getComment());
            this.mComment.setTextColor(ContextCompat.getColor(this.mApplication.getBaseContext(), R.color.clr_widget_filter_unpaid_color));
        }

        if ((this.mFilter.getAssigned() != null) && (this.mFilter.getAssigned())) {
            LayoutUtils.loadImage(this.mCheckBox, R.drawable.star, R.drawable.star);
        } else {
            LayoutUtils.loadImage(this.mCheckBox, R.drawable.empty_checkbox, R.drawable.empty_checkbox);
        }

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
