package com.nicecode.android.tender.library.slidelistview;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2016
 * @since 18.03.16.
 */

class ItemBackGroundLayout extends ViewGroup {
    /* Background color */
    private ImageView mBGImage;
    /* Next View Distance */
    private int mMarginLeft = 0;
    private int mMarginRight = 0;
    /* Add sub-View */
    private List<View> mBtnViews;

    public ItemBackGroundLayout(Context context) {
        this(context, null);
    }

    public ItemBackGroundLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemBackGroundLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mBGImage = new ImageView(context);
        mBGImage.setBackgroundColor(Color.TRANSPARENT);
        addView(mBGImage, 0, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mBtnViews = new ArrayList<>();
        setVisibility(GONE);
    }

    public View addMenuItem(MenuItem menuItem) {
        int count = getChildCount();
        if (!TextUtils.isEmpty(menuItem.text)) {
            TextView textView = new TextView(getContext());
            Compat.setBackgroundDrawable(textView, menuItem.background);
            textView.setText(menuItem.text);
            textView.setTextSize(menuItem.textSize);
            textView.setTextColor(menuItem.textColor);
            textView.setGravity(Gravity.CENTER);
            textView.setTag(menuItem);
            addView(textView, count, new LayoutParams(menuItem.width, LayoutParams.MATCH_PARENT));
            requestLayout();
            mBtnViews.add(textView);
            return textView;
        } else if (menuItem.icon != null) {
            ImageView imageView = new ImageView(getContext());
            Compat.setBackgroundDrawable(imageView, menuItem.background);
            imageView.setImageDrawable(menuItem.icon);
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            imageView.setTag(menuItem);
            addView(imageView, count, new LayoutParams(menuItem.width, LayoutParams.MATCH_PARENT));
            requestLayout();
            mBtnViews.add(imageView);
            return imageView;
        } else {
            throw new IllegalArgumentException("Have to have a!");
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int total = getChildCount();
        for (int i = 0; i < total; i++) {
            View view = getChildAt(i);
            if (view == mBGImage) {
                measureChild(view, widthMeasureSpec, heightMeasureSpec);
            } else {
                MenuItem menuItem = (MenuItem) view.getTag();
                measureChild(view, MeasureSpec.makeMeasureSpec(menuItem.width, MeasureSpec.EXACTLY),
                        heightMeasureSpec);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int total = getChildCount();
        mMarginLeft = 0;
        mMarginRight = getMeasuredWidth();
        for (int i = 0; i < total; i++) {
            View view = getChildAt(i);
            if (view == mBGImage) {
                view.layout(l, t, r, b);
            } else {
                MenuItem menuItem = (MenuItem) view.getTag();
                if (menuItem.direction == MenuItem.DIRECTION_LEFT) {
                    view.layout(mMarginLeft, t, menuItem.width + mMarginLeft, b);
                    mMarginLeft += menuItem.width;
                } else {
                    view.layout(mMarginRight - menuItem.width, t, mMarginRight, b);
                    mMarginRight -= menuItem.width;
                }
            }
        }
    }

    public ImageView getBackGroundImage() {
        return mBGImage;
    }

    public List<View> getBtnViews() {
        return mBtnViews;
    }
}
