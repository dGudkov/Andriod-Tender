package com.nicecode.android.tender.fragments;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.nicecode.android.tender.ApplicationWrapper;
import com.nicecode.android.tender.R;
import com.nicecode.android.tender.dto.Filters;
import com.nicecode.android.tender.library.async.AsyncTask;
import com.nicecode.android.tender.library.interfaces.IScrollViewData;
import com.nicecode.android.tender.library.interfaces.OnAdapterHolderAdded;
import com.nicecode.android.tender.library.slidelistview.Menu;
import com.nicecode.android.tender.library.slidelistview.MenuItem;
import com.nicecode.android.tender.library.slidelistview.SlideAndDragListView;
import com.nicecode.android.tender.library.utils.WeakReference;
import com.nicecode.android.tender.models.ViewData_Filters;
import com.nicecode.android.tender.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2015
 * @since 21.03.16.
 */

public class Fragment_FilterList extends BaseFragment
        implements SlideAndDragListView.OnListItemLongClickListener,
        SlideAndDragListView.OnDragListener, SlideAndDragListView.OnSlideListener,
        SlideAndDragListView.OnListItemClickListener, SlideAndDragListView.OnMenuItemClickListener,
        SlideAndDragListView.OnItemDeleteListener,
        OnAdapterHolderAdded<ViewData_Filters> {

    private ArrayList<ViewData_Filters> mFiltersList = new ArrayList<>();
    private List<ApplicationInfo> mAppList;

    private SlideAndDragListView mFilterListView;
    private View mWaitLayout;
    private LoadFiltersTask mLoadFiltersTask;
    private View mEmptyView;
    private List<ViewData_Filters> mHoldersList = new ArrayList<>();
    private FloatingActionButton mFab;
    private Menu mMenu;
    private Adapter<ViewData_Filters> mAdapter;

    public Fragment_FilterList() {
        super();
        this.mLayoutId = R.layout.fragment_filterlist_layout;
    }

    @Override
    protected void initView(View rootView) {

        mAppList = getActivity().getPackageManager().getInstalledApplications(0);

        this.mWaitLayout = this.mRootView.findViewById(R.id.fragment_filter_list_wait_layout);
        this.mWaitLayout.setVisibility(View.INVISIBLE);

        mFab = (FloatingActionButton) rootView.findViewById(R.id.fragment_filter_list_fab);
        mFab.setBackgroundTintList(ColorStateList.valueOf(0xFF0288d1));

        this.initMenu();
        this.initUiAndListener(this.mRootView);


    }

    public void initMenu() {
        mMenu = new Menu(new ColorDrawable(Color.LTGRAY), true);
        mMenu.addItem(new MenuItem.Builder().setWidth((int) getResources().getDimension(R.dimen.fl_fragment_filter_list_menu_button_width) + 30)
                .setBackground(new ColorDrawable(ContextCompat.getColor(this.getContext(), R.color.clr_fragment_filter_list_background)))
                .setIcon(ContextCompat.getDrawable(this.getContext(), R.drawable.trash))
                .setDirection(MenuItem.DIRECTION_RIGHT)
                .build());
//        mMenu.addItem(new MenuItem.Builder().setWidth((int) getResources().getDimension(R.dimen.fl_fragment_filter_list_menu_button_width) * 2)
//                .setBackground(new ColorDrawable(Color.RED))
//                .setText("One")
//                .setTextColor(Color.GRAY)
//                .setTextSize((int) getResources().getDimension(R.dimen.fl_fragment_filter_list_menu_text_size))
//                .build());
//        mMenu.addItem(new MenuItem.Builder().setWidth((int) getResources().getDimension(R.dimen.fl_fragment_filter_list_menu_button_width))
//                .setBackground(new ColorDrawable(Color.GREEN))
//                .setText("Two")
//                .setTextColor(Color.BLACK)
//                .setTextSize((int) getResources().getDimension(R.dimen.fl_fragment_filter_list_menu_text_size))
//                .build());
    }

    public void initUiAndListener(View rootView) {
        this.mFilterListView = (SlideAndDragListView) rootView.findViewById(R.id.fragment_filter_list_list);
        this.mFilterListView.setMenu(this.mMenu);
        this.mFilterListView.setOnListItemLongClickListener(this);
        this.mFilterListView.setOnDragListener(this, mAppList);
        this.mFilterListView.setOnListItemClickListener(this);
        this.mFilterListView.setOnSlideListener(this);
        this.mFilterListView.setOnMenuItemClickListener(this);
        this.mFilterListView.setOnItemDeleteListener(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        this.mWaitLayout.setVisibility(View.VISIBLE);
        this.cancelAsyncTasks();
        this.mLoadFiltersTask = new LoadFiltersTask(this);
        this.mLoadFiltersTask.execute();

    }

    @Override
    public void onPause() {
        this.cancelAsyncTasks();
        this.clearLists(this.mHoldersList);
        this.mFilterListView.setAdapter(null);
        this.mAdapter = null;
        super.onPause();
    }

    @Override
    public void onDestroy() {
        this.mWaitLayout = null;
        this.mLoadFiltersTask = null;
        this.mFilterListView.setAdapter(null);
        this.mAdapter = null;
        this.mEmptyView = null;
        if (this.mFiltersList != null) {
            this.mFiltersList.clear();
            this.mFiltersList = null;
        }
        this.mFilterListView = null;
        this.mFab = null;
        super.onDestroy();
    }

    private void cancelAsyncTasks() {
        synchronized (this) {
            if (this.mLoadFiltersTask != null) {
                this.mLoadFiltersTask.interrupt(true);
                this.mLoadFiltersTask = null;
            }
        }
    }

    private void clearLists(List<ViewData_Filters> holders) {
        synchronized (this) {
            for (ViewData_Filters holder : holders) {
                holder.clean();
            }
            holders.clear();
        }
    }

    @Override
    protected void onToolBarClick(View view) {
        switch (view.getId()) {
            case R.id.activity_filterlist_status_bar_search_layout:
            case R.id.activity_filterlist_status_bar_search:
//                if (!this.getActivity().isFinishing()) {
//                    this.getActivity().finish();
//                }
                break;
            default:
                super.onToolBarClick(view);
        }
    }

    @Override
    public void onDragViewStart(int position) {
        Toast.makeText(getActivity(), "onDragViewStart   position--->" + position, Toast.LENGTH_SHORT).show();
        Log.i("TAG", "onDragViewStart   " + position);
    }

    @Override
    public void onDragViewMoving(int position) {
//        Toast.makeText(DemoActivity.this, "onDragViewMoving   position--->" + position, Toast.LENGTH_SHORT).show();
        Log.i("TAG", "onDragViewMoving   " + position);
    }

    @Override
    public void onDragViewDown(int position) {
        Toast.makeText(getActivity(), "onDragViewDown   position--->" + position, Toast.LENGTH_SHORT).show();
        Log.i("TAG", "onDragViewDown   " + position);
    }

    @Override
    public void onItemDelete(View view, int position) {
        mAppList.remove(position - mFilterListView.getHeaderViewsCount());
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onListItemClick(View v, int position) {
        Toast.makeText(getActivity(), "onItemClick   position--->" + position, Toast.LENGTH_SHORT).show();
        Log.i("TAG", "onListItemClick   " + position);
    }

    @Override
    public void onListItemLongClick(View view, int position) {
        Toast.makeText(getActivity(), "onItemLongClick   position--->" + position, Toast.LENGTH_SHORT).show();
        Log.i("TAG", "onListItemLongClick   " + position);
    }

    @Override
    public int onMenuItemClick(View v, int itemPosition, int buttonPosition, int direction) {
        Log.i("TAG", "onMenuItemClick   " + itemPosition + "   " + buttonPosition + "   " + direction);
        switch (direction) {
            case MenuItem.DIRECTION_LEFT:
                switch (buttonPosition) {
                    case 0:
                        return Menu.ITEM_NOTHING;
                    case 1:
                        return Menu.ITEM_SCROLL_BACK;
                }
                break;
            case MenuItem.DIRECTION_RIGHT:
                switch (buttonPosition) {
                    case 0:
                        return Menu.ITEM_SCROLL_BACK;
                    case 1:
                        return Menu.ITEM_DELETE_FROM_BOTTOM_TO_TOP;
                }
        }
        return Menu.ITEM_NOTHING;
    }

    @Override
    public void onSlideOpen(View view, View parentView, int position, int direction) {
        Toast.makeText(getActivity(), "onSlideOpen   position--->" + position + "  direction--->" + direction, Toast.LENGTH_SHORT).show();
        Log.i("TAG", "onSlideOpen   " + position);
    }

    @Override
    public void onSlideClose(View view, View parentView, int position, int direction) {
        Toast.makeText(getActivity(), "onSlideClose   position--->" + position + "  direction--->" + direction, Toast.LENGTH_SHORT).show();
        Log.i("TAG", "onSlideClose   " + position);
    }


    @Override
    public void onHolderAdded(ViewData_Filters scrollViewData) {
        synchronized (this) {
            this.mHoldersList.add(scrollViewData);
        }
    }

    private class LoadFiltersTask extends AsyncTask<Void, Void, Filters[]> {

        private WeakReference<Fragment_FilterList> mFragment;

        public LoadFiltersTask(
                Fragment_FilterList fragment) {
            this.mFragment = new WeakReference<>(fragment);
        }

        @Override
        protected void onPreExecute() {
            Fragment_FilterList fragment = this.mFragment.get();
            if (fragment != null) {
                View fade = fragment.mWaitLayout;
                if (fade != null) {
                    fade.setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        protected Filters[] doInBackground(Void... params) {
            Filters[] filter = null;
            Fragment_FilterList fragment = this.mFragment.get();
            if (fragment != null) {
                ApplicationWrapper application = fragment.mApplication;

                try {
                    this.stopIfCancelled();
                    Thread.sleep(5000);
                    String token = application.getPreferences().getToken();

                    filter = Utils.GetFilters(token);

                } catch (InterruptedException ignored) {
                }
            }
            return filter;
        }


        @Override
        protected void onPostExecute(Filters[] filters) {
            try {
                Fragment_FilterList fragment = this.mFragment.get();

                if (fragment != null) {
                    if ((filters != null) && (filters.length > 0)) {

                        ArrayList<ViewData_Filters> productList = fragment.mFiltersList;
                        productList.clear();

                        for (int i = 0; i < 1; i++)
                            for (Filters filter : filters) {
                                productList.add(
                                        new ViewData_Filters(
                                                fragment.mApplication,
                                                filter,
                                                R.layout.widget_filter_card_layout
                                        )
                                );
                                productList.add(
                                        new ViewData_Filters(
                                                fragment.mApplication,
                                                filter,
                                                R.layout.widget_filter_card_layout
                                        )
                                );
                                productList.add(
                                        new ViewData_Filters(
                                                fragment.mApplication,
                                                filter,
                                                R.layout.widget_filter_card_layout
                                        )
                                );
                                productList.add(
                                        new ViewData_Filters(
                                                fragment.mApplication,
                                                filter,
                                                R.layout.widget_filter_card_layout
                                        )
                                );
                            }

                        List<ViewData_Filters> prevHolders = fragment.mHoldersList;
                        fragment.mHoldersList = new ArrayList<>();
                        synchronized (fragment.getClass()) {
                            fragment.mAdapter = new Adapter<>(
                                    fragment.getContext(),
                                    productList,
                                    fragment
                            );
//                            FilterListAdapter<ViewData_Filters> adapter = new FilterListAdapter<>(
//                                    fragment.getContext(),
//                                    productList,
//                                    fragment);
                            fragment.mFilterListView.setAdapter(fragment.mAdapter);
                        }
                        if (fragment.mFilterListView != null) {
                            fragment.mFilterListView.setVisibility(View.VISIBLE);
                        }
                        fragment.clearLists(prevHolders);

                        if (fragment.mEmptyView != null) {
                            fragment.mEmptyView.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        if (fragment.mEmptyView != null) {
                            fragment.mEmptyView.setVisibility(View.VISIBLE);
                        }

                        if (fragment.mFilterListView != null) {
                            fragment.mFilterListView.setVisibility(View.INVISIBLE);
                            synchronized (fragment.getClass()) {
                                fragment.mFilterListView.setAdapter(null);
                                fragment.mFiltersList.clear();

                                List<ViewData_Filters> prevHolders = fragment.mHoldersList;
                                fragment.mHoldersList = new ArrayList<>();
                                fragment.clearLists(prevHolders);
                            }
                        }
                    }
                }
            } finally {
                super.onPostExecute(filters);
            }
        }

        @Override
        protected void finalizeTask() {
            if (this.mFragment != null) {
                Fragment_FilterList fragment = this.mFragment.get();
                if (fragment != null) {
                    View fade = fragment.mWaitLayout;
                    if (fade != null) {
                        fade.setVisibility(View.INVISIBLE);
                    }
                    fragment.mLoadFiltersTask = null;
                    this.mFragment = null;
                }
            }
        }
    }

    private class Adapter<T extends IScrollViewData> extends ArrayAdapter<T> {

        private Context mContext;
        private ArrayList<T> mList;
        private OnAdapterHolderAdded<T> mHolderAddedListener;

        public Adapter(Context context,
                       ArrayList<T> list,
                       OnAdapterHolderAdded<T> holderAddedListener) {
            super(context, -1);
            this.mContext = context;
            this.mList = list;
            this.mHolderAddedListener = holderAddedListener;
        }

        @Override
        public int getCount() {
            return this.mList.size();
        }

        @Override
        public T getItem(int position) {
            return this.mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

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

}
