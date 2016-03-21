package com.nicecode.android.tender.library.slidelistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2016
 * @since 18.03.16.
 */

class ItemMainLayout extends FrameLayout {
    private static final int INTENTION_LEFT_OPEN = 1;
    private static final int INTENTION_LEFT_CLOSE = 2;
    private static final int INTENTION_LEFT_ALREADY_OPEN = 3;
    private static final int INTENTION_RIGHT_OPEN = -1;
    private static final int INTENTION_RIGHT_CLOSE = -2;
    private static final int INTENTION_RIGHT_ALREADY_OPEN = -3;
    private static final int INTENTION_SCROLL_BACK = -4;
    private static final int INTENTION_ZERO = 0;
    private int mIntention = INTENTION_ZERO;

    /* Determine whether the current slide, slide if it is, it is SCROLL_STATE_OPEN, the excessive sliding interfaces are not going to trigger slideOpen, empathy SCROLL_STATE_CLOSE */    protected static final int SCROLL_STATE_OPEN = 1;
    protected static final int SCROLL_STATE_CLOSE = 0;
    private int mScrollState = SCROLL_STATE_CLOSE;
    /* Need to scroll back when returned status */
    protected static final int SCROLL_BACK_CLICK_NOTHING = 0;
    protected static final int SCROLL_BACK_CLICK_OWN = 1;
    protected static final int SCROLL_BACK_ALREADY_CLOSED = 2;
    protected static final int SCROLL_BACK_CLICK_MENU_BUTTON = 3;

    /* Time */
    private static final int SCROLL_TIME = 500;//500ms
    private static final int SCROLL_BACK_TIME = 250;//250MS
    private static final int SCROLL_DELETE_TIME = 300;//300ms
    /* Control height */
    private int mHeight;
    /* Height changes when removed */
    private int mDeleteHeight = DEFAULT_DELETE_HEIGHT;
    private static final int DEFAULT_DELETE_HEIGHT = -4399;
    /* Sub-control button total width */
    private int mBtnLeftTotalWidth;
    private int mBtnRightTotalWidth;

    private ItemBackGroundLayout mItemLeftBackGroundLayout;
    private ItemBackGroundLayout mItemRightBackGroundLayout;
    private ItemCustomLayout mItemCustomLayout;
    /* Scroller */
    private Scroller mScroller;
    /* If the control slide */
    private boolean mIsMoving = false;
    /* Is not going across (over) */
    private boolean mWannaOver = true;
    /* Coordinate */
    private float mXDown;
    private float mYDown;
    /* Minimum sliding distance, more than before as the beginning of the slide */
    private int mTouchSlop = 0;
    /* X-direction sliding distance */
    private float mLeftDistance;
    /* Slide listener */
    private OnItemSlideListenerProxy mOnItemSlideListenerProxy;

    public ItemMainLayout(Context context) {
        this(context, null);
    }

    public ItemMainLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemMainLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
        mItemRightBackGroundLayout = new ItemBackGroundLayout(context);
        addView(mItemRightBackGroundLayout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mItemLeftBackGroundLayout = new ItemBackGroundLayout(context);
        addView(mItemLeftBackGroundLayout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mItemCustomLayout = new ItemCustomLayout(context);
        addView(mItemCustomLayout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    /**
     * Get CustomView
     *
     * @return
     */
    public ItemCustomLayout getItemCustomLayout() {
        return mItemCustomLayout;
    }

    /**
     * Get left background View
     *
     * @return
     */
    public ItemBackGroundLayout getItemLeftBackGroundLayout() {
        return mItemLeftBackGroundLayout;
    }

    /**
     * Get right background View
     *
     * @return
     */
    public ItemBackGroundLayout getItemRightBackGroundLayout() {
        return mItemRightBackGroundLayout;
    }

    /**
     * @param btnLeftTotalWidth
     * @param btnRightTotalWidth
     * @param wannaOver
     */
    public void setParams(int btnLeftTotalWidth, int btnRightTotalWidth, boolean wannaOver) {
        requestLayout();
        mBtnLeftTotalWidth = btnLeftTotalWidth;
        mBtnRightTotalWidth = btnRightTotalWidth;
        mWannaOver = wannaOver;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mDeleteHeight == DEFAULT_DELETE_HEIGHT || mDeleteHeight < 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            mHeight = getMeasuredHeight();
            for (int i = 0; i < getChildCount(); i++) {
                measureChild(getChildAt(i), widthMeasureSpec, MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY));
            }
        } else if (mDeleteHeight >= 0) {
            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), mDeleteHeight);
            for (int i = 0; i < getChildCount(); i++) {
                measureChild(getChildAt(i), widthMeasureSpec, MeasureSpec.makeMeasureSpec(mDeleteHeight, MeasureSpec.EXACTLY));
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(false);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mXDown = ev.getX();
                mYDown = ev.getY();
                // Initial distance control
                mLeftDistance = mItemCustomLayout.getLeft();
                // If there is to scroll trends, there is no
                mIsMoving = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (fingerNotMove(ev) && !mIsMoving) {//Within the scope of the finger 50
                    //Executive ListView gestures
                    getParent().requestDisallowInterceptTouchEvent(false);
                } else if (fingerLeftAndRightMove(ev) || mIsMoving) {//Slide up and down in the range of 50, mainly detected around
                    //Are there trends to scroll
                    mIsMoving = true;
                    //Gesture controls execution
                    getParent().requestDisallowInterceptTouchEvent(true);
                    float moveDistance = ev.getX() - mXDown;//This right is positive, left is negative
                    //Analyzing intent
                    if (moveDistance > 0) {//To the Right
                        if (mLeftDistance == 0) {//Disabled
                            mIntention = INTENTION_LEFT_OPEN;
                            setBackGroundVisible(true, false);
                        } else if (mLeftDistance < 0) {//The right of the button displayed
                            mIntention = INTENTION_RIGHT_CLOSE;
                        } else if (mLeftDistance > 0) {//The left button is displayed
                            mIntention = INTENTION_LEFT_ALREADY_OPEN;
                        }
                    } else if (moveDistance < 0) {//To the left
                        if (mLeftDistance == 0) {//Disabled
                            mIntention = INTENTION_RIGHT_OPEN;
                            setBackGroundVisible(false, true);
                            } else if (mLeftDistance < 0) {//The right of the button displayed
                            mIntention = INTENTION_RIGHT_ALREADY_OPEN;
                        } else if (mLeftDistance > 0) {//The left button is displayed
                            mIntention = INTENTION_LEFT_CLOSE;
                        }
                    }
                    //Calculates the distance
                    switch (mIntention) {
                        case INTENTION_LEFT_OPEN:
                        case INTENTION_LEFT_ALREADY_OPEN:
                            if (mItemLeftBackGroundLayout.getBtnViews().size() == 0) {//If left no menu, you can not slide to the left
                                break;
                            }
                            //At this point move Distance is positive, mLeft Distance is 0
                            float distanceLeftOpen = mLeftDistance + moveDistance;
                            if (!mWannaOver) {
                                distanceLeftOpen = distanceLeftOpen > mBtnLeftTotalWidth ? mBtnLeftTotalWidth : distanceLeftOpen;
                            }
                            //slide
                            mItemCustomLayout.layout((int) distanceLeftOpen, mItemCustomLayout.getTop(),
                                    mItemCustomLayout.getWidth() + (int) distanceLeftOpen, mItemCustomLayout.getBottom());
                            break;
                        case INTENTION_LEFT_CLOSE:
                            //At this point move Distance is negative, mLeft Distance is positive
                            float distanceLeftClose = mLeftDistance + moveDistance < 0 ? 0 : mLeftDistance + moveDistance;
                            //slide
                            mItemCustomLayout.layout((int) distanceLeftClose, mItemCustomLayout.getTop(),
                                    mItemCustomLayout.getWidth() + (int) distanceLeftClose, mItemCustomLayout.getBottom());
                            break;
                        case INTENTION_RIGHT_OPEN:
                        case INTENTION_RIGHT_ALREADY_OPEN:
                            if (mItemRightBackGroundLayout.getBtnViews().size() == 0) {//If there is no right of the menu, you can not slide left
                                break;
                            }
                            //At this point move Distance is negative, mLeft Distance is 0
                            float distanceRightOpen = mLeftDistance + moveDistance;
                                //distanceRightOpen is positive
                            if (!mWannaOver) {
                                distanceRightOpen = -distanceRightOpen > mBtnRightTotalWidth ? -mBtnRightTotalWidth : distanceRightOpen;
                            }
                            //slide
                            mItemCustomLayout.layout((int) distanceRightOpen, mItemCustomLayout.getTop(),
                                    mItemCustomLayout.getWidth() + (int) distanceRightOpen, mItemCustomLayout.getBottom());
                            break;
                        case INTENTION_RIGHT_CLOSE:
                            //At this point move Distance is positive, mLeft Distance negative
                            float distanceRightClose = mLeftDistance + moveDistance > 0 ? 0 : mLeftDistance + moveDistance;
                            //slide
                            mItemCustomLayout.layout((int) distanceRightClose, mItemCustomLayout.getTop(),
                                    mItemCustomLayout.getWidth() + (int) distanceRightClose, mItemCustomLayout.getBottom());

                            break;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                switch (mIntention) {
                    case INTENTION_LEFT_CLOSE:
                    case INTENTION_LEFT_OPEN:
                    case INTENTION_LEFT_ALREADY_OPEN:
                        //If the slide, then slide on a fixed position (as long as the slide of the mBtnLeftTotalWidth / 2, even if the slide out)
                        if (Math.abs(mItemCustomLayout.getLeft()) > mBtnLeftTotalWidth / 2) {
                                //Slide
                            mIntention = INTENTION_LEFT_OPEN;
                            int delta = mBtnLeftTotalWidth - Math.abs(mItemCustomLayout.getLeft());
                            mScroller.startScroll(mItemCustomLayout.getLeft(), 0, delta, 0, SCROLL_TIME);
                            if (mOnItemSlideListenerProxy != null && mScrollState != SCROLL_STATE_OPEN) {
                                mOnItemSlideListenerProxy.onSlideOpen(this, MenuItem.DIRECTION_LEFT);
                            }
                            mScrollState = SCROLL_STATE_OPEN;
                        } else {
                            mIntention = INTENTION_LEFT_CLOSE;
                            //Slip back, homing
                            mScroller.startScroll(mItemCustomLayout.getLeft(), 0, -mItemCustomLayout.getLeft(), 0, SCROLL_TIME);
                            if (mOnItemSlideListenerProxy != null && mScrollState != SCROLL_STATE_CLOSE) {
                                mOnItemSlideListenerProxy.onSlideClose(this, MenuItem.DIRECTION_LEFT);
                            }
                            mScrollState = SCROLL_STATE_CLOSE;
                        }
                        break;
                    case INTENTION_RIGHT_CLOSE:
                    case INTENTION_RIGHT_OPEN:
                    case INTENTION_RIGHT_ALREADY_OPEN:
                        if (Math.abs(mItemCustomLayout.getLeft()) > mBtnRightTotalWidth / 2) {
                            //Slide
                            mIntention = INTENTION_RIGHT_OPEN;
                            int delta = mBtnRightTotalWidth - Math.abs(mItemCustomLayout.getLeft());
                            mScroller.startScroll(mItemCustomLayout.getLeft(), 0, -delta, 0, SCROLL_TIME);
                            if (mOnItemSlideListenerProxy != null && mScrollState != SCROLL_STATE_OPEN) {
                                mOnItemSlideListenerProxy.onSlideOpen(this, MenuItem.DIRECTION_RIGHT);
                            }
                            mScrollState = SCROLL_STATE_OPEN;
                        } else {
                            mIntention = INTENTION_RIGHT_CLOSE;
                            mScroller.startScroll(mItemCustomLayout.getLeft(), 0, -mItemCustomLayout.getLeft(), 0, SCROLL_TIME);
                            //Slip back, homing
                            if (mOnItemSlideListenerProxy != null && mScrollState != SCROLL_STATE_CLOSE) {
                                mOnItemSlideListenerProxy.onSlideClose(this, MenuItem.DIRECTION_RIGHT);
                            }
                            mScrollState = SCROLL_STATE_CLOSE;
                        }
                        break;
                }
                mIntention = INTENTION_ZERO;
                postInvalidate();
                mIsMoving = false;
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * Which side do not show the display settings there
     *
     * @param leftVisible
     * @param rightVisible
     */
    private void setBackGroundVisible(boolean leftVisible, boolean rightVisible) {
        if (leftVisible) {
            if (mItemLeftBackGroundLayout.getVisibility() != VISIBLE) {
                mItemLeftBackGroundLayout.setVisibility(VISIBLE);
            }
        } else {
            if (mItemLeftBackGroundLayout.getVisibility() == VISIBLE) {
                mItemLeftBackGroundLayout.setVisibility(GONE);
            }
        }
        if (rightVisible) {
            if (mItemRightBackGroundLayout.getVisibility() != VISIBLE) {
                mItemRightBackGroundLayout.setVisibility(VISIBLE);
            }
        } else {
            if (mItemRightBackGroundLayout.getVisibility() == VISIBLE) {
                mItemRightBackGroundLayout.setVisibility(GONE);
            }
        }
    }

    /**
     * Up and down can not exceed 50
     *
     * @param ev
     * @return
     */
    private boolean fingerNotMove(MotionEvent ev) {
        return (mXDown - ev.getX() < mTouchSlop && mXDown - ev.getX() > -mTouchSlop &&
                mYDown - ev.getY() < mTouchSlop && mYDown - ev.getY() > -mTouchSlop);
    }

    /**
     * Get beyond about 50, up and down can not exceed 50
     *
     * @param ev
     * @return
     */
    private boolean fingerLeftAndRightMove(MotionEvent ev) {
        return ((ev.getX() - mXDown > mTouchSlop || ev.getX() - mXDown < -mTouchSlop) &&
                ev.getY() - mYDown < mTouchSlop && ev.getY() - mYDown > -mTouchSlop);
    }

    /**
     * Delete Item
     */
    public void deleteItem(final OnItemDeleteListenerProxy onItemDeleteListenerProxy) {
        scrollBack();
        mDeleteHeight = mHeight;
        Animation.AnimationListener animationListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mDeleteHeight = DEFAULT_DELETE_HEIGHT;
                ItemMainLayout.this.requestLayout();
                ItemMainLayout.this.getItemCustomLayout().refreshBackground();
                if (onItemDeleteListenerProxy != null) {
                    onItemDeleteListenerProxy.onDelete(ItemMainLayout.this);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };

        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1.0f) {
                    mDeleteHeight = mHeight;
                } else {
                    mDeleteHeight = mHeight - (int) (mHeight * interpolatedTime);
                }
                ItemMainLayout.this.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        animation.setAnimationListener(animationListener);
        animation.setDuration(SCROLL_DELETE_TIME);
        startAnimation(animation);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            mItemCustomLayout.layout(mScroller.getCurrX(), mItemCustomLayout.getTop(),
                    mScroller.getCurrX() + mItemCustomLayout.getWidth(), mItemCustomLayout.getBottom());
            postInvalidate();
        }
        super.computeScroll();
    }

    /**
     * reset
     */
    protected void scrollBack() {
        mIntention = INTENTION_SCROLL_BACK;
        mScroller.startScroll(mItemCustomLayout.getLeft(), 0, -mItemCustomLayout.getLeft(), 0, SCROLL_BACK_TIME);
        postInvalidate();
        mScrollState = SCROLL_STATE_CLOSE;
    }

    /**
     * @param x The position of the finger at the point
     * @return
     */
    protected int scrollBack(float x) {
        if (mScrollState == SCROLL_STATE_CLOSE) {//No slide open, but in fact it is slipped slippery homing
            return SCROLL_BACK_ALREADY_CLOSED;
        }
        if (mItemCustomLayout.getLeft() > 0) { //It has slid to the right, and slide open
            if (x > mItemCustomLayout.getLeft()) {
                //Do not click on the button to the menu
                scrollBack();
                mScrollState = SCROLL_STATE_CLOSE;
                return SCROLL_BACK_CLICK_OWN;
            }

        } else if (mItemCustomLayout.getLeft() < 0) {//It has slid to the left, and slide open
            if (x < mItemCustomLayout.getRight()) {
                //Do not click on the button to the menu
                scrollBack();
                mScrollState = SCROLL_STATE_CLOSE;
                return SCROLL_BACK_CLICK_OWN;
            }
        }
        return SCROLL_BACK_CLICK_MENU_BUTTON;
    }

    /**
     * Setting item listener slide
     *
     * @param onItemSlideListenerProxy
     */
    protected void setOnItemSlideListenerProxy(OnItemSlideListenerProxy onItemSlideListenerProxy) {
        mOnItemSlideListenerProxy = onItemSlideListenerProxy;
    }

    protected interface OnItemSlideListenerProxy {
        void onSlideOpen(View view, int direction);

        void onSlideClose(View view, int direction);
    }

    protected interface OnItemDeleteListenerProxy {
        void onDelete(View view);
    }

    /**
     * ItemMainLayout is obtained if the status of the current slide open
     *
     * @return
     */
    public int getScrollState() {
        return mScrollState;
    }
}
