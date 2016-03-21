package com.nicecode.android.tender.library.models.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.nicecode.android.tender.library.interfaces.IScrollViewData;
import com.nicecode.android.tender.library.interfaces.OnAdapterHolderAdded;

import java.util.ArrayList;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2016
 * @since 21.03.16.
 */

public class FilterListAdapter<T extends IScrollViewData>
        extends ArrayAdapter<T> {

    private Context mContext;
    private ArrayList<T> mList;
    private OnAdapterHolderAdded<T> mHolderAddedListener;

    public FilterListAdapter(Context context,
                             ArrayList<T> list,
                             OnAdapterHolderAdded<T> holderAddedListener) {
        super(context, -1);
        this.mContext = context;
        this.mList = list;
        this.mHolderAddedListener = holderAddedListener;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public T getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        T data;

        if (convertView == null) {
            data = getItem(position);
            convertView = data.initViews(this.mContext);
            if (this.mHolderAddedListener != null) {
                this.mHolderAddedListener.onHolderAdded(data);
            }
        }
        return convertView;
    }

}
