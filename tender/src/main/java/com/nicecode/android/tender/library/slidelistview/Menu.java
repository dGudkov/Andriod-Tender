package com.nicecode.android.tender.library.slidelistview;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2016
 * @since 18.03.16.
 */

public final class Menu {
    public static final int ITEM_NOTHING = 0;
    public static final int ITEM_SCROLL_BACK = 1;
    public static final int ITEM_DELETE_FROM_BOTTOM_TO_TOP = 2;

    private List<MenuItem> mLeftMenuItems;
    private List<MenuItem> mRightMenuItems;

    private Drawable mItemBackGroundDrawable;
    private boolean mWannaOver = true;

    private int mMenuViewType = 0;

    public Menu(Drawable itemBackGroundDrawable) {
        this(itemBackGroundDrawable, true);
    }

    public Menu(Drawable itemBackGroundDrawable, boolean wannaOver) {
        this(itemBackGroundDrawable, wannaOver, 0);
    }

    public Menu(Drawable itemBackGroundDrawable, boolean wannaOver, int menuViewType) {
        mItemBackGroundDrawable = itemBackGroundDrawable;
        mWannaOver = wannaOver;
        mLeftMenuItems = new ArrayList<>();
        mRightMenuItems = new ArrayList<>();
        mMenuViewType = menuViewType;
    }

    public Drawable getItemBackGroundDrawable() {
        return mItemBackGroundDrawable;
    }

    public boolean isWannaOver() {
        return mWannaOver;
    }

    public void addItem(MenuItem menuItem) {
        if (menuItem.direction == MenuItem.DIRECTION_LEFT) {
            mLeftMenuItems.add(menuItem);
        } else {
            mRightMenuItems.add(menuItem);
        }
    }

    public void addItem(MenuItem menuItem, int position) {
        if (menuItem.direction == MenuItem.DIRECTION_LEFT) {
            mLeftMenuItems.add(position, menuItem);
        } else {
            mRightMenuItems.add(position, menuItem);
        }
    }

    public boolean removeItem(MenuItem menuItem) {
        if (menuItem.direction == MenuItem.DIRECTION_LEFT) {
            return mLeftMenuItems.remove(menuItem);
        } else {
            return mRightMenuItems.remove(menuItem);
        }
    }

    protected int getTotalBtnLength(int direction) {
        int total = 0;
        if (direction == MenuItem.DIRECTION_LEFT) {
            for (MenuItem menuItem : mLeftMenuItems) {
                total += menuItem.width;
            }
            return total;
        } else {
            for (MenuItem menuItem : mRightMenuItems) {
                total += menuItem.width;
            }
            return total;
        }
    }

    /**
     * This function is not very safe, because then get their own operations to List add or remove words btn total length of the operation will not change
     *
     * @param direction
     * @return
     */
    protected List<MenuItem> getMenuItems(int direction) {
        if (direction == MenuItem.DIRECTION_LEFT) {
            return mLeftMenuItems;
        } else {
            return mRightMenuItems;
        }
    }

    public int getMenuViewType() {
        return mMenuViewType;
    }
}
