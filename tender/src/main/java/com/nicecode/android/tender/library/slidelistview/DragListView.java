package com.nicecode.android.tender.library.slidelistview;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.List;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2016
 * @since 18.03.16.
 */
public class DragListView<T> extends ListView implements View.OnDragListener {
    /* Analyzing drag up or down */
    private boolean mUp = false;
    /* Where the ListView drag in the current position */
    private int mCurrentPosition;
    /* Before the position where the ListView drag */
    private int mBeforeCurrentPosition;
    /* Prior to drag in before the ListView position */
    private int mBeforeBeforePosition;
    /* Adapter */
    protected BaseAdapter mSDAdapter;
    /* Data */
    protected List<T> mDataList;
    /* Listener */
    private OnDragListener mOnDragListener;

    public DragListView(Context context) {
        this(context, null);
    }

    public DragListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        super.setOnDragListener(this);
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        final int action = event.getAction();
        switch (action) {
            case DragEvent.ACTION_DRAG_STARTED:
                return true;
            case DragEvent.ACTION_DRAG_ENTERED:
                return true;
            case DragEvent.ACTION_DRAG_LOCATION:
                //The current move item in the ListView position
                int position = pointToPosition((int) event.getX(), (int) event.getY());
                //If the location is changed
                if (mBeforeCurrentPosition != position) {
                    //Sometimes the resulting position is -1 (AdapterView.INVALID_POSITION), ignored
                    if (position >= 0) {
                        //The judgment is up or down the
                        mUp = position - mBeforeCurrentPosition <= 0;
                        //After recording the last movement of the position
                        mBeforeBeforePosition = mBeforeCurrentPosition;
                        //Record current position
                        mBeforeCurrentPosition = position;
                    }
                }
                moveListViewUpOrDown(position);
                //Sometimes as -1 (AdapterView.INVALID_POSITION) cases ignored
                if (position >= 0) {
                    //Judgment is not to have changed position, if not replaced, then go change
                    if (position != mCurrentPosition) {
                        if (mUp) {//Up
                            int realPosition = position - getHeaderViewsCount();
                            if (realPosition >= 0 && realPosition < mDataList.size()) {//Here judgment ignores the header to drag the case
                                //Just moved one space
                                if (position - mBeforeBeforePosition == -1) {
                                    T t = mDataList.get(realPosition);
                                    mDataList.set(realPosition, mDataList.get(realPosition + 1));
                                    mDataList.set(realPosition + 1, t);
                                } else {//Suddenly moved several positions, in fact, that the above method and merge together
                                    T t = mDataList.get(mBeforeBeforePosition - getHeaderViewsCount());
                                    for (int i = mBeforeBeforePosition - getHeaderViewsCount(); i > realPosition; i--) {
                                        mDataList.set(i, mDataList.get(i - 1));
                                    }
                                    mDataList.set(realPosition, t);
                                }
                                mSDAdapter.notifyDataSetChanged();
                                //Update location
                                mCurrentPosition = position;
                            }
                        } else {
                            //header part is not, footer part is not
                            int realPosition = position - getHeaderViewsCount();
                            if (realPosition > 0 && realPosition < mDataList.size()) {
                                if (position - mBeforeBeforePosition == 1) {
                                    T t = mDataList.get(realPosition);
                                    mDataList.set(realPosition, mDataList.get(realPosition - 1));
                                    mDataList.set(realPosition - 1, t);
                                } else {
                                    T t = mDataList.get(mBeforeBeforePosition - getHeaderViewsCount());
                                    for (int i = mBeforeBeforePosition - getHeaderViewsCount(); i < realPosition; i++) {
                                        mDataList.set(i, mDataList.get(i + 1));
                                    }
                                    mDataList.set(realPosition, t);
                                }
                                mSDAdapter.notifyDataSetChanged();
                                //Update location
                                mCurrentPosition = position;
                            }
                        }
                    }
                }
                if (mOnDragListener != null) {
                    mOnDragListener.onDragViewMoving(mCurrentPosition);
                }
                return true;
            case DragEvent.ACTION_DRAG_EXITED:
                return true;
            case DragEvent.ACTION_DROP:
                mSDAdapter.notifyDataSetChanged();
                for (int i = 0; i < getLastVisiblePosition() - getFirstVisiblePosition(); i++) {
                    if (getChildAt(i) instanceof ItemMainLayout) {
                        ItemMainLayout view = (ItemMainLayout) getChildAt(i);
                        setItemVisible(view);
                    }
                }
                if (mOnDragListener != null) {
                    mOnDragListener.onDragViewDown(mCurrentPosition);
                }
                return true;
            case DragEvent.ACTION_DRAG_ENDED:
                return true;
            default:
                break;
        }
        return false;
    }

    /**
     * That will change back partially transparent
     *
     * @param itemMainLayout
     */
    private void setItemVisible(ItemMainLayout itemMainLayout) {
        if (!itemMainLayout.getItemCustomLayout().isBackgroundShowing()) {
            itemMainLayout.getItemCustomLayout().showBackground();
        }
    }


    /**
     * If the ends are judged ListView ListView up or slide down the slide
     *
     * @param position
     */
    private void moveListViewUpOrDown(int position) {
        //Topmost position ListView display
        int firstPosition = getFirstVisiblePosition();
        //Lowermost position ListView display
        int lastPosition = getLastVisiblePosition();
        //Up can be up words
        if ((position == firstPosition || position == firstPosition + 1) && firstPosition != 0) {
            smoothScrollToPosition(firstPosition - 1);
        }
        //Words can down down
        if ((position == lastPosition || position == lastPosition - 1) && lastPosition != getCount() - 1) {
            smoothScrollToPosition(lastPosition + 1);
        }
    }

    protected void setRawAdapter(ListAdapter adapter) {
        mSDAdapter = (BaseAdapter) adapter;
    }

    protected void setDragPosition(int position) {
        mCurrentPosition = position;
        mBeforeCurrentPosition = position;
        mBeforeBeforePosition = position;
        View view = getChildAt(position - getFirstVisiblePosition());
        if (mOnDragListener != null && view instanceof ItemMainLayout) {
            ItemMainLayout itemMainLayout = (ItemMainLayout) getChildAt(position - getFirstVisiblePosition());
            itemMainLayout.getItemCustomLayout().hideBackground();
            itemMainLayout.getItemLeftBackGroundLayout().setVisibility(GONE);
            itemMainLayout.getItemRightBackGroundLayout().setVisibility(GONE);
            ClipData.Item item = new ClipData.Item("1");
            ClipData data = new ClipData("1", new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
            itemMainLayout.startDrag(data, new View.DragShadowBuilder(itemMainLayout), null, 0);
            mOnDragListener.onDragViewStart(position);
            itemMainLayout.getItemCustomLayout().showBackground();
        }
    }

    @Deprecated
    @Override
    public void setOnDragListener(View.OnDragListener l) {
    }

    /**
     * Settings dialog listener, adding data
     *
     * @param onDragListener
     * @param dataList
     */
    public void setOnDragListener(OnDragListener onDragListener, List<T> dataList) {
        mOnDragListener = onDragListener;
        mDataList = dataList;
    }

    /**
     * 更新数据
     *
     * @param dataList
     */
    public void updateDataList(List<T> dataList) {
        mDataList = dataList;
    }

    /**
     * When the trigger occurs drag listeners
     */
    public interface OnDragListener {
        /**
         * Start drag
         *
         * @param position
         */
        void onDragViewStart(int position);

        /**
         * drag is moving
         *
         * @param position
         */
        void onDragViewMoving(int position);

        /**
         * drag put down
         *
         * @param position
         */
        void onDragViewDown(int position);
    }
}
