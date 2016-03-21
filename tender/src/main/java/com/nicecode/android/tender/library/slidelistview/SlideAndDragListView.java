package com.nicecode.android.tender.library.slidelistview;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2016
 * @since 18.03.16.
 */

public class SlideAndDragListView<T> extends DragListView<T> implements WrapperAdapter.OnAdapterSlideListenerProxy,
        WrapperAdapter.OnAdapterMenuClickListenerProxy, Handler.Callback {
    /* Handler's Message Information */
    private static final int MSG_WHAT_LONG_CLICK = 1;
    /* Handler Send message needs to be extended time */
    private static final long CLICK_LONG_TRIGGER_TIME = 1000;//1s
    /* OnTouch inside the state */
    private static final int STATE_NOTHING = -1;//Lift Status
    private static final int STATE_DOWN = 0;//Pressed state
    private static final int STATE_LONG_CLICK = 1;//Long Click Status
    private static final int STATE_SCROLL = 2;//SCROLL state
    private static final int STATE_LONG_CLICK_FINISH = 3;//Click the trigger has been completed long
    private static final int STATE_MORE_FINGERS = 4;//A plurality of fingers
    private int mState = STATE_NOTHING;

    private static final int RETURN_SCROLL_BACK_OWN = 1;//I have a homing operation
    private static final int RETURN_SCROLL_BACK_OTHER = 2;//Other Location homing operation
    private static final int RETURN_SCROLL_BACK_CLICK_MENU_BUTTON = 3;//Click on the item to the bloom of menuButton
    private static final int RETURN_SCROLL_BACK_NOTHING = 0;//So the location did not return operation

    /* Vibration */
    private Vibrator mVibrator;
    /* handler */
    private Handler mHandler;
    /* Do you want to trigger itemClick */
    private boolean mIsWannaTriggerClick = true;
    /* Finger down the coordinates */
    private int mXDown;
    private int mYDown;
    /* Menu */
    private Map<Integer, Menu> mMenuMap;
    /* WrapperAdapter */
    private WrapperAdapter mWrapperAdapter;
    /* Swipe shortest distance */
    private int mShortestDistance = 25;

    /* Listener */
    private OnSlideListener mOnSlideListener;
    private OnMenuItemClickListener mOnMenuItemClickListener;
    private OnListItemLongClickListener mOnListItemLongClickListener;
    private OnListItemClickListener mOnListItemClickListener;
    private OnItemDeleteListener mOnItemDeleteListener;
    private OnListScrollListener mOnListScrollListener;

    public SlideAndDragListView(Context context) {
        this(context, null);
    }

    public SlideAndDragListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideAndDragListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mVibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        mHandler = new Handler(this);
//        mShortestDistance = ViewConfiguration.get(context).getScaledDoubleTapSlop();
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_WHAT_LONG_CLICK:
                if (mState == STATE_LONG_CLICK) {//If you get msg when the state if the state is Long Click
                    //Long click to trigger complete
                    mState = STATE_LONG_CLICK_FINISH;
                    //Click to obtain a long position
                    int position = msg.arg1;
                    //Find the view that location
                    View view = getChildAt(position - getFirstVisiblePosition());
                    //If the listener, then triggers
                    if (mOnListItemLongClickListener != null) {
                        mVibrator.vibrate(100);
                        mOnListItemLongClickListener.onListItemLongClick(view, position);
                    }
                    boolean canDrag = scrollBackByDrag(position);
                    if (canDrag && view instanceof ItemMainLayout) {
                        setDragPosition(position);
                    }
                }
                break;
        }
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //Get the coordinates
                mXDown = (int) ev.getX();
                mYDown = (int) ev.getY();
                //The current state is pressed state
                mState = STATE_DOWN;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_2_DOWN:
            case MotionEvent.ACTION_POINTER_3_DOWN:
                removeLongClickMessage();
                mState = STATE_MORE_FINGERS;
                //Consumed, do not pass go
                return true;
            case MotionEvent.ACTION_MOVE:
                if (fingerNotMove(ev) && mState != STATE_SCROLL) {//Within the scope of the finger 50
                    sendLongClickMessage(pointToPosition(mXDown, mYDown));
                    mState = STATE_LONG_CLICK;
                } else if (fingerLeftAndRightMove(ev)) {//Slide up and down in the range of 50, mainly detected around
                    removeLongClickMessage();
                    //Which one will want to pass the current slide to wrapperAdapter
                    int position = pointToPosition(mXDown, mYDown);
                    if (position != AdapterView.INVALID_POSITION) {
                        View view = getChildAt(position - getFirstVisiblePosition());
                        if (view instanceof ItemMainLayout) {
                            ItemMainLayout itemMainLayout = (ItemMainLayout) view;
                            if (isFingerMoving2Right(ev)) {//If you want to swipe to the right
                                if (itemMainLayout.getItemLeftBackGroundLayout().getBtnViews().size() == 0 &&
                                        itemMainLayout.getScrollState() == ItemMainLayout.SCROLL_STATE_CLOSE) {//But there is no Left in the Menu
                                    mState = STATE_NOTHING;
                                    //Consumption Event
                                    return true;
                                }
                            } else if (isFingerMoving2Left(ev)) {//If you want to slide to the left
                                if (itemMainLayout.getItemRightBackGroundLayout().getBtnViews().size() == 0 &&
                                        itemMainLayout.getScrollState() == ItemMainLayout.SCROLL_STATE_CLOSE) {//But there is no Right of the Menu
                                    mState = STATE_NOTHING;
                                    //Consumption Event
                                    return true;
                                }
                            }
                            mWrapperAdapter.setSlideItemPosition(position);
                            //The event passed on
                            mState = STATE_SCROLL;
                            return super.dispatchTouchEvent(ev);
                        } else {
                            mState = STATE_NOTHING;
                            //Consumption Event
                            return true;
                        }
                    } else {
                        mState = STATE_NOTHING;
                        //Consumption Event
                        return true;
                    }
                } else {
                    removeLongClickMessage();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mState == STATE_DOWN || mState == STATE_LONG_CLICK) {
                    int position = pointToPosition(mXDown, mYDown);
                    //Whether ScrollBack, yes, then do not go to perform the operation onListItemClick
                    int scrollBackState = scrollBack(position, ev.getX());
                    if (scrollBackState == RETURN_SCROLL_BACK_NOTHING) {
                        if (mOnListItemClickListener != null && mIsWannaTriggerClick) {
                            View v = getChildAt(position - getFirstVisiblePosition());
                            mOnListItemClickListener.onListItemClick(v, position);
                        }
                    }
                }
                removeLongClickMessage();
                mState = STATE_NOTHING;
                break;
            case MotionEvent.ACTION_POINTER_3_UP:
            case MotionEvent.ACTION_POINTER_2_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                mState = STATE_NOTHING;
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * The slide open the item homing
     *
     * @param position
     * @param x        coordinate
     * @return
     */
    private int scrollBack(int position, float x) {
        //Is not this current slide open
        if (mWrapperAdapter.getSlideItemPosition() == position) {
            int scrollBackSituation = mWrapperAdapter.returnSlideItemPosition(x);
            switch (scrollBackSituation) {
                case ItemMainLayout.SCROLL_BACK_CLICK_OWN:
                    return RETURN_SCROLL_BACK_OWN;
                case ItemMainLayout.SCROLL_BACK_ALREADY_CLOSED:
                    return RETURN_SCROLL_BACK_NOTHING;
                case ItemMainLayout.SCROLL_BACK_CLICK_MENU_BUTTON:
                    return RETURN_SCROLL_BACK_CLICK_MENU_BUTTON;
            }
        } else if (mWrapperAdapter.getSlideItemPosition() != -1) {
            mWrapperAdapter.returnSlideItemPosition();
            return RETURN_SCROLL_BACK_OTHER;
        }
        return RETURN_SCROLL_BACK_NOTHING;
    }

    /**
     * For logic operations drag the ScrollBack
     *
     * @param position
     * @return true--->You can drag false ---> can not drag
     */
    private boolean scrollBackByDrag(int position) {
        // not this current slide open
        if (mWrapperAdapter.getSlideItemPosition() == position) {
            return false;
        } else if (mWrapperAdapter.getSlideItemPosition() != -1) {
            mWrapperAdapter.returnSlideItemPosition();
            return true;
        }
        return true;
    }

    /**
     * remove掉message
     */
    private void removeLongClickMessage() {
        if (mHandler.hasMessages(MSG_WHAT_LONG_CLICK)) {
            mHandler.removeMessages(MSG_WHAT_LONG_CLICK);
        }
    }

    /**
     * sendMessage
     */
    private void sendLongClickMessage(int position) {
        if (!mHandler.hasMessages(MSG_WHAT_LONG_CLICK)) {
            Message message = new Message();
            message.what = MSG_WHAT_LONG_CLICK;
            message.arg1 = position;
            mHandler.sendMessageDelayed(message, CLICK_LONG_TRIGGER_TIME);
        }
    }

    /**
     * Up and down can not exceed 50
     *
     * @param ev
     * @return
     */
    private boolean fingerNotMove(MotionEvent ev) {
        return (mXDown - ev.getX() < mShortestDistance && mXDown - ev.getX() > -mShortestDistance &&
                mYDown - ev.getY() < mShortestDistance && mYDown - ev.getY() > -mShortestDistance);
    }

    /**
     * Get beyond about 50, up and down can not exceed 50
     *
     * @param ev
     * @return
     */
    private boolean fingerLeftAndRightMove(MotionEvent ev) {
        return ((ev.getX() - mXDown > mShortestDistance || ev.getX() - mXDown < -mShortestDistance) &&
                ev.getY() - mYDown < mShortestDistance && ev.getY() - mYDown > -mShortestDistance);
    }

    /**
     * Is not slide to the right
     *
     * @return
     */
    private boolean isFingerMoving2Right(MotionEvent ev) {
        return (ev.getX() - mXDown > mShortestDistance);
    }

    /**
     * Is not slide to the left
     *
     * @return
     */
    private boolean isFingerMoving2Left(MotionEvent ev) {
        return (ev.getX() - mXDown < -mShortestDistance);
    }

    /**
     * Settings Menu
     *
     * @param menu
     */
    public void setMenu(Menu menu) {
        if (mMenuMap != null) {
            mMenuMap.clear();
        } else {
            mMenuMap = new HashMap<>(1);
        }
        mMenuMap.put(menu.getMenuViewType(), menu);
    }

    /**
     * Settings menu
     *
     * @param list
     */
    public void setMenu(List<Menu> list) {
        if (mMenuMap != null) {
            mMenuMap.clear();
        } else {
            mMenuMap = new HashMap<>(list.size());
        }
        for (Menu menu : list) {
            mMenuMap.put(menu.getMenuViewType(), menu);
        }
    }

    /**
     * Settings menu
     *
     * @param menus
     */
    public void setMenu(Menu... menus) {
        if (mMenuMap != null) {
            mMenuMap.clear();
        } else {
            mMenuMap = new HashMap<>(menus.length);
        }
        for (Menu menu : menus) {
            mMenuMap.put(menu.getMenuViewType(), menu);
        }
    }

    @Override
    public void setAdapter(final ListAdapter adapter) {
        if (mMenuMap == null || mMenuMap.size() == 0) {
            throw new IllegalArgumentException("First set Menu");
        }
        mWrapperAdapter = new WrapperAdapter(getContext(), this, adapter, mMenuMap) {

            @Override
            public void onScrollStateChangedProxy(AbsListView view, int scrollState) {
                if (scrollState == WrapperAdapter.SCROLL_STATE_IDLE) {
                    mIsWannaTriggerClick = true;
                } else {
                    mIsWannaTriggerClick = false;
                }
                if (mOnListScrollListener != null) {
                    mOnListScrollListener.onScrollStateChanged(view, scrollState);
                }
            }

            @Override
            public void onScrollProxy(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (mOnListScrollListener != null) {
                    mOnListScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                }
            }

            @Override
            public void onItemDelete(View view, int position) {
                if (mOnItemDeleteListener != null) {
                    mOnItemDeleteListener.onItemDelete(view, position);
                }
            }

        };
        mWrapperAdapter.setOnAdapterSlideListenerProxy(this);
        mWrapperAdapter.setOnAdapterMenuClickListenerProxy(this);
        setRawAdapter(adapter);
        super.setAdapter(mWrapperAdapter);
    }

    /**
     * Setting item slide Listener
     *
     * @param listener
     */
    public void setOnSlideListener(OnSlideListener listener) {
        mOnSlideListener = listener;
    }

    /**
     * sliding item listener
     */
    public interface OnSlideListener {
        /**
         * When the slide open when the trigger
         *
         * @param view
         * @param parentView
         * @param position
         */
        void onSlideOpen(View view, View parentView, int position, int direction);

        /**
         * When the slide triggered when homing
         *
         * @param view
         * @param parentView
         * @param position
         */
        void onSlideClose(View view, View parentView, int position, int direction);
    }

    @Override
    public void onSlideOpen(View view, int position, int direction) {
        if (mOnSlideListener != null) {
            mOnSlideListener.onSlideOpen(view, this, position, direction);
        }
    }

    @Override
    public void onSlideClose(View view, int position, int direction) {
        if (mOnSlideListener != null) {
            mOnSlideListener.onSlideClose(view, this, position, direction);
        }
    }

    /**
     * Setting item in the button click event listener
     *
     * @param onMenuItemClickListener
     */
    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        mOnMenuItemClickListener = onMenuItemClickListener;
    }

    /**
     * The button item listener
     */
    public interface OnMenuItemClickListener {
        /**
         * Click Event
         *
         * @param v
         * @param itemPosition   The first of several item
         * @param buttonPosition The first of several button
         * @param direction      irection
         * @return Menu of the few constants reference
         */
        int onMenuItemClick(View v, int itemPosition, int buttonPosition, int direction);
    }

    @Override
    public int onMenuItemClick(View v, int itemPosition, int buttonPosition, int direction) {
        if (mOnMenuItemClickListener != null) {
            return mOnMenuItemClickListener.onMenuItemClick(v, itemPosition, buttonPosition, direction);
        }
        return Menu.ITEM_NOTHING;
    }

    @Deprecated
    @Override
    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
    }

    /**
     * Set up listeners
     *
     * @param listener
     */
    public void setOnListItemClickListener(OnListItemClickListener listener) {
        mOnListItemClickListener = listener;
    }

    /**
     * Their click event
     */
    public interface OnListItemClickListener {
        void onListItemClick(View v, int position);
    }

    /**
     * {@link #setOnListItemLongClickListener(OnListItemLongClickListener)}
     *
     * @param listener
     */
    @Deprecated
    @Override
    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener listener) {
    }

    /**
     * Set up listeners
     *
     * @param listener
     */
    public void setOnListItemLongClickListener(OnListItemLongClickListener listener) {
        mOnListItemLongClickListener = listener;
    }

    /**
     * Write your own long click events
     */
    public interface OnListItemLongClickListener {
        void onListItemLongClick(View view, int position);
    }

    public void setOnItemDeleteListener(OnItemDeleteListener onItemDeleteListener) {
        mOnItemDeleteListener = onItemDeleteListener;
    }

    public interface OnItemDeleteListener {
        void onItemDelete(View view, int position);
    }

    @Deprecated
    @Override
    public void setOnScrollListener(AbsListView.OnScrollListener l) {
        super.setOnScrollListener(l);
    }

    public void setOnListScrollListener(OnListScrollListener onListScrollListener) {
        mOnListScrollListener = onListScrollListener;
    }

    public interface OnListScrollListener {
        int SCROLL_STATE_IDLE = 0;
        int SCROLL_STATE_TOUCH_SCROLL = 1;
        int SCROLL_STATE_FLING = 2;

        void onScrollStateChanged(AbsListView view, int scrollState);

        void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount);
    }
}
